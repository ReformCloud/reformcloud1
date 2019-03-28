/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import com.google.common.base.Enums;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import systems.reformcloud.api.IAPIService;
import systems.reformcloud.api.IDefaultPlayerProvider;
import systems.reformcloud.api.IEventHandler;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.commands.ingame.sender.IngameCommandSender;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.launcher.BungeecordBootstrap;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketClient;
import systems.reformcloud.network.api.event.NetworkEventAdapter;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.in.*;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.packets.PacketOutStartGameServer;
import systems.reformcloud.network.packets.PacketOutStartProxy;
import systems.reformcloud.network.packets.PacketOutUpdateOfflinePlayer;
import systems.reformcloud.network.packets.PacketOutUpdateOnlinePlayer;
import systems.reformcloud.network.query.out.PacketOutQueryGetOnlinePlayer;
import systems.reformcloud.network.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.player.api.PlayerProvider;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.threading.TaskScheduler;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 01.11.2018
 */

@Setter
@Getter
public class ReformCloudAPIBungee implements IAPIService {
    @Getter
    public static ReformCloudAPIBungee instance;

    private final NettySocketClient nettySocketClient;
    //TODO
    private final ChannelHandler channelHandler = new ChannelHandler(new TaskScheduler());

    private final ProxyStartupInfo proxyStartupInfo;
    private ProxyInfo proxyInfo;
    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private PermissionCache permissionCache;

    private Map<UUID, PermissionHolder> cachedPermissionHolders = new HashMap<>();
    private List<IngameCommand> registeredIngameCommands = new ArrayList<>();

    private long internalTime = System.currentTimeMillis();

