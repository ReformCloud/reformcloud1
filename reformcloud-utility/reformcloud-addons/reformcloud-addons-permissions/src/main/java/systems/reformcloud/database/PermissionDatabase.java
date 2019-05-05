/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.in.PacketInUpdatePermissionHolder;
import systems.reformcloud.network.out.PacketOutUpdatePermissionCache;
import systems.reformcloud.network.query.in.PacketInQueryGetPermissionCache;
import systems.reformcloud.network.query.in.PacketInQueryGetPermissionHolder;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PermissionDatabase implements Serializable {
    private File playerDir = new File("reformcloud/database/permissions/players/");

    private PermissionCache permissionCache;

    private Map<UUID, PermissionHolder> cachedPermissionHolders = new HashMap<>();

    public PermissionDatabase() {
        if (!playerDir.exists() || !Files.exists(Paths.get("reformcloud/addons/permissions/config.json"))) {
            playerDir.mkdirs();
            new File("reformcloud/addons/permissions").mkdirs();

            new Configuration().addValue("permissionConfig", new PermissionCache(false, false,
                    "%display% %player% &7|&r %message%",
                    Collections.singletonList(new PermissionGroup(
                            "admin", null, null, null, "c", 999, Collections.singletonMap("*", true)
                    )),
                    new PermissionGroup("default", null, null, null, "7", 100, new HashMap<>())
            )).write(Paths.get("reformcloud/addons/permissions/config.json"));
        }

        this.permissionCache = Configuration.parse(Paths.get("reformcloud/addons/permissions/config.json"))
                .getValue("permissionConfig", TypeTokenAdaptor.getPERMISSION_CACHE_TYPE());

        ReformCloudController.getInstance().getNettyHandler()
                //Query Handlers
                .registerQueryHandler("QueryGetPermissionCache", new PacketInQueryGetPermissionCache())
                .registerQueryHandler("QueryGetPermissionHolder", new PacketInQueryGetPermissionHolder())

                .registerHandler("UpdatePermissionHolder", new PacketInUpdatePermissionHolder());
    }

    public void createPermissionGroup(PermissionGroup permissionGroup) {
        if (permissionCache.getAllRegisteredGroups().contains(permissionGroup))
            return;

        this.permissionCache.getAllRegisteredGroups().add(permissionGroup);
    }

    public void deletePermissionGroup(PermissionGroup permissionGroup) {
        this.cachedPermissionHolders.forEach((k, v) -> {
            v.getPermissionGroups().remove(permissionGroup.getName());
            if (v.getPermissionGroups().size() == 0) {
                v.getPermissionGroups().put(this.permissionCache.getDefaultGroup().getName(), -1L);
                this.updatePermissionHolder(v);
            }
        });
        this.permissionCache.getAllRegisteredGroups().remove(permissionGroup);
    }

    public PermissionHolder getPermissionHolder(final PermissionHolder permissionHolder) {
        if (ReformCloudController.getInstance().getPlayerDatabase().getOfflinePlayer(permissionHolder.getUniqueID()) == null)
            return null;

        if (this.cachedPermissionHolders.containsKey(permissionHolder.getUniqueID()))
            return this.cachedPermissionHolders.get(permissionHolder.getUniqueID());

        if (playerDir.isDirectory()) {
            for (File file : playerDir.listFiles()) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                        .equals(String.valueOf(permissionHolder.getUniqueID()))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        PermissionHolder permissionHolder1 = configuration.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
                        List<String> copy = new ArrayList<>(permissionHolder1.getPermissionGroups().keySet());
                        int currentSize = copy.size();
                        copy.forEach(k -> {
                            if (this.getPermissionGroup(k) == null) {
                                permissionHolder1.getPermissionGroups().remove(k);
                            }
                        });
                        if (currentSize != permissionHolder1.getPermissionGroups().size())
                            this.updatePermissionHolder(permissionHolder1);

                        if (permissionHolder1.getPermissionGroups().size() == 0) {
                            permissionHolder1.getPermissionGroups().put(this.permissionCache.getDefaultGroup().getName(), -1L);
                            this.updatePermissionHolder(permissionHolder1);
                        }

                        this.cachedPermissionHolders.put(permissionHolder.getUniqueID(), permissionHolder1);
                        return permissionHolder1;
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                                "Could not load PermissionHolder", throwable);
                        return null;
                    }
                }
            }
        }

        return this.createPermissionHolder(permissionHolder);
    }

    public PermissionHolder getPermissionHolder(final UUID permissionHolderUID) {
        if (ReformCloudController.getInstance().getPlayerDatabase().getOfflinePlayer(permissionHolderUID) == null)
            return null;

        if (this.cachedPermissionHolders.containsKey(permissionHolderUID))
            return this.cachedPermissionHolders.get(permissionHolderUID);

        if (playerDir.isDirectory()) {
            for (File file : playerDir.listFiles()) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                        .equals(String.valueOf(permissionHolderUID))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        PermissionHolder permissionHolder1 = configuration.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
                        this.cachedPermissionHolders.put(permissionHolderUID, permissionHolder1);
                        return permissionHolder1;
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(),
                                "Could not load PermissionHolder", throwable);
                        return null;
                    }
                }
            }
        }

        return null;
    }

    public PermissionHolder createPermissionHolder(PermissionHolder permissionHolder) {
        new Configuration().addValue("holder", permissionHolder)
                .write(Paths.get(playerDir.getPath() + "/" + permissionHolder.getUniqueID() + ".json"));
        this.cachedPermissionHolders.put(permissionHolder.getUniqueID(), permissionHolder);

        return permissionHolder;
    }

    public void updatePermissionHolder(PermissionHolder permissionHolder) {
        this.cachedPermissionHolders.replace(permissionHolder.getUniqueID(), permissionHolder);
        new Configuration().addValue("holder", permissionHolder)
                .write(Paths.get(playerDir.getPath() + "/" + permissionHolder.getUniqueID() + ".json"));
    }

    public void updatePermissionGroup(PermissionGroup permissionGroup) {
        if (permissionGroup.getName().equalsIgnoreCase(this.permissionCache.getDefaultGroup().getName())) {
            this.permissionCache.setDefaultGroup(permissionGroup);
            return;
        }

        List<PermissionGroup> copyOf = new ArrayList<>(this.permissionCache.getAllRegisteredGroups());
        copyOf.forEach(permissionGroup1 -> {
            if (permissionGroup1.getName().equals(permissionGroup.getName()))
                this.permissionCache.getAllRegisteredGroups().remove(permissionGroup1);
        });
        this.permissionCache.getAllRegisteredGroups().add(permissionGroup);
    }

    public void addPermission(UUID holderUniqueID, String permission, boolean set) {
        if (this.getPermissionHolder(holderUniqueID) == null)
            return;

        PermissionHolder permissionHolder = this.getPermissionHolder(holderUniqueID);
        permissionHolder.addPermission(permission, set);

        this.updatePermissionHolder(permissionHolder);
    }

    public void update() {
        new Configuration().addValue("permissionConfig", this.permissionCache).write(Paths.get("reformcloud/addons/permissions/config.json"));
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
                new PacketOutUpdatePermissionCache()
        );
    }

    public PermissionGroup getPermissionGroup(String name) {
        return this.getAllGroups().stream().filter(e -> e.getName().startsWith(name)).findFirst().orElse(null);
    }

    public List<PermissionGroup> getAllGroups() {
        List<PermissionGroup> permissionGroups = new ArrayList<>(this.permissionCache.getAllRegisteredGroups());
        permissionGroups.add(this.permissionCache.getDefaultGroup());

        return permissionGroups;
    }

    public File getPlayerDir() {
        return this.playerDir;
    }

    public PermissionCache getPermissionCache() {
        return this.permissionCache;
    }

    public Map<UUID, PermissionHolder> getCachedPermissionHolders() {
        return this.cachedPermissionHolders;
    }
}
