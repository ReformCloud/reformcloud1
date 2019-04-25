/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobsaddon;

import com.google.common.base.Enums;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.commands.CommandReformMobs;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.internal.events.CloudServerAddEvent;
import systems.reformcloud.internal.events.CloudServerInfoUpdateEvent;
import systems.reformcloud.internal.events.CloudServerRemoveEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.enums.ServerState;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.mobs.SelectorMobPosition;
import systems.reformcloud.mobs.config.SelectorMobConfig;
import systems.reformcloud.mobs.inventory.SelectorMobInventory;
import systems.reformcloud.mobs.inventory.SelectorMobInventoryItem;
import systems.reformcloud.mobs.inventory.item.SelectorsMobServerItem;
import systems.reformcloud.mobsaddon.packet.in.PacketInCreateMob;
import systems.reformcloud.mobsaddon.packet.in.PacketInDeleteMob;
import systems.reformcloud.mobsaddon.packet.in.PacketInDisableMobs;
import systems.reformcloud.mobsaddon.packet.in.PacketInUpdateMobs;
import systems.reformcloud.mobsaddon.packet.out.PacketOutCreateMob;
import systems.reformcloud.mobsaddon.packet.out.PacketOutDeleteMob;
import systems.reformcloud.mobsaddon.packet.out.PacketOutRequestAll;
import systems.reformcloud.permissions.ReflectionUtil;
import systems.reformcloud.utility.map.MapUtility;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class MobSelector implements Serializable {
    private static MobSelector instance;

    private Map<UUID, SelectorMob> mobs;

    private SelectorMobConfig selectorMobConfig;

    private Map<UUID, Mob> spawnedMobs = new HashMap<>();

    public MobSelector() {
        if (instance != null)
            throw new InstanceAlreadyExistsException();

        instance = this;

        ReformCloudAPISpigot.getInstance().getNettyHandler().registerHandler("CreateMob", new PacketInCreateMob());
        ReformCloudAPISpigot.getInstance().getNettyHandler().registerHandler("DeleteMob", new PacketInDeleteMob());
        ReformCloudAPISpigot.getInstance().getNettyHandler().registerHandler("UpdateMobs", new PacketInUpdateMobs());

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendPacketQuerySync(
                "ReformCloudController",
                ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName(),
                new PacketOutRequestAll(),
                (configuration, resultID) -> {
                    this.mobs = configuration.getValue("mobs", new TypeToken<Map<UUID, SelectorMob>>() {
                    }.getType());
                    this.selectorMobConfig = configuration.getValue("config", new TypeToken<SelectorMobConfig>() {
                    }.getType());

                    PluginCommand pluginCommand = SpigotBootstrap.getInstance().getCommand("mobs");
                    pluginCommand.setExecutor(new CommandReformMobs());
                    pluginCommand.setPermission("reformcloud.commands.mobs");

                    ReformCloudAPISpigot.getInstance().getNettyHandler().registerHandler("DisableMobs", new PacketInDisableMobs());

                    SpigotBootstrap.getInstance().getServer().getPluginManager().registerEvents(new BukkitListenerImpl(), SpigotBootstrap.getInstance());
                    SpigotBootstrap.getInstance().getServer().getPluginManager().registerEvents(new CloudListenerImpl(), SpigotBootstrap.getInstance());

                    this.mobs
                            .values()
                            .stream()
                            .filter(e -> Bukkit.getWorld(e.getSelectorMobPosition().getWorld()) != null)
                            .forEach(Mob::new);
                }, (configuration, resultID) -> {
                    instance = null;
                    ReformCloudAPISpigot.getInstance().getNettyHandler().unregisterHandler("CreateMob");
                    ReformCloudAPISpigot.getInstance().getNettyHandler().unregisterHandler("DeleteMob");
                    ReformCloudAPISpigot.getInstance().getNettyHandler().unregisterHandler("UpdateMobs");
                }
        );
    }

    public static MobSelector getInstance() {
        return MobSelector.instance;
    }

    public Location toLocation(SelectorMobPosition selectorMobPosition) {
        return new Location(
                Bukkit.getWorld(selectorMobPosition.getWorld()),
                selectorMobPosition.getX(),
                selectorMobPosition.getY(),
                selectorMobPosition.getZ(),
                (float) selectorMobPosition.getYaw(),
                (float) selectorMobPosition.getPitch()
        );
    }

    public SelectorMobPosition toPosition(String group, Location location) {
        return new SelectorMobPosition(
                group,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public Collection<SelectorMob> getMobs() {
        List<SelectorMob> out = new ArrayList<>();
        this.spawnedMobs
                .values()
                .forEach(e -> out.add(e.selectorMob));

        return out;
    }

    private ServerInfo findBestServer(Collection<String> servers) {
        ServerInfo best = null;
        for (String server : servers) {
            ServerInfo serverInfo = ReformCloudAPISpigot.getInstance().getServerInfo(server);
            if (serverInfo == null)
                continue;

            if (serverInfo.isFull()
                    || !serverInfo.getServerState().isJoineable()
                    || serverInfo.getServerState().equals(ServerState.HIDDEN)
                    || serverInfo.getCloudProcess().getName()
                    .equals(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()))
                continue;

            if (best == null) {
                best = serverInfo;
                continue;
            }

            if (serverInfo.getOnline() > best.getOnline())
                best = serverInfo;
        }

        return best;
    }

    private Inventory newInventory(SelectorMob selectorMob) {
        SelectorMobInventory mobInventory = selectorMobConfig.getSelectorMobInventory();
        Inventory inventory = Bukkit.createInventory(
                null,
                mobInventory.getSize() % 9 != 0
                        ? 54
                        : mobInventory.getSize(),
                this.formatInventoryName(mobInventory.getName(), selectorMob.getSelectorMobPosition().getTargetGroup())
        );
        for (SelectorMobInventoryItem item : mobInventory.getItems()) {
            ItemStack itemStack = new ItemStack(Enums.getIfPresent(Material.class, item.getMaterialName()).isPresent()
                    ? Material.getMaterial(item.getMaterialName())
                    : Material.GLASS_PANE, 1, item.getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(item.getName());
                itemStack.setItemMeta(itemMeta);
            }
            inventory.setItem(item.getSlot(), itemStack);
        }

        return inventory;
    }

    public void clearInventory(Inventory inventory) {
        if (inventory == null)
            return;

        inventory.clear();

        SelectorMobInventory mobInventory = selectorMobConfig.getSelectorMobInventory();
        for (SelectorMobInventoryItem item : mobInventory.getItems()) {
            ItemStack itemStack = new ItemStack(Enums.getIfPresent(Material.class, item.getMaterialName()).isPresent()
                    ? Material.getMaterial(item.getMaterialName())
                    : Material.GLASS_PANE, 1, item.getSubId());
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(item.getName());
                itemStack.setItemMeta(itemMeta);
            }
            inventory.setItem(item.getSlot(), itemStack);
        }
    }

    private ItemStack itemForServer(ServerInfo serverInfo) {
        SelectorsMobServerItem serverItem = this.selectorMobConfig.getSelectorsMobServerItem();
        ItemStack itemStack = new ItemStack(Enums.getIfPresent(Material.class, serverItem.getItemName()).isPresent()
                ? Material.getMaterial(serverItem.getItemName())
                : Material.GLASS_PANE, 1, serverItem.getSubId());
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(format(serverItem.getName(), serverInfo));
        itemMeta.setLore(format(serverItem.getLore(), serverInfo));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String format(String input, ServerInfo serverInfo) {
        if (input == null || serverInfo == null)
            return input;

        input.replace("%server_name%", serverInfo.getCloudProcess().getName())
                .replace("%server_uid%", serverInfo.getCloudProcess().getProcessUID().toString())
                .replace("%server_group_name%", serverInfo.getCloudProcess().getGroup())
                .replace("%server_motd%", serverInfo.getMotd())
                .replace("%server_client%", serverInfo.getCloudProcess().getClient())
                .replace("%server_template%", serverInfo.getCloudProcess().getLoadedTemplate().getName())
                .replace("%server_id%", Integer.toString(serverInfo.getCloudProcess().getProcessID()))
                .replace("%server_state%", serverInfo.getServerState().name())
                .replace("%server_online_players%", Integer.toString(serverInfo.getOnline()))
                .replace("%server_host%", serverInfo.getHost())
                .replace("%server_port%", Integer.toString(serverInfo.getPort()))
                .replace("%server_max_players%", Integer.toString(serverInfo.getServerGroup().getMaxPlayers()));

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private String formatDisplayName(String input, String group) {
        if (input == null || group == null)
            return input;

        input
                .replace("%group_name%", group)
                .replace("%group_online%", Integer.toString(ReformCloudAPISpigot.getInstance().getAllRegisteredServers(group).size()));

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private String formatInventoryName(String input, String group) {
        if (input == null || group == null)
            return input;

        input.replace("%group_name%", group);

        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private List<String> format(List<String> input, ServerInfo serverInfo) {
        if (input == null || serverInfo == null || input.isEmpty())
            return input;

        List<String> output = new ArrayList<>(input.size());
        for (String line : input)
            output.add(format(line, serverInfo));
        return output;
    }

    public Mob findMobByInventory(Inventory inventory) {
        return this.spawnedMobs
                .values()
                .stream()
                .filter(e -> e.inventory.equals(inventory))
                .findFirst()
                .orElse(null);
    }

    public Collection<Mob> findMobsByGroup(String group) {
        return this.spawnedMobs
                .values()
                .stream()
                .filter(e -> e.selectorMob.getSelectorMobPosition().getTargetGroup().equals(group))
                .collect(Collectors.toList());
    }

    public void createMob(SelectorMob selectorMob) {
        ReformCloudAPISpigot.getInstance().getChannelHandler().sendDirectPacket("ReformCloudController", new PacketOutCreateMob(selectorMob));
    }

    public Mob findMobByName(String name) {
        return this.spawnedMobs
                .values()
                .stream()
                .filter(e -> e.selectorMob.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean deleteMob(String name) {
        Mob mob = findMobByName(name);
        if (mob == null)
            return false;

        ReformCloudAPISpigot.getInstance().getChannelHandler().sendDirectPacket(
                "ReformCloudController",
                new PacketOutDeleteMob(mob.selectorMob.getUniqueID())
        );
        return true;
    }

    public int deleteAllMobs(String group) {
        int deleted = 0;
        Collection<Mob> groupMobs = MapUtility.filterAll(this.spawnedMobs.values(), e -> e.selectorMob.getSelectorMobPosition().getTargetGroup().equals(group));
        for (Mob mob : groupMobs) {
            ReformCloudAPISpigot.getInstance().getChannelHandler().sendDirectPacket(
                    "ReformCloudController",
                    new PacketOutDeleteMob(mob.selectorMob.getUniqueID())
            );
            deleted++;
        }

        return deleted;
    }

    public void handleServerAdd(Collection<Mob> mobs, ServerInfo serverInfo) {
        mobs.forEach(e -> e.addServer(serverInfo));
    }

    public void handleServerDelete(Collection<Mob> mobs, ServerInfo serverInfo) {
        mobs.forEach(e -> e.removeServer(serverInfo));
    }

    public void handleServerInfoUpdate(Collection<Mob> mobs, ServerInfo serverInfo) {
        mobs.forEach(e -> e.updateServerInfo(serverInfo));
    }

    public void handleCreateMob(SelectorMob selectorMob) {
        this.mobs.put(selectorMob.getUniqueID(), selectorMob);
        new Mob(selectorMob);
    }

    public void handleDeleteMob(SelectorMob selectorMob) {
        this.mobs.remove(selectorMob.getUniqueID());
        Mob mob = findMobByName(selectorMob.getName());
        if (mob == null)
            return;

        this.spawnedMobs.remove(mob.entity.getUniqueId()).despawn();
    }

    public SelectorMobConfig getSelectorMobConfig() {
        return this.selectorMobConfig;
    }

    public Map<UUID, Mob> getSpawnedMobs() {
        return this.spawnedMobs;
    }

    public void setMobs(Map<UUID, SelectorMob> mobs) {
        this.mobs = mobs;
    }

    public void setSelectorMobConfig(SelectorMobConfig selectorMobConfig) {
        this.selectorMobConfig = selectorMobConfig;
    }

    private class Mob {
        private Mob(SelectorMob selectorMob) {
            this.selectorMob = selectorMob;
            this.location = toLocation(selectorMob.getSelectorMobPosition());
            this.inventory = MobSelector.this.newInventory(this.selectorMob);
            EntityType entityType = MapUtility.filter(EntityType.values(), e -> e.getEntityClass() != null
                    && e.getEntityClass().getSimpleName().equals(selectorMob.getEntityClassName()));
            if (entityType != null)
                this.entityType = entityType;
            else
                this.entityType = EntityType.WITCH;

            Collection<ServerInfo> servers = ReformCloudAPISpigot.getInstance().getAllRegisteredServers(selectorMob.getSelectorMobPosition().getTargetGroup());
            for (ServerInfo serverInfo : servers)
                if (serverInfo.getServerState().equals(ServerState.READY))
                    addServer(serverInfo);

            this.spawn();
        }

        private SelectorMob selectorMob;

        private Location location;

        private Entity entity;

        private EntityType entityType;

        private Inventory inventory;

        private Map<String, Integer> infos = new HashMap<>();

        private Map<String, Integer> servers = new HashMap<>();

        private Map<Integer, ServerInfo> sorted = new TreeMap<>();

        private void addServer(ServerInfo serverInfo) {
            if (infos.containsKey(serverInfo.getCloudProcess().getName()))
                return;

            this.sorted.put(serverInfo.getCloudProcess().getProcessID(), serverInfo);
            this.infos.clear();

            MobSelector.this.clearInventory(this.inventory);

            sorted.values().forEach(e -> {
                int i = 0;
                for (ItemStack itemStack : inventory.getContents()) {
                    if (itemStack == null || itemStack.getType() == null || itemStack.getType() == Material.AIR) {
                        ItemStack itemStack1 = itemForServer(e);
                        inventory.setItem(i, itemStack1);
                        servers.put(e.getCloudProcess().getName(), i);

                        this.infos.put(e.getCloudProcess().getName(), i);

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            if (player.getOpenInventory() != null
                                    && player.getOpenInventory().getTopInventory() != null
                                    && player.getOpenInventory().getTopInventory().equals(inventory)) {
                                player.updateInventory();
                            }
                        });
                        break;
                    }
                    i++;
                }
            });

            if (entity != null)
                this.entity.setCustomName(MobSelector.this.formatDisplayName(selectorMob.getDisplayName(), selectorMob.getSelectorMobPosition().getTargetGroup()));
        }

        private void updateServerInfo(ServerInfo serverInfo) {
            if (!infos.containsKey(serverInfo.getCloudProcess().getName())) {
                addServer(serverInfo);
                return;
            }

            this.sorted.replace(serverInfo.getCloudProcess().getProcessID(), serverInfo);

            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null
                        && itemStack.getType() != null
                        && !itemStack.getType().equals(Material.AIR)
                        && itemStack.hasItemMeta()) {
                    if (servers.containsKey(serverInfo.getCloudProcess().getName())) {
                        ItemStack itemStack1 = itemForServer(serverInfo);
                        inventory.setItem(servers.get(serverInfo.getCloudProcess().getName()), itemStack1);

                        Bukkit.getOnlinePlayers().forEach(e -> {
                            if (e.getOpenInventory() != null
                                    && e.getOpenInventory().getTopInventory() != null
                                    && e.getOpenInventory().getTopInventory().equals(inventory)) {
                                e.updateInventory();
                            }
                        });
                    } else {
                        addServer(serverInfo);
                        break;
                    }
                }
            }
        }

        private void removeServer(ServerInfo serverInfo) {
            if (!infos.containsKey(serverInfo.getCloudProcess().getName()))
                return;

            this.sorted.remove(serverInfo.getCloudProcess().getProcessID());

            servers.remove(serverInfo.getCloudProcess().getName());
            Collection<ServerInfo> serverInfos = MapUtility.filterAll(
                    ReformCloudAPISpigot.getInstance().getAllRegisteredServers(), e -> servers.containsKey(e.getCloudProcess().getName())
            );
            servers.clear();
            infos.clear();

            MobSelector.this.clearInventory(this.inventory);

            Bukkit.getScheduler().runTaskLater(SpigotBootstrap.getInstance(), () -> {
                if (entity != null)
                    this.entity.setCustomName(MobSelector.this.formatDisplayName(selectorMob.getDisplayName(), selectorMob.getSelectorMobPosition().getTargetGroup()));
            }, 20);

            serverInfos.forEach(this::addServer);
        }

        private void despawn() {
            spawnedMobs.remove(this.entity.getUniqueId());
            this.entity.remove();
            this.entity = null;
        }

        private void spawn() {
            if (this.entity != null)
                this.despawn();

            SpigotBootstrap.getInstance().getServer().getScheduler().runTask(SpigotBootstrap.getInstance(), () -> {
                this.entity = this.location.getWorld().spawnEntity(this.location, this.entityType);
                this.entity.setCustomName(MobSelector.this.formatDisplayName(selectorMob.getDisplayName(), selectorMob.getSelectorMobPosition().getTargetGroup()));
                this.entity.setCustomNameVisible(true);
                this.entity.setFireTicks(0);
                this.entity.setOp(false);
                this.entity.setGravity(false);
                if (this.entity instanceof LivingEntity)
                    ((LivingEntity) this.entity).setCollidable(false);

                if (this.entity instanceof Villager)
                    ((Villager) this.entity).setProfession(Villager.Profession.FARMER);

                ReflectionUtil.setNoAI(this.entity);
                if (!spawnedMobs.containsKey(this.entity.getUniqueId()))
                    spawnedMobs.put(this.entity.getUniqueId(), this);
            });
        }
    }

    private class BukkitListenerImpl implements Listener {
        @EventHandler
        public void handle(final PlayerInteractEntityEvent event) {
            Player player = event.getPlayer();
            if (event.getRightClicked() != null) {
                Mob mob = MobSelector.this.getSpawnedMobs().get(event.getRightClicked().getUniqueId());
                if (mob != null) {
                    event.setCancelled(true);
                    player.openInventory(mob.inventory);
                }
            }
        }

        @EventHandler
        public void handle(final EntityDamageEvent event) {
            if (event.getEntity().getType().isSpawnable()) {
                Mob mob = MobSelector.this.getSpawnedMobs().get(event.getEntity().getUniqueId());
                if (mob != null) {
                    event.getEntity().setFireTicks(0);
                    event.setCancelled(true);
                }
            }
        }

        @EventHandler
        public void handle(final EntityDamageByEntityEvent event) {
            if (event.getEntity().getType().isSpawnable()) {
                Mob mob = MobSelector.this.getSpawnedMobs().get(event.getEntity().getUniqueId());
                if (mob != null) {
                    event.getEntity().setFireTicks(0);
                    event.setCancelled(true);

                    if (!(event.getDamager() instanceof Player))
                        return;

                    ServerInfo serverInfo = MobSelector.this.findBestServer(mob.infos.keySet());
                    if (serverInfo == null)
                        return;

                    ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
                    byteArrayDataOutput.writeUTF("Connect");
                    byteArrayDataOutput.writeUTF(serverInfo.getCloudProcess().getName());
                    ((Player) event.getDamager()).sendPluginMessage(SpigotBootstrap.getInstance(), "BungeeCord", byteArrayDataOutput.toByteArray());
                }
            }
        }

        @EventHandler
        public void handle(final InventoryClickEvent event) {
            if (!(event.getWhoClicked() instanceof Player) || event.getClickedInventory() == null || event.getCurrentItem() == null)
                return;

            Player player = (Player) event.getWhoClicked();
            Mob mob = MobSelector.this.findMobByInventory(event.getClickedInventory());
            if (mob == null)
                return;

            event.setCancelled(true);

            String server = null;
            for (Map.Entry<String, Integer> entry : mob.infos.entrySet()) {
                if (entry.getValue() == event.getRawSlot()) {
                    server = entry.getKey();
                    break;
                }
            }

            if (server == null || server.equals(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName()))
                return;

            ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
            byteArrayDataOutput.writeUTF("Connect");
            byteArrayDataOutput.writeUTF(server);
            player.sendPluginMessage(SpigotBootstrap.getInstance(), "BungeeCord", byteArrayDataOutput.toByteArray());
        }

        @EventHandler
        public void handle(final WorldLoadEvent event) {
            if (mobs == null)
                return;

            Collection<SelectorMob> worldMobs = MapUtility.filterAll(MobSelector.this.mobs.values(),
                    e -> e.getSelectorMobPosition().getWorld().equals(event.getWorld().getName()));
            if (worldMobs.isEmpty())
                return;

            worldMobs.forEach(Mob::new);
        }

        @EventHandler
        public void handle(final WorldUnloadEvent event) {
            if (spawnedMobs == null)
                return;

            event.getWorld().getEntities()
                    .stream()
                    .filter(e -> MobSelector.this.spawnedMobs.containsKey(e.getUniqueId()))
                    .forEach(e -> MobSelector.this.spawnedMobs.get(e.getUniqueId()).despawn());
        }

        @EventHandler
        public void handle(final WorldSaveEvent event) {
            Collection<Mob> inWorld = MapUtility.filterAll(spawnedMobs.values(),
                    e -> e.entity.getWorld().getName().equals(event.getWorld().getName()));
            if (inWorld.isEmpty())
                return;

            inWorld.forEach(Mob::despawn);
            SpigotBootstrap.getInstance().getServer().getScheduler().runTaskLater(SpigotBootstrap.getInstance(), () -> {
                inWorld.forEach(Mob::spawn);
            }, 60);
        }
    }

    private class CloudListenerImpl implements Listener {
        @EventHandler
        public void handle(final CloudServerAddEvent event) {
            if (event.getServerInfo().getServerState().equals(ServerState.READY)) {
                Collection<Mob> mobs = findMobsByGroup(event.getServerInfo().getCloudProcess().getGroup());
                if (mobs.isEmpty())
                    return;

                handleServerAdd(mobs, event.getServerInfo());
            }
        }

        @EventHandler
        public void handle(final CloudServerInfoUpdateEvent event) {
            Collection<Mob> mobs = findMobsByGroup(event.getServerInfo().getCloudProcess().getGroup());
            if (mobs.isEmpty())
                return;

            if (event.getServerInfo().getServerState().equals(ServerState.READY))
                handleServerInfoUpdate(mobs, event.getServerInfo());
            else
                handleServerDelete(mobs, event.getServerInfo());
        }

        @EventHandler
        public void handle(final CloudServerRemoveEvent event) {
            Collection<Mob> mobs = findMobsByGroup(event.getServerInfo().getCloudProcess().getGroup());
            if (mobs.isEmpty())
                return;

            handleServerDelete(mobs, event.getServerInfo());
        }
    }

    public void close() {
        instance = null;
        ReformCloudAPISpigot.getInstance().getNettyHandler().unregisterHandler("CreateMob");
        ReformCloudAPISpigot.getInstance().getNettyHandler().unregisterHandler("DeleteMob");
        ReformCloudAPISpigot.getInstance().getNettyHandler().unregisterHandler("UpdateMobs");
        ReformCloudAPISpigot.getInstance().getNettyHandler().unregisterHandler("DisableMobs");

        SpigotBootstrap.getInstance().getCommand("mobs").setExecutor(null);

        Collection<Mob> spawnedCopy = new ArrayList<>(this.spawnedMobs.values());
        spawnedCopy.forEach(Mob::despawn);
    }
}
