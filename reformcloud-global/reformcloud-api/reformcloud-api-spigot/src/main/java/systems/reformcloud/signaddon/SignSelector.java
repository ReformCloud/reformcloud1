/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signaddon;

import com.google.common.base.Enums;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.reflect.TypeToken;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.CommandReformSigns;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.internal.events.CloudServerAddEvent;
import systems.reformcloud.internal.events.CloudServerInfoUpdateEvent;
import systems.reformcloud.internal.events.CloudServerRemoveEvent;
import systems.reformcloud.launcher.SpigotBootstrap;
import systems.reformcloud.meta.enums.ServerState;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.packets.PacketOutDeleteSign;
import systems.reformcloud.network.query.out.PacketOutQueryGetSigns;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.signs.SignLayout;
import systems.reformcloud.signs.SignLayoutConfiguration;
import systems.reformcloud.signs.SignPosition;
import systems.reformcloud.signs.map.TemplateMap;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.map.MapUtility;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

public final class SignSelector {

    private static SignSelector instance;

    private SignLayoutConfiguration signLayoutConfiguration;
    private Worker worker;
    private Map<UUID, Sign> signMap;

    /**
     * Creates a new SignSelector instance
     */
    public SignSelector() throws Throwable {
        if (instance == null) {
            instance = this;
        } else {
            throw new InstanceAlreadyExistsException();
        }

        ReformCloudAPISpigot.getInstance().getChannelHandler()
            .sendPacketQuerySync("ReformCloudController",
                ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName(),
                new PacketOutQueryGetSigns(),
                (configuration, resultID) -> {
                    this.signLayoutConfiguration = configuration
                        .getValue("signConfig", TypeTokenAdaptor.getSIGN_LAYOUT_CONFIG_TYPE());
                    this.signMap = configuration
                        .getValue("signMap", new TypeToken<Map<UUID, Sign>>() {
                        }.getType());

                    SpigotBootstrap.getInstance().getServer().getPluginManager()
                        .registerEvents(new ListenerImpl(), SpigotBootstrap.getInstance());

                    CommandReformSigns commandReformSigns = new CommandReformSigns();
                    SpigotBootstrap.getInstance().getServer().getPluginManager()
                        .registerEvents(commandReformSigns, SpigotBootstrap.getInstance());

                    PluginCommand pluginCommand = SpigotBootstrap.getInstance()
                        .getCommand("reformsigns");
                    pluginCommand.setExecutor(commandReformSigns);
                    pluginCommand.setAliases(Arrays.asList("rs", "sings", "cloudsigns"));
                    pluginCommand.setTabCompleter(commandReformSigns);
                    pluginCommand.setPermission("reformcloud.command.selectors");

                    this.worker = new Worker(
                        this.signLayoutConfiguration.getLoadingLayout().getPerSecondAnimation());
                    this.worker.setDaemon(true);
                    this.worker.start();

                    for (ServerInfo serverInfo : ReformCloudAPISpigot.getInstance()
                        .getInternalCloudNetwork().getServerProcessManager()
                        .getAllRegisteredServerProcesses()) {
                        if (!serverInfo.getCloudProcess().getName().equals(
                            ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess()
                                .getName())) {
                            final Sign sign = findFreeSign(serverInfo.getServerGroup().getName());
                            if (sign != null) {
                                updateSign(sign, serverInfo);
                            }
                        }
                    }
                }, (configuration, resultID) -> {
                    instance = null;
                    SpigotBootstrap.getInstance().getServer().getPluginCommand("reformsigns")
                        .unregister(SpigotBootstrap.getInstance().getCommandMap());
                });
    }

    public static SignSelector getInstance() {
        return SignSelector.instance;
    }

    public void updateAll() {
        ReformCloudAPISpigot.getInstance().getChannelHandler()
            .sendPacketQuerySync("ReformCloudController",
                ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName(),
                new PacketOutQueryGetSigns(),
                (configuration, resultID) -> {
                    this.signLayoutConfiguration = configuration
                        .getValue("signConfig", TypeTokenAdaptor.getSIGN_LAYOUT_CONFIG_TYPE());
                    this.signMap = configuration
                        .getValue("signMap", new TypeToken<Map<UUID, Sign>>() {
                        }.getType());
                });
    }

