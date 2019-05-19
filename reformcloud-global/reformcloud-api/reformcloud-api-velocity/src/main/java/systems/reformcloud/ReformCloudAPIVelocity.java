/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import com.google.common.base.Enums;
import com.google.gson.reflect.TypeToken;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.text.TextComponent;
import systems.reformcloud.api.*;
import systems.reformcloud.api.save.ISaveAPIService;
import systems.reformcloud.bootstrap.VelocityBootstrap;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.commands.ingame.sender.IngameCommandSender;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.auto.start.AutoStart;
import systems.reformcloud.meta.auto.stop.AutoStop;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.dev.DevProcess;
import systems.reformcloud.meta.enums.ProxyModeType;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.startup.ProxyStartupInfo;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketClient;
import systems.reformcloud.network.api.event.NetworkEventAdapter;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.in.*;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.packets.*;
import systems.reformcloud.network.query.out.PacketOutQueryGetOnlinePlayer;
import systems.reformcloud.network.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.network.query.out.PacketOutQueryStartQueuedProcess;
import systems.reformcloud.permissions.VelocityPermissionProvider;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.defaults.DefaultCloudService;

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 24.03.2019
 */

public final class ReformCloudAPIVelocity implements Serializable, IAPIService {
    public static ReformCloudAPIVelocity instance;

    private final NettySocketClient nettySocketClient;
    private final ChannelHandler channelHandler;

    private ProxySettings proxySettings;

    private final VelocityPermissionProvider velocityPermissionProvider = new VelocityPermissionProvider();

    private final ProxyStartupInfo proxyStartupInfo;
    private ProxyInfo proxyInfo;
    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private PermissionCache permissionCache;

    private Map<UUID, OnlinePlayer> onlinePlayers = new HashMap<>();
    private Map<UUID, PermissionHolder> cachedPermissionHolders = new HashMap<>();
    private List<IngameCommand> registeredIngameCommands = new ArrayList<>();

    private long internalTime = System.currentTimeMillis();