    /**
     * Creates a new BungeeCord Plugin instance
     *
     * @throws Throwable
     */
    public ReformCloudAPIBungee() throws Throwable {
        if (instance == null)
            instance = this;
        else
            throw new InstanceAlreadyExistsException();

        ReformCloudLibraryService.sendHeader();

        IAPIService.instance.set(this);
        IDefaultPlayerProvider.instance.set(new PlayerProvider());

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/config.json"));

        final EthernetAddress ethernetAddress = configuration.getValue("address", TypeTokenAdaptor.getETHERNET_ADDRESS_TYPE());
        new ReformCloudLibraryServiceProvider(new LoggerProvider(), configuration.getStringValue("controllerKey"), ethernetAddress.getHost(), new EventManager(), null);
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().setDebug(configuration.getBooleanValue("debug"));

        this.proxyStartupInfo = configuration.getValue("startupInfo", TypeTokenAdaptor.getPROXY_STARTUP_INFO_TYPE());
        this.proxyInfo = configuration.getValue("info", TypeTokenAdaptor.getPROXY_INFO_TYPE());

        IEventHandler.instance.set(new NetworkEventAdapter());

        this.getNettyHandler().registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal())
                .registerHandler("ProcessAdd", new PacketInProcessAdd())
                .registerHandler("ProcessRemove", new PacketInProcessRemove())
                .registerHandler("UpdateAll", new PacketInUpdateAll())
                .registerHandler("SyncControllerTime", new PacketInSyncControllerTime())
                .registerHandler("ProxyInfoUpdate", new PacketInProxyInfoUpdate())
                .registerHandler("UpdatePermissionCache", new PacketInUpdatePermissionCache())
                .registerHandler("ConnectPlayer", new PacketInConnectPlayer())
                .registerHandler("KickPlayer", new PacketInKickPlayer())
                .registerHandler("SendPlayerMessage", new PacketInSendPlayerMessage())
                .registerHandler("UpdateIngameCommands", new PacketInUpdateIngameCommands())
                .registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate());

        this.nettySocketClient = new NettySocketClient();
        this.nettySocketClient.connect(
                ethernetAddress, channelHandler, configuration.getBooleanValue("ssl"),
                configuration.getStringValue("controllerKey"), this.proxyStartupInfo.getName()
        );
    }

    @Override
    public void startGameServer(final String serverGroupName) {
        this.startGameServer(serverGroupName, new Configuration());
    }

    @Override
    public void startGameServer(final String serverGroupName, final Configuration preConfig) {
        final ServerGroup serverGroup = this.internalCloudNetwork.getServerGroups().getOrDefault(serverGroupName, null);
        this.startGameServer(serverGroup, preConfig);
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup) {
        this.startGameServer(serverGroup.getName(), new Configuration());
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartGameServer(serverGroup, preConfig));
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig, final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartGameServer(serverGroup, preConfig, template));
    }

    @Override
    public void startProxy(final String proxyGroupName) {
        this.startProxy(proxyGroupName, new Configuration());
    }

    @Override
    public void startProxy(final String proxyGroupName, final Configuration preConfig) {
        final ProxyGroup proxyGroup = this.internalCloudNetwork.getProxyGroups().getOrDefault(proxyGroupName, null);
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartProxy(proxyGroup, preConfig));
    }

    @Override
    public void startProxy(final ProxyGroup proxyGroup) {
        this.startProxy(proxyGroup.getName());
    }

    @Override
    public void startProxy(final ProxyGroup proxyGroup, final Configuration preConfig) {
        this.startProxy(proxyGroup.getName(), preConfig);
    }

    @Override
    public void startProxy(final ProxyGroup proxyGroup, final Configuration preConfig, final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutStartProxy(proxyGroup, preConfig, template));
    }

    @Override
    public OnlinePlayer getOnlinePlayer(UUID uniqueId) {
        PacketFuture packetFuture = this.createPacketFuture(
                new PacketOutQueryGetOnlinePlayer(uniqueId),
                "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null)
            return null;

        return result.getConfiguration().getValue("result", TypeTokenAdaptor.getONLINE_PLAYER_TYPE());
    }

    @Override
    public OnlinePlayer getOnlinePlayer(String name) {
        PacketFuture packetFuture = this.createPacketFuture(
                new PacketOutQueryGetOnlinePlayer(name),
                "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null)
            return null;

        return result.getConfiguration().getValue("result", TypeTokenAdaptor.getONLINE_PLAYER_TYPE());
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID uniqueId) {
        PacketFuture packetFuture = this.createPacketFuture(
                new PacketOutQueryGetPlayer(uniqueId),
                "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null)
            return null;

        return result.getConfiguration().getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        PacketFuture packetFuture = this.createPacketFuture(
                new PacketOutQueryGetPlayer(name),
                "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null)
            return null;

        return result.getConfiguration().getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
    }

    @Override
    public void updateOnlinePlayer(OnlinePlayer onlinePlayer) {
        this.channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutUpdateOnlinePlayer(onlinePlayer));
    }

    @Override
    public void updateOfflinePlayer(OfflinePlayer offlinePlayer) {
        this.channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutUpdateOfflinePlayer(offlinePlayer));
    }

    @Override
    public boolean isOnline(UUID uniqueId) {
        return this.getOnlinePlayer(uniqueId) != null;
    }

    @Override
    public boolean isOnline(String name) {
        return this.getOnlinePlayer(name) != null;
    }

    @Override
    public boolean isRegistered(UUID uniqueId) {
        return this.getOfflinePlayer(uniqueId) != null;
    }

    @Override
    public boolean isRegistered(String name) {
        return this.getOfflinePlayer(name) != null;
    }

    @Override
    public int getMaxPlayers() {
        return this.proxyInfo.getProxyGroup().getMaxPlayers();
    }

    @Override
    public int getOnlineCount() {
        return this.proxyInfo.getOnline();
    }

    @Override
    public List<Client> getAllClients() {
        return new ArrayList<>(this.internalCloudNetwork.getClients().values());
    }

    @Override
    public List<Client> getAllConnectedClients() {
        return this.internalCloudNetwork
                .getClients()
                .values()
                .stream()
                .filter(e -> e.getClientInfo() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServerGroup> getAllServerGroups() {
        return new ArrayList<>(this.internalCloudNetwork.getServerGroups().values());
    }

    @Override
    public List<ProxyGroup> getAllProxyGroups() {
        return new ArrayList<>(this.internalCloudNetwork.getProxyGroups().values());
    }

    @Override
    public List<ServerInfo> getAllRegisteredServers() {
        return this.internalCloudNetwork.getServerProcessManager().getAllRegisteredServerProcesses();
    }

    @Override
    public List<ProxyInfo> getAllRegisteredProxies() {
        return this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses();
    }

    @Override
    public List<ServerInfo> getAllRegisteredServers(String groupName) {
        return null;
    }

    @Override
    public List<ProxyInfo> getAllRegisteredProxies(String groupName) {
        return this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredProxyProcesses()
                .stream()
                .filter(e -> e.getProxyGroup().getName().equals(groupName))
                .collect(Collectors.toList());
    }

    @Override
    public boolean sendPacket(String subChannel, Packet packet) {
        return this.channelHandler.sendPacketAsynchronous(subChannel, packet);
    }

    @Override
    public boolean sendPacketSync(String subChannel, Packet packet) {
        return this.channelHandler.sendPacketSynchronized(subChannel, packet);
    }

    @Override
    public void sendPacketToAll(Packet packet) {
        this.channelHandler.sendToAllAsynchronous(packet);
    }

    @Override
    public void sendPacketToAllSync(Packet packet) {
        this.channelHandler.sendToAllSynchronized(packet);
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess) {
        this.channelHandler.sendPacketQuerySync(
                channel, this.proxyInfo.getCloudProcess().getName(), packet, onSuccess
        );
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess, NetworkQueryInboundHandler onFailure) {
        this.channelHandler.sendPacketQuerySync(
                channel, this.proxyInfo.getCloudProcess().getName(), packet, onSuccess, onFailure
        );
    }

    @Override
    public PacketFuture createPacketFuture(Packet packet, String networkComponent) {
        this.channelHandler.toQueryPacket(packet, UUID.randomUUID(), this.proxyInfo.getCloudProcess().getName());
        PacketFuture packetFuture = new PacketFuture(
                this.channelHandler,
                packet,
                this.channelHandler.getExecutorService(),
                networkComponent
        );
        this.channelHandler.getResults().put(packet.getResult(), packetFuture);

        return packetFuture;
    }

    @Override
    public PacketFuture sendPacketQuery(String channel, Packet packet) {
        return this.channelHandler.sendPacketQuerySync(
                channel, this.proxyInfo.getCloudProcess().getName(), packet
        );
    }

    @Override
    public Client getClient(String name) {
        return this.internalCloudNetwork.getClients().getOrDefault(name, null);
    }

    @Override
    public ClientInfo getConnectedClient(String name) {
        Client client = this.internalCloudNetwork
                .getClients()
                .values()
                .stream()
                .filter(e -> e.getName().equals(name) && e.getClientInfo() != null)
                .findFirst()
                .orElse(null);

        if (client == null)
            return null;

        return client.getClientInfo();
    }

    @Override
    public ServerInfo getServerInfo(UUID uniqueID) {
        return this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredServerProcesses()
                .stream()
                .filter(serverInfo1 -> serverInfo1.getCloudProcess().getProcessUID().equals(uniqueID))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ServerInfo getServerInfo(String name) {
        return this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredServerProcesses()
                .stream()
                .filter(serverInfo1 -> serverInfo1.getCloudProcess().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ProxyInfo getProxyInfo(UUID uniqueID) {
        return this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredProxyProcesses()
                .stream()
                .filter(proxyInfo -> proxyInfo.getCloudProcess().getProcessUID().equals(uniqueID))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ProxyInfo getProxyInfo(String name) {
        return this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredProxyProcesses()
                .stream()
                .filter(proxyInfo -> proxyInfo.getCloudProcess().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ServerGroup getServerGroup(String name) {
        return this.internalCloudNetwork.getServerGroups().getOrDefault(name, null);
    }

    @Override
    public ProxyGroup getProxyGroup(String name) {
        return this.internalCloudNetwork.getProxyGroups().getOrDefault(name, null);
    }

    public ServerInfo nextFreeLobby(final ProxyGroup proxyGroup, ProxiedPlayer proxiedPlayer) {
        for (ServerInfo serverInfo : this.internalCloudNetwork.getServerProcessManager().getAllRegisteredServerProcesses()) {
            if (serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC)
                    || serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.DYNAMIC)
                    || proxyGroup.getDisabledServerGroups().contains(serverInfo.getServerGroup().getName())) {
                continue;
            }

            if (serverInfo.getServerGroup().getJoin_permission() == null && serverInfo.getOnlinePlayers().size() < serverInfo.getServerGroup().getMaxPlayers())
                return serverInfo;
            else if (proxiedPlayer.hasPermission(serverInfo.getServerGroup().getJoin_permission()) && serverInfo.getOnlinePlayers().size() < serverInfo.getServerGroup().getMaxPlayers())
                return serverInfo;
        }

        return null;
    }

    public ServerInfo nextFreeLobby(final ProxyGroup proxyGroup, final ProxiedPlayer proxiedPlayer, final String current) {
        for (ServerInfo serverInfo : this.internalCloudNetwork.getServerProcessManager().getAllRegisteredServerProcesses()) {
            if (serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.STATIC)
                    || serverInfo.getServerGroup().getServerModeType().equals(ServerModeType.DYNAMIC)
                    || serverInfo.getCloudProcess().getName().equals(current)
                    || proxyGroup.getDisabledServerGroups().contains(serverInfo.getServerGroup().getName())) {
                continue;
            }

            if (serverInfo.getServerGroup().getJoin_permission() == null && serverInfo.getOnlinePlayers().size() < serverInfo.getServerGroup().getMaxPlayers())
                return serverInfo;
            else if (proxiedPlayer.hasPermission(serverInfo.getServerGroup().getJoin_permission()) && serverInfo.getOnlinePlayers().size() < serverInfo.getServerGroup().getMaxPlayers())
                return serverInfo;
        }

        return null;
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public void removeInternalProcess() {
        BungeecordBootstrap.getInstance().onDisable();
    }

    @Override
    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    public void updateIngameCommands(List<IngameCommand> newIngameCommands) {
        this.registeredIngameCommands.clear();
        this.setRegisteredIngameCommands(newIngameCommands);
    }

    public void executeIngameCommand(IngameCommand ingameCommand, UUID sender, String msg) {
        if (ingameCommand == null)
            return;

        ProxiedPlayer proxiedPlayer = BungeecordBootstrap.getInstance().getProxy().getPlayer(sender);
        if (proxiedPlayer == null)
            return;

        if (ingameCommand.getPermission() != null && !proxiedPlayer.hasPermission(ingameCommand.getPermission()))
            return;

        String string = msg.replace((msg.contains(" ") ? msg.split(" ")[0] + " " : msg), "");
        if (string.equalsIgnoreCase("")) {
            ingameCommand.handle(new IngameCommandSender() {
                @Override
                public String getDisplayName() {
                    return proxiedPlayer.getDisplayName();
                }

                @Override
                public void setDisplayName(String var1) {
                    proxiedPlayer.setDisplayName(var1);
                }

                @Override
                public void sendMessage(String message) {
                    proxiedPlayer.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
                }

                @Override
                public void connect(ServerInfo serverInfo) {
                    net.md_5.bungee.api.config.ServerInfo serverInfo1 =
                            BungeecordBootstrap.getInstance().getProxy().getServerInfo(serverInfo.getCloudProcess().getName());
                    if (serverInfo1 == null)
                        return;

                    proxiedPlayer.connect(serverInfo1);
                }

                @Override
                public ServerInfo getServer() {
                    return ReformCloudAPIBungee.this.getServerInfo(proxiedPlayer.getServer().getInfo().getName());
                }

                @Override
                public int getPing() {
                    return proxiedPlayer.getPing();
                }

                @Override
                public void sendData(String s, byte[] bytes) {
                    proxiedPlayer.sendData(s, bytes);
                }

                @Override
                public UUID getUniqueId() {
                    return proxiedPlayer.getUniqueId();
                }

                @Override
                public Locale getLocale() {
                    return proxiedPlayer.getLocale();
                }

                @Override
                public byte getViewDistance() {
                    return proxiedPlayer.getViewDistance();
                }

                @Override
                public ChatMode getChatMode() {
                    return Enums.getIfPresent(IngameCommandSender.ChatMode.class, proxiedPlayer.getChatMode().name()).or(ChatMode.SHOWN);
                }

                @Override
                public boolean hasChatColors() {
                    return proxiedPlayer.hasChatColors();
                }

                @Override
                public MainHand getMainHand() {
                    return Enums.getIfPresent(IngameCommandSender.MainHand.class, proxiedPlayer.getMainHand().name()).or(MainHand.RIGHT);
                }

                @Override
                public void resetTabHeader() {
                    proxiedPlayer.resetTabHeader();
                }

                @Override
                public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
                    BungeecordBootstrap.getInstance().getProxy().createTitle()
                            .title(TextComponent.fromLegacyText(title))
                            .subTitle(TextComponent.fromLegacyText(subTitle))
                            .fadeIn(fadeIn)
                            .fadeOut(fadeOut)
                            .stay(stay)
                            .send(proxiedPlayer);
                }

                @Override
                public void clearTitle() {
                    BungeecordBootstrap.getInstance().getProxy().createTitle().clear().send(proxiedPlayer);
                }

                @Override
                public boolean isForgeUser() {
                    return proxiedPlayer.isForgeUser();
                }

                @Override
                public Map<String, String> getModList() {
                    return proxiedPlayer.getModList();
                }
            }, new String[0]);
        } else {
            final String[] arguments = string.split(" ");
            ingameCommand.handle(new IngameCommandSender() {
                @Override
                public String getDisplayName() {
                    return proxiedPlayer.getDisplayName();
                }

                @Override
                public void setDisplayName(String var1) {
                    proxiedPlayer.setDisplayName(var1);
                }

                @Override
                public void sendMessage(String message) {
                    proxiedPlayer.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
                }

                @Override
                public void connect(ServerInfo serverInfo) {
                    net.md_5.bungee.api.config.ServerInfo serverInfo1 =
                            BungeecordBootstrap.getInstance().getProxy().getServerInfo(serverInfo.getCloudProcess().getName());
                    if (serverInfo1 == null)
                        return;

                    proxiedPlayer.connect(serverInfo1);
                }

                @Override
                public ServerInfo getServer() {
                    return ReformCloudAPIBungee.this.getServerInfo(proxiedPlayer.getServer().getInfo().getName());
                }

                @Override
                public int getPing() {
                    return proxiedPlayer.getPing();
                }

                @Override
                public void sendData(String s, byte[] bytes) {
                    proxiedPlayer.sendData(s, bytes);
                }

                @Override
                public UUID getUniqueId() {
                    return proxiedPlayer.getUniqueId();
                }

                @Override
                public Locale getLocale() {
                    return proxiedPlayer.getLocale();
                }

                @Override
                public byte getViewDistance() {
                    return proxiedPlayer.getViewDistance();
                }

                @Override
                public ChatMode getChatMode() {
                    return Enums.getIfPresent(IngameCommandSender.ChatMode.class, proxiedPlayer.getChatMode().name()).or(ChatMode.SHOWN);
                }

                @Override
                public boolean hasChatColors() {
                    return proxiedPlayer.hasChatColors();
                }

                @Override
                public MainHand getMainHand() {
                    return Enums.getIfPresent(IngameCommandSender.MainHand.class, proxiedPlayer.getMainHand().name()).or(MainHand.RIGHT);
                }

                @Override
                public void resetTabHeader() {
                    proxiedPlayer.resetTabHeader();
                }

                @Override
                public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
                    BungeecordBootstrap.getInstance().getProxy().createTitle()
                            .title(TextComponent.fromLegacyText(title))
                            .subTitle(TextComponent.fromLegacyText(subTitle))
                            .fadeIn(fadeIn)
                            .fadeOut(fadeOut)
                            .stay(stay)
                            .send(proxiedPlayer);
                }

                @Override
                public void clearTitle() {
                    BungeecordBootstrap.getInstance().getProxy().createTitle().clear().send(proxiedPlayer);
                }

                @Override
                public boolean isForgeUser() {
                    return proxiedPlayer.isForgeUser();
                }

                @Override
                public Map<String, String> getModList() {
                    return proxiedPlayer.getModList();
                }
            }, arguments);
        }
    }

    public IngameCommand getIngameCommand(String msg) {
        String[] strings = msg.split(" ");
        if (strings.length <= 0)
            return null;

        return this.registeredIngameCommands.stream().filter(e -> e.getName().equalsIgnoreCase(strings[0])).findFirst().orElse(null);
    }
}