    private void setMaintenance(final Sign sign) {
        set(toNormalSign(sign.getSignPosition()), this.getGroupLayout(sign).getMaintenanceLayout(),
            null, ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerGroups()
                .get(sign.getSignPosition().getTargetGroup()));
    }

    private void setEmpty(final Sign sign) {
        SignLayout.TemplateLayout templateLayout = this
            .isTemplateLayoutAvailable(sign.getServerInfo());
        if (templateLayout != null) {
            set(toNormalSign(sign.getSignPosition()), templateLayout.getEmptyLayout(),
                sign.getServerInfo(), null);
        } else {
            set(toNormalSign(sign.getSignPosition()), this.getGroupLayout(sign).getEmptyLayout(),
                sign.getServerInfo(), null);
        }
    }

    private void setFull(final Sign sign) {
        SignLayout.TemplateLayout templateLayout = this
            .isTemplateLayoutAvailable(sign.getServerInfo());
        if (templateLayout != null) {
            set(toNormalSign(sign.getSignPosition()), templateLayout.getFullLayout(),
                sign.getServerInfo(), null);
        } else {
            set(toNormalSign(sign.getSignPosition()), this.getGroupLayout(sign).getFullLayout(),
                sign.getServerInfo(), null);
        }
    }

    private void setOnline(final Sign sign) {
        SignLayout.TemplateLayout templateLayout = this
            .isTemplateLayoutAvailable(sign.getServerInfo());
        if (templateLayout != null) {
            set(toNormalSign(sign.getSignPosition()), templateLayout.getOnlineLayout(),
                sign.getServerInfo(), null);
        } else {
            set(toNormalSign(sign.getSignPosition()), this.getGroupLayout(sign).getOnlineLayout(),
                sign.getServerInfo(), null);
        }
    }

    private void setLoading(final Sign sign, SignLayout animation) {
        set(toNormalSign(sign.getSignPosition()), animation, null,
            ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerGroups()
                .get(sign.getSignPosition().getTargetGroup()));
    }

    private SignLayout.GroupLayout getGroupLayout(final Sign sign) {
        SignLayout.GroupLayout groupLayout = this.signLayoutConfiguration.getGroupLayouts()
            .get(sign.getSignPosition().getTargetGroup());
        return groupLayout != null ? groupLayout : this.signLayoutConfiguration.getDefaultLayout();
    }