    /**
     * Creates a new BungeeCord Plugin instance
     *
     * @throws Throwable
     */
    public ReformCloudAPIVelocity() throws Throwable {
        if (instance == null)
            instance = this;
        else
            throw new InstanceAlreadyExistsException();

        ReformCloudLibraryService.sendHeader();

        ISaveAPIService.instance.set(new SaveAPIImpl());
        IAPIService.instance.set(this);
        new DefaultCloudService(this);
        IDefaultPlayerProvider.instance.set(new PlayerProvider());

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/config.json"));

        final EthernetAddress ethernetAddress = configuration.getValue("address", TypeTokenAdaptor.getETHERNET_ADDRESS_TYPE());
        new ReformCloudLibraryServiceProvider(new LoggerProvider(), configuration.getStringValue("controllerKey"), ethernetAddress.getHost(), new EventManager(), null);
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().setDebug(configuration.getBooleanValue("debug"));

        this.channelHandler = new ChannelHandler();

        this.proxyStartupInfo = configuration.getValue("startupInfo", TypeTokenAdaptor.getPROXY_STARTUP_INFO_TYPE());
        this.proxyInfo = configuration.getValue("info", TypeTokenAdaptor.getPROXY_INFO_TYPE());

        IEventHandler.instance.set(new NetworkEventAdapter());

        this.getNettyHandler().registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal())
                .registerHandler("ProcessAdd", new PacketInProcessAdd())
                .registerHandler("ProcessRemove", new PacketInProcessRemove())
                .registerHandler("UpdateAll", new PacketInUpdateAll())
                .registerHandler("SyncControllerTime", new PacketInSyncControllerTime())
                .registerHandler("UpdateProxyConfig", new PacketInUpdateProxySettings())
                .registerHandler("ProxyInfoUpdate", new PacketInProxyInfoUpdate())
                .registerHandler("UpdatePermissionCache", new PacketInUpdatePermissionCache())
                .registerHandler("ConnectPlayer", new PacketInConnectPlayer())
                .registerHandler("KickPlayer", new PacketInKickPlayer())
                .registerHandler("SendPlayerMessage", new PacketInSendPlayerMessage())
                .registerHandler("DisableIcons", new PacketInDisableIcons())
                .registerHandler("EnableIcons", new PacketInEnableIcons())
                .registerHandler("EnableDebug", new PacketInEnableDebug())
                .registerHandler("UpdateIngameCommands", new PacketInUpdateIngameCommands())
                .registerHandler("UpdatePermissionHolder", new PacketInUpdatePermissionHolder())
                .registerHandler("UpdatePermissionGroup", new PacketInUpdatePermissionGroup())
                .registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate());

        this.nettySocketClient = new NettySocketClient();
        this.nettySocketClient.connect(
                ethernetAddress, channelHandler, configuration.getBooleanValue("ssl"),
                configuration.getStringValue("controllerKey"), this.proxyStartupInfo.getName()
        );

        if (this.proxyInfo.getProxyGroup().getAutoStop().isEnabled()) {
            ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
                ReformCloudLibraryService.sleep(TimeUnit.SECONDS, this.proxyInfo.getProxyGroup().getAutoStop().getCheckEverySeconds());
                if (VelocityBootstrap.getInstance().getProxy().getPlayerCount() == 0) {
                    if (this.getAllRegisteredProxies(proxyInfo.getCloudProcess().getGroup()).size() > proxyInfo.getProxyGroup().getMinOnline())
                        this.stopProxy(this.proxyInfo);
                }
            });
        }
    }

    public static ReformCloudAPIVelocity getInstance() {
        return ReformCloudAPIVelocity.instance;
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
    public boolean stopProxy(String name) {
        return channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutStopProcess(name));
    }

    @Override
    public boolean stopProxy(ProxyInfo proxyInfo) {
        return stopProxy(proxyInfo.getCloudProcess().getName());
    }

    @Override
    public boolean stopServer(String name) {
        return channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutStopProcess(name));
    }

    @Override
    public boolean stopServer(ServerInfo serverInfo) {
        return stopServer(serverInfo.getCloudProcess().getName());
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, SpigotVersions spigotVersions) {
        ServerGroup serverGroup = new ServerGroup(
                name,
                "ReformCloud",
                null,
                new ArrayList<>(clients),
                Arrays.asList(new Template("default", null, TemplateBackend.CLIENT)),
                512,
                1,
                -1,
                50,
                41000,
                true,
                false,
                new AutoStart(true, 45, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                serverModeType,
                spigotVersions
        );
        createServerGroup(serverGroup);
    }

    @Override
    public void createServerGroup(ServerGroup serverGroup) {
        if (this.internalCloudNetwork.getServerGroups().get(serverGroup.getName()) != null)
            return;

        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutCreateServerGroup(serverGroup));
    }

    @Override
    public void createServerGroup(String name) {
        createServerGroup(name, ServerModeType.DYNAMIC, Arrays.asList("Client-01"), SpigotVersions.SPIGOT_1_8_8);
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType, Collection<Template> templates) {
        ServerGroup serverGroup = new ServerGroup(
                name,
                "ReformCloud",
                null,
                new ArrayList<>(this.internalCloudNetwork.getClients().keySet()),
                new ArrayList<>(templates),
                512,
                1,
                -1,
                50,
                41000,
                true,
                false,
                new AutoStart(true, 45, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                serverModeType,
                SpigotVersions.SPIGOT_1_8_8
        );
        createServerGroup(serverGroup);
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType, Collection<String> clients, Collection<Template> templates, SpigotVersions spigotVersions) {
        ServerGroup serverGroup = new ServerGroup(
                name,
                "ReformCloud",
                null,
                new ArrayList<>(clients),
                new ArrayList<>(templates),
                512,
                1,
                -1,
                50,
                41000,
                true,
                false,
                new AutoStart(true, 45, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                serverModeType,
                spigotVersions
        );
        createServerGroup(serverGroup);
    }

    @Override
    public void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, ProxyVersions proxyVersions) {
        ProxyGroup proxyGroup = new ProxyGroup(
                name,
                new ArrayList<>(clients),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                proxyModeType,
                new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                false,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                proxyVersions
        );
        createProxyGroup(proxyGroup);
    }

    @Override
    public void createProxyGroup(ProxyGroup proxyGroup) {
        if (this.internalCloudNetwork.getProxyGroups().get(proxyGroup.getName()) != null)
            return;

        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutCreateProxyGroup(proxyGroup));
    }

    @Override
    public void createProxyGroup(String name) {
        ProxyGroup proxyGroup = new ProxyGroup(
                name,
                new ArrayList<>(this.internalCloudNetwork.getClients().keySet()),
                new ArrayList<>(),
                Arrays.asList(new Template("default", null, TemplateBackend.CLIENT)),
                new ArrayList<>(),
                ProxyModeType.DYNAMIC,
                new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                false,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                ProxyVersions.BUNGEECORD
        );
        createProxyGroup(proxyGroup);
    }

    @Override
    public void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<Template> templates) {
        ProxyGroup proxyGroup = new ProxyGroup(
                name,
                new ArrayList<>(this.internalCloudNetwork.getClients().keySet()),
                new ArrayList<>(),
                new ArrayList<>(templates),
                new ArrayList<>(),
                proxyModeType,
                new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                false,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                ProxyVersions.BUNGEECORD
        );
        createProxyGroup(proxyGroup);
    }

    @Override
    public void createProxyGroup(String name, ProxyModeType proxyModeType, Collection<String> clients, Collection<Template> templates, ProxyVersions proxyVersions) {
        ProxyGroup proxyGroup = new ProxyGroup(
                name,
                new ArrayList<>(clients),
                new ArrayList<>(),
                new ArrayList<>(templates),
                new ArrayList<>(),
                proxyModeType,
                new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                false,
                true,
                false,
                25565,
                1,
                -1,
                512,
                128,
                proxyVersions
        );
        createProxyGroup(proxyGroup);
    }

    @Override
    public void createClient(String name, String host) {
        Client client = new Client(
                name,
                host,
                null
        );
        createClient(client);
    }

    @Override
    public void createClient(String name) {
        Client client = new Client(
                name,
                "127.0.0.1",
                null
        );
        createClient(client);
    }

    @Override
    public void createClient(Client client) {
        if (this.internalCloudNetwork.getClients().get(client.getName()) != null)
            return;

        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutCreateClient(client));
    }

    @Override
    public void updateServerInfo(ServerInfo serverInfo) {
        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutUpdateServerInfo(serverInfo));
    }

    @Override
    public void updateProxyInfo(ProxyInfo proxyInfo) {
        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutUpdateProxyInfo(proxyInfo));
    }

    @Override
    public void updateServerGroup(ServerGroup serverGroup) {
        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutUpdateServerGroup(serverGroup));
    }

    @Override
    public void updateProxyGroup(ProxyGroup proxyGroup) {
        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutUpdateProxyGroup(proxyGroup));
    }

    @Override
    public void createWebUser(String name) {
        String password = ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE)
                + StringUtil.EMPTY
                + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE);

        WebUser webUser = new WebUser(name, StringEncrypt.encryptSHA512(password), new HashMap<>());
        createWebUser(webUser);
    }

    @Override
    public void createWebUser(String name, String password) {
        WebUser webUser = new WebUser(name, StringEncrypt.encryptSHA512(password), new HashMap<>());
        createWebUser(webUser);
    }

    @Override
    public void createWebUser(String name, String password, Map<String, Boolean> permissions) {
        WebUser webUser = new WebUser(name, StringEncrypt.encryptSHA512(password), permissions);
        createWebUser(webUser);
    }

    @Override
    public void createWebUser(WebUser webUser) {
        channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutCreateWebUser(webUser));
    }

    @Override
    public void dispatchConsoleCommand(String commandLine) {
        this.channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutDispatchConsoleCommand(commandLine));
    }

    @Override
    public void dispatchConsoleCommand(CharSequence commandLine) {
        this.dispatchConsoleCommand(String.valueOf(commandLine));
    }

    @Override
    public String dispatchConsoleCommandAndGetResult(String commandLine) {
        return this.createPacketFuture(
                new PacketOutExecuteCommandSilent(commandLine), "ReformCloudController"
        ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getStringValue("result");
    }

    @Override
    public String dispatchConsoleCommandAndGetResult(CharSequence commandLine) {
        return this.dispatchConsoleCommandAndGetResult(String.valueOf(commandLine));
    }
    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup) {
        return this.createPacketFuture(
                new PacketOutQueryStartQueuedProcess(serverGroup, "default", new Configuration()),
                "ReformCloudController"
        ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getValue("result", new TypeToken<DevProcess>() {
        });
    }

    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup, String template) {
        return this.createPacketFuture(
                new PacketOutQueryStartQueuedProcess(serverGroup, template, new Configuration()),
                "ReformCloudController"
        ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getValue("result", new TypeToken<DevProcess>() {
        });
    }

    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup, String template, Configuration preConfig) {
        return this.createPacketFuture(
                new PacketOutQueryStartQueuedProcess(serverGroup, template, preConfig),
                "ReformCloudController"
        ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getValue("result", new TypeToken<DevProcess>() {
        });
    }

    @Override
    public OnlinePlayer getOnlinePlayer(UUID uniqueId) {
        if (this.onlinePlayers.containsKey(uniqueId))
            return this.onlinePlayers.get(uniqueId);

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
    public int getGlobalOnlineCount() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.getAllRegisteredProxies().forEach(proxyInfo1 -> atomicInteger.addAndGet(proxyInfo1.getOnline()));

        return atomicInteger.get();
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
        return new ArrayList<>(this.internalCloudNetwork.getServerProcessManager().getAllRegisteredServerGroupProcesses(groupName));
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

    public ServerInfo nextFreeLobby(final ProxyGroup proxyGroup, Player proxiedPlayer) {
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

    public ServerInfo nextFreeLobby(final ProxyGroup proxyGroup, final Player proxiedPlayer, final String current) {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    @Override
    public Optional<ISaveAPIService> getAPISave() {
        return Optional.ofNullable(ISaveAPIService.instance.get());
    }

    public void updateIngameCommands(List<IngameCommand> newIngameCommands) {
        this.registeredIngameCommands.clear();
        this.setRegisteredIngameCommands(newIngameCommands);
    }

    public void executeIngameCommand(IngameCommand ingameCommand, UUID sender, String msg) {
        if (ingameCommand == null)
            return;

        Player proxiedPlayer = VelocityBootstrap.getInstance().getProxyServer().getPlayer(sender).orElse(null);
        if (proxiedPlayer == null)
            return;

        if (ingameCommand.getPermission() != null && !proxiedPlayer.hasPermission(ingameCommand.getPermission()))
            return;

        String string = msg.replace((msg.contains(" ") ? msg.split(" ")[0] + " " : msg), "");
        if (string.equalsIgnoreCase("")) {
            ingameCommand.handle(new IngameCommandSender() {
                @Override
                public String getDisplayName() {
                    return proxiedPlayer.getUsername();
                }

                @Override
                public void setDisplayName(String var1) {
                }

                @Override
                public void sendMessage(String message) {
                    proxiedPlayer.sendMessage(TextComponent.of(message));
                }

                @Override
                public void connect(ServerInfo serverInfo) {
                    RegisteredServer serverInfo1 =
                            VelocityBootstrap.getInstance().getProxyServer().getServer(serverInfo.getCloudProcess().getName()).orElse(null);
                    if (serverInfo1 == null)
                        return;

                    proxiedPlayer.createConnectionRequest(serverInfo1).connect();
                }

                @Override
                public ServerInfo getServer() {
                    return ReformCloudAPIVelocity.this.getServerInfo(proxiedPlayer.getCurrentServer().get().getServerInfo().getName());
                }

                @Override
                public int getPing() {
                    return (int) proxiedPlayer.getPing();
                }

                @Override
                public void sendData(String s, byte[] bytes) {
                    proxiedPlayer.sendResourcePack(s, bytes);
                }

                @Override
                public UUID getUniqueId() {
                    return proxiedPlayer.getUniqueId();
                }

                @Override
                public Locale getLocale() {
                    return proxiedPlayer.getPlayerSettings().getLocale();
                }

                @Override
                public byte getViewDistance() {
                    return proxiedPlayer.getPlayerSettings().getViewDistance();
                }

                @Override
                public ChatMode getChatMode() {
                    return Enums.getIfPresent(IngameCommandSender.ChatMode.class, proxiedPlayer.getPlayerSettings().getChatMode().name()).or(ChatMode.SHOWN);
                }

                @Override
                public boolean hasChatColors() {
                    return proxiedPlayer.getPlayerSettings().hasChatColors();
                }

                @Override
                public MainHand getMainHand() {
                    return Enums.getIfPresent(IngameCommandSender.MainHand.class, proxiedPlayer.getPlayerSettings().getMainHand().name()).or(MainHand.RIGHT);
                }

                @Override
                public void resetTabHeader() {
                }

                @Override
                public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
                }

                @Override
                public void clearTitle() {
                }

                @Override
                public boolean isForgeUser() {
                    return proxiedPlayer.getModInfo().orElse(null) != null;
                }

                @Override
                public Map<String, String> getModList() {
                    return null;
                }
            }, new String[0]);
        } else {
            final String[] arguments = string.split(" ");
            ingameCommand.handle(new IngameCommandSender() {
                @Override
                public String getDisplayName() {
                    return proxiedPlayer.getUsername();
                }

                @Override
                public void setDisplayName(String var1) {
                }

                @Override
                public void sendMessage(String message) {
                    proxiedPlayer.sendMessage(TextComponent.of(message));
                }

                @Override
                public void connect(ServerInfo serverInfo) {
                    RegisteredServer serverInfo1 =
                            VelocityBootstrap.getInstance().getProxyServer().getServer(serverInfo.getCloudProcess().getName()).orElse(null);
                    if (serverInfo1 == null)
                        return;

                    proxiedPlayer.createConnectionRequest(serverInfo1).connect();
                }

                @Override
                public ServerInfo getServer() {
                    return ReformCloudAPIVelocity.this.getServerInfo(proxiedPlayer.getCurrentServer().get().getServerInfo().getName());
                }

                @Override
                public int getPing() {
                    return (int) proxiedPlayer.getPing();
                }

                @Override
                public void sendData(String s, byte[] bytes) {
                    proxiedPlayer.sendResourcePack(s, bytes);
                }

                @Override
                public UUID getUniqueId() {
                    return proxiedPlayer.getUniqueId();
                }

                @Override
                public Locale getLocale() {
                    return proxiedPlayer.getPlayerSettings().getLocale();
                }

                @Override
                public byte getViewDistance() {
                    return proxiedPlayer.getPlayerSettings().getViewDistance();
                }

                @Override
                public ChatMode getChatMode() {
                    return Enums.getIfPresent(IngameCommandSender.ChatMode.class, proxiedPlayer.getPlayerSettings().getChatMode().name()).or(ChatMode.SHOWN);
                }

                @Override
                public boolean hasChatColors() {
                    return proxiedPlayer.getPlayerSettings().hasChatColors();
                }

                @Override
                public MainHand getMainHand() {
                    return Enums.getIfPresent(IngameCommandSender.MainHand.class, proxiedPlayer.getPlayerSettings().getMainHand().name()).or(MainHand.RIGHT);
                }

                @Override
                public void resetTabHeader() {
                }

                @Override
                public void sendTitle(String title, String subTitle, int fadeIn, int fadeOut, int stay) {
                }

                @Override
                public void clearTitle() {
                }

                @Override
                public boolean isForgeUser() {
                    return proxiedPlayer.getModInfo().orElse(null) != null;
                }

                @Override
                public Map<String, String> getModList() {
                    return null;
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

    public int getGlobalMaxOnlineCount() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.getAllProxyGroups().forEach(e -> atomicInteger.addAndGet(e.getMaxPlayers()));
        return atomicInteger.get();
    }

    public NettySocketClient getNettySocketClient() {
        return this.nettySocketClient;
    }

    public ChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    public ProxySettings getProxySettings() {
        return this.proxySettings;
    }

    public ProxyStartupInfo getProxyStartupInfo() {
        return this.proxyStartupInfo;
    }

    public ProxyInfo getProxyInfo() {
        return this.proxyInfo;
    }

    public InternalCloudNetwork getInternalCloudNetwork() {
        return this.internalCloudNetwork;
    }

    public PermissionCache getPermissionCache() {
        return this.permissionCache;
    }

    public Map<UUID, OnlinePlayer> getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public Map<UUID, PermissionHolder> getCachedPermissionHolders() {
        return this.cachedPermissionHolders;
    }

    public List<IngameCommand> getRegisteredIngameCommands() {
        return this.registeredIngameCommands;
    }

    public long getInternalTime() {
        return this.internalTime;
    }

    public void setProxySettings(ProxySettings proxySettings) {
        this.proxySettings = proxySettings;
    }

    public void setProxyInfo(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    public void setInternalCloudNetwork(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public VelocityPermissionProvider getVelocityPermissionProvider() {
        return velocityPermissionProvider;
    }

    public void setPermissionCache(PermissionCache permissionCache) {
        this.permissionCache = permissionCache;
    }

    public void setOnlinePlayers(Map<UUID, OnlinePlayer> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public void setCachedPermissionHolders(Map<UUID, PermissionHolder> cachedPermissionHolders) {
        this.cachedPermissionHolders = cachedPermissionHolders;
    }

    public void setRegisteredIngameCommands(List<IngameCommand> registeredIngameCommands) {
        this.registeredIngameCommands = registeredIngameCommands;
    }

    public void setInternalTime(long internalTime) {
        this.internalTime = internalTime;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReformCloudAPIVelocity)) return false;
        final ReformCloudAPIVelocity other = (ReformCloudAPIVelocity) o;
        final Object this$nettySocketClient = this.getNettySocketClient();
        final Object other$nettySocketClient = other.getNettySocketClient();
        if (this$nettySocketClient == null ? other$nettySocketClient != null : !this$nettySocketClient.equals(other$nettySocketClient))
            return false;
        final Object this$channelHandler = this.getChannelHandler();
        final Object other$channelHandler = other.getChannelHandler();
        if (this$channelHandler == null ? other$channelHandler != null : !this$channelHandler.equals(other$channelHandler))
            return false;
        final Object this$proxySettings = this.getProxySettings();
        final Object other$proxySettings = other.getProxySettings();
        if (this$proxySettings == null ? other$proxySettings != null : !this$proxySettings.equals(other$proxySettings))
            return false;
        final Object this$proxyStartupInfo = this.getProxyStartupInfo();
        final Object other$proxyStartupInfo = other.getProxyStartupInfo();
        if (this$proxyStartupInfo == null ? other$proxyStartupInfo != null : !this$proxyStartupInfo.equals(other$proxyStartupInfo))
            return false;
        final Object this$proxyInfo = this.getProxyInfo();
        final Object other$proxyInfo = other.getProxyInfo();
        if (this$proxyInfo == null ? other$proxyInfo != null : !this$proxyInfo.equals(other$proxyInfo)) return false;
        final Object this$internalCloudNetwork = this.getInternalCloudNetwork();
        final Object other$internalCloudNetwork = other.getInternalCloudNetwork();
        if (this$internalCloudNetwork == null ? other$internalCloudNetwork != null : !this$internalCloudNetwork.equals(other$internalCloudNetwork))
            return false;
        final Object this$permissionCache = this.getPermissionCache();
        final Object other$permissionCache = other.getPermissionCache();
        if (this$permissionCache == null ? other$permissionCache != null : !this$permissionCache.equals(other$permissionCache))
            return false;
        final Object this$onlinePlayers = this.getOnlinePlayers();
        final Object other$onlinePlayers = other.getOnlinePlayers();
        if (this$onlinePlayers == null ? other$onlinePlayers != null : !this$onlinePlayers.equals(other$onlinePlayers))
            return false;
        final Object this$cachedPermissionHolders = this.getCachedPermissionHolders();
        final Object other$cachedPermissionHolders = other.getCachedPermissionHolders();
        if (this$cachedPermissionHolders == null ? other$cachedPermissionHolders != null : !this$cachedPermissionHolders.equals(other$cachedPermissionHolders))
            return false;
        final Object this$registeredIngameCommands = this.getRegisteredIngameCommands();
        final Object other$registeredIngameCommands = other.getRegisteredIngameCommands();
        if (this$registeredIngameCommands == null ? other$registeredIngameCommands != null : !this$registeredIngameCommands.equals(other$registeredIngameCommands))
            return false;
        if (this.getInternalTime() != other.getInternalTime()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $nettySocketClient = this.getNettySocketClient();
        result = result * PRIME + ($nettySocketClient == null ? 43 : $nettySocketClient.hashCode());
        final Object $channelHandler = this.getChannelHandler();
        result = result * PRIME + ($channelHandler == null ? 43 : $channelHandler.hashCode());
        final Object $proxySettings = this.getProxySettings();
        result = result * PRIME + ($proxySettings == null ? 43 : $proxySettings.hashCode());
        final Object $proxyStartupInfo = this.getProxyStartupInfo();
        result = result * PRIME + ($proxyStartupInfo == null ? 43 : $proxyStartupInfo.hashCode());
        final Object $proxyInfo = this.getProxyInfo();
        result = result * PRIME + ($proxyInfo == null ? 43 : $proxyInfo.hashCode());
        final Object $internalCloudNetwork = this.getInternalCloudNetwork();
        result = result * PRIME + ($internalCloudNetwork == null ? 43 : $internalCloudNetwork.hashCode());
        final Object $permissionCache = this.getPermissionCache();
        result = result * PRIME + ($permissionCache == null ? 43 : $permissionCache.hashCode());
        final Object $onlinePlayers = this.getOnlinePlayers();
        result = result * PRIME + ($onlinePlayers == null ? 43 : $onlinePlayers.hashCode());
        final Object $cachedPermissionHolders = this.getCachedPermissionHolders();
        result = result * PRIME + ($cachedPermissionHolders == null ? 43 : $cachedPermissionHolders.hashCode());
        final Object $registeredIngameCommands = this.getRegisteredIngameCommands();
        result = result * PRIME + ($registeredIngameCommands == null ? 43 : $registeredIngameCommands.hashCode());
        final long $internalTime = this.getInternalTime();
        result = result * PRIME + (int) ($internalTime >>> 32 ^ $internalTime);
        return result;
    }

    public String toString() {
        return "ReformCloudAPIVelocity(nettySocketClient=" + this.getNettySocketClient() + ", channelHandler=" + this.getChannelHandler() + ", proxySettings=" + this.getProxySettings() + ", proxyStartupInfo=" + this.getProxyStartupInfo() + ", proxyInfo=" + this.getProxyInfo() + ", internalCloudNetwork=" + this.getInternalCloudNetwork() + ", permissionCache=" + this.getPermissionCache() + ", onlinePlayers=" + this.getOnlinePlayers() + ", cachedPermissionHolders=" + this.getCachedPermissionHolders() + ", registeredIngameCommands=" + this.getRegisteredIngameCommands() + ", internalTime=" + this.getInternalTime() + ")";
    }
}
