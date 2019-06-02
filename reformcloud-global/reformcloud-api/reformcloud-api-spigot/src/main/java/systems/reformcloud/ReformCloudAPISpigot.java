/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import systems.reformcloud.api.IAPIService;
import systems.reformcloud.api.IDefaultPlayerProvider;
import systems.reformcloud.api.IEventHandler;
import systems.reformcloud.api.PlayerProvider;
import systems.reformcloud.api.SaveAPImpl;
import systems.reformcloud.api.save.ISaveAPIService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.launcher.SpigotBootstrap;
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
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.stats.TempServerStats;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.startup.ServerStartupInfo;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketClient;
import systems.reformcloud.network.api.event.NetworkEventHandler;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.in.PacketInCreateSign;
import systems.reformcloud.network.in.PacketInEnableDebug;
import systems.reformcloud.network.in.PacketInEnableMobs;
import systems.reformcloud.network.in.PacketInInitializeInternal;
import systems.reformcloud.network.in.PacketInProcessAdd;
import systems.reformcloud.network.in.PacketInProcessRemove;
import systems.reformcloud.network.in.PacketInProxyInfoUpdate;
import systems.reformcloud.network.in.PacketInRemoveSign;
import systems.reformcloud.network.in.PacketInServerInfoUpdate;
import systems.reformcloud.network.in.PacketInSignUpdate;
import systems.reformcloud.network.in.PacketInSyncControllerTime;
import systems.reformcloud.network.in.PacketInUpdateAll;
import systems.reformcloud.network.in.PacketInUpdatePermissionCache;
import systems.reformcloud.network.in.PacketInUpdatePermissionGroup;
import systems.reformcloud.network.in.PacketInUpdatePermissionHolder;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.packets.PacketOutCreateClient;
import systems.reformcloud.network.packets.PacketOutCreateProxyGroup;
import systems.reformcloud.network.packets.PacketOutCreateServerGroup;
import systems.reformcloud.network.packets.PacketOutCreateWebUser;
import systems.reformcloud.network.packets.PacketOutDispatchConsoleCommand;
import systems.reformcloud.network.packets.PacketOutExecuteCommandSilent;
import systems.reformcloud.network.packets.PacketOutServerInfoUpdate;
import systems.reformcloud.network.packets.PacketOutStartGameServer;
import systems.reformcloud.network.packets.PacketOutStartProxy;
import systems.reformcloud.network.packets.PacketOutStopProcess;
import systems.reformcloud.network.packets.PacketOutUpdateOfflinePlayer;
import systems.reformcloud.network.packets.PacketOutUpdateOnlinePlayer;
import systems.reformcloud.network.packets.PacketOutUpdateProxyGroup;
import systems.reformcloud.network.packets.PacketOutUpdateProxyInfo;
import systems.reformcloud.network.packets.PacketOutUpdateServerGroup;
import systems.reformcloud.network.packets.PacketOutUpdateServerInfo;
import systems.reformcloud.network.packets.PacketOutUpdateServerTempStats;
import systems.reformcloud.network.query.out.PacketOutQueryGetOnlinePlayer;
import systems.reformcloud.network.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.network.query.out.PacketOutQueryStartQueuedProcess;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.defaults.DefaultCloudService;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class ReformCloudAPISpigot implements Listener, IAPIService, Serializable {

    private static ReformCloudAPISpigot instance;

    private final NettySocketClient nettySocketClient;
    private final ChannelHandler channelHandler;

    private final ServerStartupInfo serverStartupInfo;
    private ServerInfo serverInfo;
    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private final TempServerStats tempServerStats = new TempServerStats();

    private PermissionCache permissionCache;

    private Map<UUID, PermissionHolder> cachedPermissionHolders = new HashMap<>();

    private long internalTime = System.currentTimeMillis();

    /**
     * Creates a new ReformCloud Spigot instance
     */
    public ReformCloudAPISpigot() throws Throwable {
        if (instance == null) {
            instance = this;
        } else {
            throw new InstanceAlreadyExistsException();
        }

        ReformCloudLibraryService.sendHeader();

        ISaveAPIService.instance.set(new SaveAPImpl());
        IAPIService.instance.set(this);
        new DefaultCloudService(this);
        IDefaultPlayerProvider.instance.set(new PlayerProvider());

        SpigotBootstrap.getInstance().getServer().getPluginManager()
            .registerEvents(this, SpigotBootstrap.getInstance());

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/config.json"));

        final EthernetAddress ethernetAddress = configuration
            .getValue("address", TypeTokenAdaptor.getETHERNET_ADDRESS_TYPE());
        new ReformCloudLibraryServiceProvider(new LoggerProvider(),
            configuration.getStringValue("controllerKey"), ethernetAddress.getHost(),
            new EventManager(), null);
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider()
            .setDebug(configuration.getBooleanValue("debug"));

        this.channelHandler = new ChannelHandler();

        this.serverStartupInfo = configuration
            .getValue("startupInfo", TypeTokenAdaptor.getSERVER_STARTUP_INFO_TYPE());
        this.serverInfo = configuration.getValue("info", TypeTokenAdaptor.getSERVER_INFO_TYPE());

        IEventHandler.instance.set(new NetworkEventHandler());

        this.getNettyHandler()
            .registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal())
            .registerHandler("UpdateAll", new PacketInUpdateAll())
            .registerHandler("ProcessAdd", new PacketInProcessAdd())
            .registerHandler("ProcessRemove", new PacketInProcessRemove())
            .registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate())
            .registerHandler("EnableDebug", new PacketInEnableDebug())
            .registerHandler("ProxyInfoUpdate", new PacketInProxyInfoUpdate())
            .registerHandler("SignUpdate", new PacketInSignUpdate())
            .registerHandler("EnableMobs", new PacketInEnableMobs())
            .registerHandler("RemoveSign", new PacketInRemoveSign())
            .registerHandler("CreateSign", new PacketInCreateSign())
            .registerHandler("SyncControllerTime", new PacketInSyncControllerTime())
            .registerHandler("UpdatePermissionHolder", new PacketInUpdatePermissionHolder())
            .registerHandler("UpdatePermissionGroup", new PacketInUpdatePermissionGroup())
            .registerHandler("UpdatePermissionCache", new PacketInUpdatePermissionCache());

        this.nettySocketClient = new NettySocketClient();
        this.nettySocketClient.connect(
            ethernetAddress, channelHandler, configuration.getBooleanValue("ssl"),
            configuration.getStringValue("controllerKey"), this.serverStartupInfo.getName()
        );

        if (this.serverInfo.getServerGroup().getAutoStop().isEnabled()) {
            ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
                ReformCloudLibraryService.sleep(TimeUnit.SECONDS,
                    this.serverInfo.getServerGroup().getAutoStop().getCheckEverySeconds());
                if (SpigotBootstrap.getInstance().getServer().getOnlinePlayers().size() == 0) {
                    if (this.getAllRegisteredServers(serverInfo.getCloudProcess().getGroup()).size()
                        > serverInfo.getServerGroup().getMinOnline()) {
                        this.stopServer(this.serverInfo);
                    }
                }
            });
        }
    }

    public void updateTempStats() {
        if (this.tempServerStats.hasChanges()) {
            this.channelHandler.sendDirectPacket("ReformCloudController",
                new PacketOutUpdateServerTempStats(this.tempServerStats));
            this.tempServerStats.reset();
        }
    }

    public void setCloudServerMOTDandSendUpdatePacket(final String newMOTD) {
        this.serverInfo.setMotd(newMOTD);
        this.channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutServerInfoUpdate(serverInfo));
    }

    public void update() {
        this.channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutServerInfoUpdate(serverInfo));
    }

    @Override
    public void startGameServer(final String serverGroupName) {
        this.startGameServer(serverGroupName, new Configuration());
    }

    @Override
    public void startGameServer(final String serverGroupName, final Configuration preConfig) {
        final ServerGroup serverGroup = this.internalCloudNetwork.getServerGroups()
            .getOrDefault(serverGroupName, null);
        this.startGameServer(serverGroup, preConfig);
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup) {
        this.startGameServer(serverGroup.getName(), new Configuration());
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController",
            new PacketOutStartGameServer(serverGroup, preConfig));
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig,
        final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController",
            new PacketOutStartGameServer(serverGroup, preConfig, template));
    }

    @Override
    public void startProxy(final String proxyGroupName) {
        this.startProxy(proxyGroupName, new Configuration());
    }

    @Override
    public void startProxy(final String proxyGroupName, final Configuration preConfig) {
        final ProxyGroup proxyGroup = this.internalCloudNetwork.getProxyGroups()
            .getOrDefault(proxyGroupName, null);
        this.channelHandler.sendPacketAsynchronous("ReformCloudController",
            new PacketOutStartProxy(proxyGroup, preConfig));
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
    public void startProxy(final ProxyGroup proxyGroup, final Configuration preConfig,
        final String template) {
        this.channelHandler.sendPacketAsynchronous("ReformCloudController",
            new PacketOutStartProxy(proxyGroup, preConfig, template));
    }

    @Override
    public boolean stopProxy(String name) {
        return channelHandler
            .sendPacketSynchronized("ReformCloudController", new PacketOutStopProcess(name));
    }

    @Override
    public boolean stopProxy(ProxyInfo proxyInfo) {
        return stopProxy(proxyInfo.getCloudProcess().getName());
    }

    @Override
    public boolean stopServer(String name) {
        return channelHandler
            .sendPacketSynchronized("ReformCloudController", new PacketOutStopProcess(name));
    }

    @Override
    public boolean stopServer(ServerInfo serverInfo) {
        return stopServer(serverInfo.getCloudProcess().getName());
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType,
        Collection<String> clients, SpigotVersions spigotVersions) {
        ServerGroup serverGroup = new ServerGroup(
            name,
            "ReformCloud",
            null,
            new ArrayList<>(clients),
            Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
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
        if (this.internalCloudNetwork.getServerGroups().get(serverGroup.getName()) != null) {
            return;
        }

        channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutCreateServerGroup(serverGroup));
    }

    @Override
    public void createServerGroup(String name) {
        createServerGroup(name, ServerModeType.DYNAMIC, Collections.singletonList("Client-01"),
            SpigotVersions.SPIGOT_1_8_8);
    }

    @Override
    public void createServerGroup(String name, ServerModeType serverModeType,
        Collection<Template> templates) {
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
    public void createServerGroup(String name, ServerModeType serverModeType,
        Collection<String> clients, Collection<Template> templates, SpigotVersions spigotVersions) {
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
    public void createProxyGroup(String name, ProxyModeType proxyModeType,
        Collection<String> clients, ProxyVersions proxyVersions) {
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
        if (this.internalCloudNetwork.getProxyGroups().get(proxyGroup.getName()) != null) {
            return;
        }

        channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutCreateProxyGroup(proxyGroup));
    }

    @Override
    public void createProxyGroup(String name) {
        ProxyGroup proxyGroup = new ProxyGroup(
            name,
            new ArrayList<>(this.internalCloudNetwork.getClients().keySet()),
            new ArrayList<>(),
            Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
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
    public void createProxyGroup(String name, ProxyModeType proxyModeType,
        Collection<Template> templates) {
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
    public void createProxyGroup(String name, ProxyModeType proxyModeType,
        Collection<String> clients, Collection<Template> templates, ProxyVersions proxyVersions) {
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
        if (this.internalCloudNetwork.getClients().get(client.getName()) != null) {
            return;
        }

        channelHandler
            .sendPacketSynchronized("ReformCloudController", new PacketOutCreateClient(client));
    }

    @Override
    public void updateServerInfo(ServerInfo serverInfo) {
        channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutUpdateServerInfo(serverInfo));
    }

    @Override
    public void updateProxyInfo(ProxyInfo proxyInfo) {
        channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutUpdateProxyInfo(proxyInfo));
    }

    @Override
    public void updateServerGroup(ServerGroup serverGroup) {
        channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutUpdateServerGroup(serverGroup));
    }

    @Override
    public void updateProxyGroup(ProxyGroup proxyGroup) {
        channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutUpdateProxyGroup(proxyGroup));
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
        channelHandler
            .sendPacketSynchronized("ReformCloudController", new PacketOutCreateWebUser(webUser));
    }

    @Override
    public void dispatchConsoleCommand(String commandLine) {
        this.channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutDispatchConsoleCommand(commandLine));
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
        ).sendOnCurrentThread().syncUninterruptedly().getConfiguration()
            .getValue("result", new TypeToken<DevProcess>() {
            });
    }

    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup, String template) {
        return this.createPacketFuture(
            new PacketOutQueryStartQueuedProcess(serverGroup, template, new Configuration()),
            "ReformCloudController"
        ).sendOnCurrentThread().syncUninterruptedly().getConfiguration()
            .getValue("result", new TypeToken<DevProcess>() {
            });
    }

    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup, String template,
        Configuration preConfig) {
        return this.createPacketFuture(
            new PacketOutQueryStartQueuedProcess(serverGroup, template, preConfig),
            "ReformCloudController"
        ).sendOnCurrentThread().syncUninterruptedly().getConfiguration()
            .getValue("result", new TypeToken<DevProcess>() {
            });
    }

    @Override
    public OnlinePlayer getOnlinePlayer(UUID uniqueId) {
        PacketFuture packetFuture = this.createPacketFuture(
            new PacketOutQueryGetOnlinePlayer(uniqueId),
            "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null) {
            return null;
        }

        return result.getConfiguration()
            .getValue("result", TypeTokenAdaptor.getONLINE_PLAYER_TYPE());
    }

    @Override
    public OnlinePlayer getOnlinePlayer(String name) {
        PacketFuture packetFuture = this.createPacketFuture(
            new PacketOutQueryGetOnlinePlayer(name),
            "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null) {
            return null;
        }

        return result.getConfiguration()
            .getValue("result", TypeTokenAdaptor.getONLINE_PLAYER_TYPE());
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID uniqueId) {
        PacketFuture packetFuture = this.createPacketFuture(
            new PacketOutQueryGetPlayer(uniqueId),
            "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null) {
            return null;
        }

        return result.getConfiguration()
            .getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        PacketFuture packetFuture = this.createPacketFuture(
            new PacketOutQueryGetPlayer(name),
            "ReformCloudController"
        );
        Packet result = packetFuture.syncUninterruptedly(2, TimeUnit.SECONDS);
        if (result.getResult() == null) {
            return null;
        }

        return result.getConfiguration()
            .getValue("result", TypeTokenAdaptor.getOFFLINE_PLAYER_TYPE());
    }

    @Override
    public void updateOnlinePlayer(OnlinePlayer onlinePlayer) {
        this.channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutUpdateOnlinePlayer(onlinePlayer));
    }

    @Override
    public void updateOfflinePlayer(OfflinePlayer offlinePlayer) {
        this.channelHandler.sendPacketSynchronized("ReformCloudController",
            new PacketOutUpdateOfflinePlayer(offlinePlayer));
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
        return this.serverInfo.getServerGroup().getMaxPlayers();
    }

    @Override
    public int getOnlineCount() {
        return this.serverInfo.getOnline();
    }

    @Override
    public int getGlobalOnlineCount() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.getAllRegisteredProxies()
            .forEach(proxyInfo1 -> atomicInteger.addAndGet(proxyInfo1.getOnline()));

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
        return this.internalCloudNetwork.getServerProcessManager()
            .getAllRegisteredServerProcesses();
    }

    @Override
    public List<ProxyInfo> getAllRegisteredProxies() {
        return this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses();
    }

    @Override
    public List<ServerInfo> getAllRegisteredServers(String groupName) {
        return new ArrayList<>(this.internalCloudNetwork.getServerProcessManager()
            .getAllRegisteredServerGroupProcesses(groupName));
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
    public void sendPacketQuery(String channel, Packet packet,
        NetworkQueryInboundHandler onSuccess) {
        this.channelHandler.sendPacketQuerySync(
            channel, this.serverInfo.getCloudProcess().getName(), packet, onSuccess
        );
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess,
        NetworkQueryInboundHandler onFailure) {
        this.channelHandler.sendPacketQuerySync(
            channel, this.serverInfo.getCloudProcess().getName(), packet, onSuccess, onFailure
        );
    }

    @Override
    public PacketFuture createPacketFuture(Packet packet, String networkComponent) {
        this.channelHandler
            .toQueryPacket(packet, UUID.randomUUID(), this.serverInfo.getCloudProcess().getName());
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
            channel, this.serverInfo.getCloudProcess().getName(), packet
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

        if (client == null) {
            return null;
        }

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

    /**
     * @deprecated
     */
    @Deprecated
    @Override
    public void removeInternalProcess() {
        SpigotBootstrap.getInstance().onDisable();
    }

    @Override
    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    @Override
    public Optional<ISaveAPIService> getAPISave() {
        return Optional.ofNullable(ISaveAPIService.instance.get());
    }

    @EventHandler
    public void handle(final PlayerMoveEvent event) {
        if (!event.isCancelled()) {
            this.tempServerStats.addWalkedDistance(event.getFrom().distance(event.getTo()));
        }
    }

    @EventHandler
    public void handle(final BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            this.tempServerStats.addPlacedBlock();
        }
    }

    public static ReformCloudAPISpigot getInstance() {
        return instance;
    }

    public NettySocketClient getNettySocketClient() {
        return nettySocketClient;
    }

    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public ServerStartupInfo getServerStartupInfo() {
        return serverStartupInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public InternalCloudNetwork getInternalCloudNetwork() {
        return internalCloudNetwork;
    }

    public void setInternalCloudNetwork(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public TempServerStats getTempServerStats() {
        return tempServerStats;
    }

    public PermissionCache getPermissionCache() {
        return permissionCache;
    }

    public void setPermissionCache(PermissionCache permissionCache) {
        this.permissionCache = permissionCache;
    }

    public Map<UUID, PermissionHolder> getCachedPermissionHolders() {
        return cachedPermissionHolders;
    }

    public void setCachedPermissionHolders(Map<UUID, PermissionHolder> cachedPermissionHolders) {
        this.cachedPermissionHolders = cachedPermissionHolders;
    }

    public long getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(long internalTime) {
        this.internalTime = internalTime;
    }
}