    private void set(final org.bukkit.block.Sign sign, SignLayout layout,
        final ServerInfo serverInfo, final ServerGroup serverGroup) {
        if (sign == null) {
            return;
        }

        try {
            org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
            if (signData != null) {
                if (layout == null && serverInfo == null && serverGroup == null) {
                    String[] lines = new String[]{" ", " ", " ", " "};
                    SpigotBootstrap.getInstance().getServer().getOnlinePlayers()
                        .forEach(e -> e.sendSignChange(sign.getLocation(), lines));
                    return;
                }

                if (signData.isWallSign()) {
                    Block block = sign.getLocation().getBlock()
                        .getRelative(signData.getAttachedFace());
                    Material material = Enums
                        .getIfPresent(Material.class, layout.getMaterialName().toUpperCase())
                        .orNull();
                    if (material != null) {
                        block.setType(material);
                        BlockState blockState = block.getState();
                        blockState
                            .setData(new MaterialData(material, (byte) layout.getMaterialData()));
                        blockState.update(true);
                    }
                }
            }
        } catch (final Throwable ignored) {
            try {
                sign.setEditable(true);
                BlockFace blockFace = getFace(
                    sign.getLocation().getBlock().getBlockData().getAsString());
                if (blockFace != null) {
                    BlockFace opposite = getOpposite(blockFace);
                    if (!opposite.equals(BlockFace.SELF)) {
                        Block behind = sign.getLocation().getBlock().getRelative(opposite);
                        Material material = Enums
                            .getIfPresent(Material.class, layout.getMaterialName().toUpperCase())
                            .orNull();
                        if (material != null) {
                            behind.setType(material);
                            BlockState blockState = behind.getState();
                            blockState.setData(
                                new MaterialData(material, (byte) layout.getMaterialData()));
                            blockState.update(true);
                        }
                    }
                }
            } catch (final Throwable ignored1) {
            }
        }

        if (serverInfo == null && serverGroup != null) {
            final String[] lines = layout.getLines().clone();

            for (int i = 0; i < 3; i++) {
                lines[i] = ChatColor.translateAlternateColorCodes('&', lines[i]
                    .replace("%group%", serverGroup.getName()));
            }
            this.updateSignForAllPlayers(sign, lines);
        } else if (serverInfo != null) {
            final String[] lines = layout.getLines().clone();

            for (int i = 0; i < 4; i++) {
                lines[i] = ChatColor.translateAlternateColorCodes('&', lines[i]
                    .replace("%group%", serverInfo.getServerGroup().getName())
                    .replace("%server%", serverInfo.getCloudProcess().getName())
                    .replace("%motd%", serverInfo.getMotd())
                    .replace("%online_players%",
                        Integer.toString(serverInfo.getOnlinePlayers().size()))
                    .replace("%max_players%",
                        Integer.toString(serverInfo.getServerGroup().getMaxPlayers()))
                    .replace("%state%", serverInfo.getServerState().name())
                    .replace("%host%", serverInfo.getHost())
                    .replace("%port%", Integer.toString(serverInfo.getPort()))
                    .replace("%template%",
                        serverInfo.getCloudProcess().getLoadedTemplate().getName())
                    .replace("%max_memory%", Integer.toString(serverInfo.getMaxMemory()))
                    .replace("%version%",
                        serverInfo.getServerGroup().getSpigotVersions().getVersion())
                    .replace("%version_name%",
                        serverInfo.getServerGroup().getSpigotVersions().getName())
                    .replace("%client%", serverInfo.getCloudProcess().getClient()));
            }
            this.updateSignForAllPlayers(sign, lines);
        }
    }

    private BlockFace getFace(String in) {
        if (in == null) {
            return null;
        }

        Matcher matcher = Pattern.compile("(.*)\\[facing=(.*),waterlogged=(.*)]").matcher(in);
        return matcher.matches() ? Enums
            .getIfPresent(BlockFace.class, matcher.group(2).toUpperCase()).orNull() : null;
    }

    private BlockFace getOpposite(BlockFace blockFace) {
        if (blockFace == null) {
            return BlockFace.SELF;
        }

        switch (blockFace) {
            case EAST:
                return BlockFace.WEST;
            case NORTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.EAST;
            case SOUTH:
                return BlockFace.NORTH;
            default:
                return BlockFace.SELF;
        }
    }

    private boolean isOnSign(final ServerInfo serverInfo) {
        for (Sign sign : this.signMap.values()) {
            if (sign.getServerInfo() != null && sign.getServerInfo().getCloudProcess().getName()
                .equals(serverInfo.getCloudProcess().getName())) {
                return true;
            }
        }
        return false;
    }

    public int deleteAllSigns(String group) {
        Collection<Sign> signs = MapUtility.filterAll(this.signMap.values(),
            e -> e.getSignPosition().getTargetGroup().equals(group));
        if (signs.isEmpty()) {
            return 0;
        }

        int deleted = 0;
        for (Sign sign : signs) {
            ReformCloudAPISpigot.getInstance().getChannelHandler()
                .sendDirectPacket("ReformCloudController", new PacketOutDeleteSign(sign));
            deleted++;
        }

        return deleted;
    }

    private SignLayout.TemplateLayout isTemplateLayoutAvailable(ServerInfo serverInfo) {
        if (serverInfo == null) {
            return null;
        }

        TemplateMap<String, String, SignLayout.TemplateLayout> templateTemplateMap = this.signLayoutConfiguration
            .getGroupTemplateLayouts()
            .stream()
            .filter(e -> e.getGroup().equals(serverInfo.getServerGroup().getName())
                && e.getTemplate()
                .equals(serverInfo.getCloudProcess().getLoadedTemplate().getName()))
            .findFirst()
            .orElse(null);
        if (templateTemplateMap != null) {
            return templateTemplateMap.getLayout();
        }

        return null;
    }

