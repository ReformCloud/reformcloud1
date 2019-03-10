/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PermissionDatabase implements Serializable {
    private File playerDir = new File("reformcloud/database/permissions/players/");

    private PermissionCache permissionCache;

    private Map<UUID, PermissionHolder> cachedPermissionHolders = new HashMap<>();

    public PermissionDatabase() {
        if (!playerDir.exists()) {
            playerDir.mkdirs();
            new File("reformcloud/permissions").mkdirs();

            new Configuration().addProperty("permissionConfig", new PermissionCache(
                    Collections.singletonList(new PermissionGroup(
                            "admin", "", "", "", 1, Collections.singletonMap("*", true)
                    )),
                    new PermissionGroup("default", "", "", "", 1, new HashMap<>())
            )).write(Paths.get("reformcloud/permissions/config.json"));
        }

        this.permissionCache = Configuration.parse(Paths.get("reformcloud/permissions/config.json"))
                .getValue("permissionConfig", TypeTokenAdaptor.getPERMISSION_CACHE_TYPE());
    }

    public void createPermissionGroup(PermissionGroup permissionGroup) {
        if (permissionCache.getAllRegisteredGroups().contains(permissionGroup))
            return;

        this.permissionCache.getAllRegisteredGroups().add(permissionGroup);
    }

    public PermissionHolder getPermissionHolder(final PermissionHolder permissionHolder) {
        if (ReformCloudController.getInstance().getPlayerDatabase().getOfflinePlayer(permissionHolder.getOfflinePlayer().getUniqueID()) == null)
            return null;

        if (this.cachedPermissionHolders.containsKey(permissionHolder.getOfflinePlayer().getUniqueID()))
            return this.cachedPermissionHolders.get(permissionHolder.getOfflinePlayer().getUniqueID());

        if (playerDir.isDirectory()) {
            for (File file : playerDir.listFiles()) {
                if (file.getName().endsWith(".json") && file.getName().replace(".json", "")
                        .equals(String.valueOf(permissionHolder.getOfflinePlayer().getUniqueID()))) {
                    try {
                        Configuration configuration = Configuration.parse(file);
                        PermissionHolder permissionHolder1 = configuration.getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
                        this.cachedPermissionHolders.put(permissionHolder.getOfflinePlayer().getUniqueID(), permissionHolder1);
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
        new Configuration().addProperty("holder", permissionHolder)
                .write(Paths.get(playerDir.getPath() + "/" + permissionHolder.getOfflinePlayer().getUniqueID() + ".json"));
        this.cachedPermissionHolders.put(permissionHolder.getOfflinePlayer().getUniqueID(), permissionHolder);

        return permissionHolder;
    }

    public void updatePermissionHolder(PermissionHolder permissionHolder) {
        this.cachedPermissionHolders.replace(permissionHolder.getOfflinePlayer().getUniqueID(), permissionHolder);
        new Configuration().addProperty("holder", permissionHolder)
                .write(Paths.get(playerDir.getPath() + "/" + permissionHolder.getOfflinePlayer().getUniqueID() + ".json"));
    }

    public void addPermission(UUID holderUniqueID, String permission, boolean set) {
        if (this.getPermissionHolder(holderUniqueID) == null)
            return;

        PermissionHolder permissionHolder = this.getPermissionHolder(holderUniqueID);
        permissionHolder.addPermission(permission, set);

        this.updatePermissionHolder(permissionHolder);
    }
}