    private void updateSignForAllPlayers(final org.bukkit.block.Sign sign, final String[] lines) {
        SpigotBootstrap.getInstance().getServer().getOnlinePlayers().forEach(e -> {
            if (e.getWorld().getName().equals(sign.getWorld().getName()) && e.getWorld()
                .isChunkLoaded(sign.getChunk())) {
                e.sendSignChange(sign.getLocation(), lines);
            }
        });
    }

    private void updateAllSigns() {
        for (Sign sign : this.signMap.values()) {
            if (sign.getServerInfo() != null) {
                org.bukkit.block.Sign bukkitSign = this.toNormalSign(sign.getSignPosition());
                if (bukkitSign != null) {
                    this.updateSignForAllPlayers(bukkitSign, bukkitSign.getLines());
                }
            }
        }
    }

    public void handleCreateSign(final Sign sign) {
        this.signMap.put(sign.getUuid(), sign);
    }

    public void handleSignRemove(final Sign sign) {
        Bukkit.getScheduler().runTask(SpigotBootstrap.getInstance(), () -> {
            this.signMap.remove(sign.getUuid());
            updateSign(sign);
        });
    }

    private void updateSign(final Sign sign, final ServerInfo serverInfo) {
        sign.setServerInfo(serverInfo);
    }

    private void updateSign(final Sign sign) {
        sign.setServerInfo(null);
        set(toNormalSign(sign.getSignPosition()), null, null, null);
    }

    private org.bukkit.block.Sign toNormalSign(final SignPosition position) {
        final Block block = toLocation(position).getBlock();
        if (block == null || !(block.getState() instanceof org.bukkit.block.Sign)) {
            return null;
        }

        return (org.bukkit.block.Sign) block.getState();
    }

    private Location toLocation(final SignPosition position) {
        return new Location(Bukkit.getWorld(position.getWorld()), position.getX(), position.getY(),
            position.getZ());
    }

    public SignPosition toSignPosition(final String group, final Location location) {
        return new SignPosition(group, location.getWorld().getName(), location.getBlockX(),
            location.getBlockY(), location.getBlockZ());
    }

    private Sign findFreeSign(final String group) {
        for (Sign sign : this.signMap.values()) {
            if (sign.getServerInfo() == null && sign.getSignPosition().getTargetGroup()
                .equals(group)) {
                return sign;
            }
        }

        return null;
    }

    private Sign findSign(final ServerInfo serverInfo) {
        for (Sign sign : this.signMap.values()) {
            if (sign.getServerInfo() != null && sign.getServerInfo().getCloudProcess().getName()
                .equals(serverInfo.getCloudProcess().getName())) {
                return sign;
            }
        }

        return null;
    }

    private Sign getSign(final SignPosition signPosition) {
        for (Sign sign : this.signMap.values()) {
            if (sign.getSignPosition().getWorld().equals(signPosition.getWorld())
                && sign.getSignPosition().getX() == signPosition.getX()
                && sign.getSignPosition().getY() == signPosition.getY()
                && sign.getSignPosition().getZ() == signPosition.getZ()) {
                return sign;
            }
        }

        return null;
    }

    public Sign getSign(final Location location) {
        return getSign(toSignPosition(null, location));
    }

    public SignLayoutConfiguration getSignLayoutConfiguration() {
        return this.signLayoutConfiguration;
    }

    public Worker getWorker() {
        return this.worker;
    }

    public Map<UUID, Sign> getSignMap() {
        return this.signMap;
    }

    public void setSignLayoutConfiguration(SignLayoutConfiguration signLayoutConfiguration) {
        this.signLayoutConfiguration = signLayoutConfiguration;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setSignMap(Map<UUID, Sign> signMap) {
        this.signMap = signMap;
    }

    private class Worker extends Thread {

        Worker(final int animations) {
            this.animations = animations;
        }

        private int animations;
        private SignLayout currentLoadingLayout;

        @Override
        public void run() {
            while (ReformCloudAPISpigot.getInstance() != null) {
                this.currentLoadingLayout = SignSelector.this.signLayoutConfiguration
                    .getLoadingLayout().getNextLayout();

                ReformCloudAPISpigot.getInstance().getInternalCloudNetwork()
                    .getServerProcessManager().getAllRegisteredServerProcesses().forEach(e -> {
                    if (!e.getCloudProcess().getName().equals(
                        ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess()
                            .getName())
                        && !SignSelector.this.isOnSign(e) && !e.getServerState()
                        .equals(ServerState.HIDDEN)) {
                        final Sign sign = findFreeSign(e.getServerGroup().getName());
                        if (sign != null) {
                            SignSelector.this.updateSign(sign, e);
                        }
                    }
                });

                for (Sign sign : SignSelector.this.signMap.values()) {
                    if (sign == null) {
                        continue;
                    }

                    final ServerGroup serverGroup = ReformCloudAPISpigot.getInstance()
                        .getInternalCloudNetwork().getServerGroups()
                        .get(sign.getSignPosition().getTargetGroup());
                    if (serverGroup == null) {
                        continue;
                    }

                    if (sign.getServerInfo() != null && sign.getServerInfo().getServerState()
                        .equals(ServerState.HIDDEN)) {
                        sign.setServerInfo(null);
                    }

                    if (SpigotBootstrap.getInstance().isEnabled()) {
                        SpigotBootstrap.getInstance().getServer().getScheduler()
                            .runTask(SpigotBootstrap.getInstance(), () -> {
                                if (sign.getServerInfo() == null
                                    || !sign.getServerInfo().getServerState().isJoineable()
                                    || sign.getServerInfo().getServerState()
                                    .equals(ServerState.HIDDEN)) {
                                    SignSelector.this.setLoading(sign, this.currentLoadingLayout);
                                } else if (serverGroup.isMaintenance()) {
                                    SignSelector.this.setMaintenance(sign);
                                } else if (sign.getServerInfo().getOnlinePlayers().size() == 0) {
                                    SignSelector.this.setEmpty(sign);
                                } else if (sign.getServerInfo().getOnlinePlayers().size()
                                    == serverGroup.getMaxPlayers()) {
                                    SignSelector.this.setFull(sign);
                                } else {
                                    SignSelector.this.setOnline(sign);
                                }
                            });
                    } else {
                        return;
                    }
                }

                ReformCloudLibraryService.sleep(Worker.this, 1000 / this.animations);
            }
        }
    }

    private final class ListenerImpl implements Listener {

        @EventHandler(priority = EventPriority.LOW)
        public void handle(final PlayerInteractEvent event) {
            if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                && event.getClickedBlock() != null && event.getClickedBlock()
                .getState() instanceof org.bukkit.block.Sign) {
                final Sign sign = getSign(event.getClickedBlock().getLocation());
                if (sign != null
                    && sign.getServerInfo() != null
                    && !sign.getServerInfo().getServerGroup().isMaintenance()
                    && sign.getServerInfo().getServerState().isJoineable()
                    && !sign.getServerInfo().getServerState().equals(ServerState.HIDDEN)) {
                    ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
                    byteArrayDataOutput.writeUTF("Connect");
                    byteArrayDataOutput.writeUTF(sign.getServerInfo().getCloudProcess().getName());
                    event.getPlayer().sendPluginMessage(SpigotBootstrap.getInstance(), "BungeeCord",
                        byteArrayDataOutput.toByteArray());
                }
            }
        }

        @EventHandler
        public void handle(final CloudServerRemoveEvent event) {
            final Sign sign = findSign(event.getServerInfo());
            if (sign != null) {
                updateSign(sign);
            }
        }

        @EventHandler
        public void handle(final CloudServerInfoUpdateEvent event) {
            if (!event.getServerInfo().getCloudProcess().getName().equals(
                ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getName())) {
                final Sign sign = findSign(event.getServerInfo());
                if (sign != null) {
                    updateSign(sign, event.getServerInfo());
                }
            }
        }

        @EventHandler
        public void handle(final CloudServerAddEvent event) {
            Sign sign = findSign(event.getServerInfo());
            if (sign != null) {
                updateSign(sign, event.getServerInfo());
            } else {
                sign = findFreeSign(event.getServerInfo().getServerGroup().getName());
                if (sign != null) {
                    updateSign(sign, event.getServerInfo());
                }
            }
        }
    }
}
