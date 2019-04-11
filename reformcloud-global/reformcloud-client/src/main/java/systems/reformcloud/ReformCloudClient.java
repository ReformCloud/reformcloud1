/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.addons.AddonParallelLoader;
import systems.reformcloud.api.*;
import systems.reformcloud.api.save.ISaveAPIService;
import systems.reformcloud.commands.*;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.LoadSuccessEvent;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketClient;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.packets.in.*;
import systems.reformcloud.network.packets.out.PacketOutStartGameServer;
import systems.reformcloud.network.packets.out.PacketOutStartProxy;
import systems.reformcloud.network.packets.out.PacketOutUpdateOfflinePlayer;
import systems.reformcloud.network.packets.out.PacketOutUpdateOnlinePlayer;
import systems.reformcloud.network.packets.query.out.PacketOutQueryGetOnlinePlayer;
import systems.reformcloud.network.packets.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.network.packets.sync.in.PacketInSyncControllerTime;
import systems.reformcloud.network.packets.sync.in.PacketInSyncScreenDisable;
import systems.reformcloud.network.packets.sync.in.PacketInSyncScreenJoin;
import systems.reformcloud.network.packets.sync.in.PacketInSyncUpdateClient;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncClientDisconnects;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncClientUpdateSuccess;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.serverprocess.CloudProcessStartupHandler;
import systems.reformcloud.serverprocess.screen.CloudProcessScreenService;
import systems.reformcloud.serverprocess.screen.internal.ClientScreenHandler;
import systems.reformcloud.serverprocess.shutdown.ShutdownWorker;
import systems.reformcloud.synchronization.SynchronizationHandler;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.cloudsystem.ServerProcessManager;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.threading.TaskScheduler;
import systems.reformcloud.versioneering.VersionController;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 23.10.2018
 */

@Getter
@Setter
public class ReformCloudClient implements Shutdown, Reload, IAPIService {
    public static volatile boolean RUNNING = false;

    @Getter
    private static ReformCloudClient instance;

    private LoggerProvider loggerProvider;
    private CommandManager commandManager;

    private boolean ssl;

    private ClientInfo clientInfo;

    private final EventManager eventManager = new EventManager();
    private final AddonParallelLoader addonParallelLoader = new AddonParallelLoader();

    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private final TaskScheduler taskScheduler;
    private final ChannelHandler channelHandler;
    private final NettySocketClient nettySocketClient = new NettySocketClient();
    private final CloudProcessStartupHandler cloudProcessStartupHandler = new CloudProcessStartupHandler();
    private final CloudProcessScreenService cloudProcessScreenService = new CloudProcessScreenService();

    private CloudConfiguration cloudConfiguration;

    private final ClientScreenHandler clientScreenHandler;

    @Setter
    private long internalTime = System.currentTimeMillis();

    /**
     * Creates a new Instance of the {ReformCloudClient}
     *
     * @param loggerProvider
     * @param commandManager
     * @param ssl
     * @param time
     * @throws Throwable
     */
    public ReformCloudClient(LoggerProvider loggerProvider, CommandManager commandManager, boolean ssl, final long time) throws Throwable {
        if (instance == null)
            instance = this;
        else
            StringUtil.printError(loggerProvider, "ReformCloudClient Instance already exists", new LoadException(new InstanceAlreadyExistsException()));

        this.ssl = ssl;
        this.commandManager = commandManager;
        this.loggerProvider = loggerProvider;

        ISaveAPIService.instance.set(new SaveAPIImpl());
        IAPIService.instance.set(this);
        IDefaultPlayerProvider.instance.set(new PlayerProvider());

        this.cloudConfiguration = new CloudConfiguration(false);
        new ReformCloudLibraryServiceProvider(loggerProvider, this.cloudConfiguration.getControllerKey(), cloudConfiguration.getEthernetAddress().getHost(), eventManager, StringUtil.NULL);

        this.taskScheduler = ReformCloudLibraryServiceProvider.getInstance().getTaskScheduler();
        this.channelHandler = new ChannelHandler();

        this.registerNetworkHandlers();
        this.registerCommands();

        this.clientScreenHandler = new ClientScreenHandler();
        loggerProvider.registerLoggerHandler(clientScreenHandler);

        this.addonParallelLoader.loadAddons();

        new EventAdapter();

        this.clientInfo = new ClientInfo(
                cloudConfiguration.getMemory(),
                Runtime.getRuntime().availableProcessors(),
                true,
                new ArrayList<>(),
                new ArrayList<>(),
                this.getMemory(),
                ReformCloudLibraryService.cpuUsage(),
                ReformCloudLibraryService.usedMemorySystem(),
                ReformCloudLibraryService.usedMemorySystem()
        );

        this.taskScheduler
                .schedule(SynchronizationHandler.class, TimeUnit.SECONDS, 30)
                .schedule(ShutdownWorker.class, TimeUnit.SECONDS, 10);

        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(this.cloudProcessScreenService);
        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(this.cloudProcessStartupHandler);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownAll, "ShutdownHook"));

        this.connect(ssl);

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        RUNNING = true;

        loggerProvider.info(this.getInternalCloudNetwork().getLoaded().getLoading_done()
                .replace("%time%", String.valueOf(System.currentTimeMillis() - time)));
        this.eventManager.callEvent(EventTargetType.LOAD_SUCCESS, new LoadSuccessEvent(true));
    }

    private void registerNetworkHandlers() {
        getNettyHandler().registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal())
                .registerHandler("StartCloudServer", new PacketInStartGameServer())
                .registerHandler("StartProxy", new PacketInStartProxy())
                .registerHandler("UpdateAll", new PacketInUpdateAll())
                .registerHandler("ExecuteCommand", new PacketInExecuteCommand())
                .registerHandler("StopProcess", new PacketInStopProcess())
                .registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate())
                .registerHandler("UpdateClientFromURL", new PacketInUpdateClientFromURL())
                .registerHandler("CopyServerIntoTemplate", new PacketInCopyServerIntoTemplate())
                .registerHandler("UpdateClientAddon", new PacketInUpdateClientAddon())
                .registerHandler("UpdateServerGroupPlugin", new PacketInUpdateSeverGroupPlugin())
                .registerHandler("UpdateProxyGroupPlugin", new PacketInUpdateProxyGroupPlugin())
                .registerHandler("UpdateProxyGroupPluginTemplate", new PacketInUpdateProxyGroupPluginTemplate())
                .registerHandler("UpdateServerGroupPluginTemplate", new PacketInUpdateServerGroupPluginTemplate())
                .registerHandler("PacketInUploadLog", new PacketInUploadLog())
                .registerHandler("UpdateClientSetting", new PacketInUpdateClientSetting())
                .registerHandler("JoinScreen", new PacketInSyncScreenJoin())
                .registerHandler("ScreenDisable", new PacketInSyncScreenDisable())
                .registerHandler("ReloadClient", new PacketInSyncUpdateClient())
                .registerHandler("ExecuteClientCommand", new PacketInExecuteClientCommand())
                .registerHandler("DeployServer", new PacketInDeployServer())
                .registerHandler("TemplateDeployReady", new PacketInTemplateDeployReady())
                .registerHandler("ClientProcessQueue", new PacketInGetClientProcessQueue())
                .registerHandler("SyncControllerTime", new PacketInSyncControllerTime())
                .registerHandler("RemoveProxyQueueProcess", new PacketInRemoveProxyProcessQueue())
                .registerHandler("GetControllerTemplateResult", new PacketInGetControllerTemplateResult())
                .registerHandler("RemoveServerQueueProcess", new PacketInRemoveServerQueueProcess());
    }

    private void registerCommands() {
        commandManager
                .registerCommand(new CommandExit())
                .registerCommand(new CommandUpdate())
                .registerCommand(new CommandHelp())
                .registerCommand(new CommandVersion())
                .registerCommand(new CommandAddons())
                .registerCommand(new CommandReload());
    }

    @Override
    public void reloadAll() {
        RUNNING = false;
        final String oldName = this.cloudConfiguration.getClientName();

        this.cloudConfiguration = null;
        this.getNettyHandler().clearHandlers();
        this.commandManager.clearCommands();
        this.addonParallelLoader.disableAddons();
        this.eventManager.unregisterAllListener();

        this.cloudConfiguration = new CloudConfiguration(true);
        this.cloudConfiguration.setClientName(oldName);

        this.addonParallelLoader.loadAddons();

        this.registerNetworkHandlers();
        this.registerCommands();

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        this.clientInfo.setMaxMemory(this.cloudConfiguration.getMemory());

        RUNNING = true;
        this.loggerProvider.info(this.internalCloudNetwork.getLoaded().getGlobal_reload_done());
        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutSyncClientUpdateSuccess());
    }

    private boolean shutdown = false;

    @Override
    public void shutdownAll() {
        if (shutdown)
            return;

        shutdown = true;
        RUNNING = false;

        this.channelHandler.sendPacketAsynchronous("ReformCloudController", new PacketOutSyncClientDisconnects());

        ReformCloudLibraryService.sleep(500);
        this.nettySocketClient.close();

        this.cloudProcessScreenService.getRegisteredProxyProcesses().forEach(proxyProcess -> {
            proxyProcess.shutdown(null, false);
            this.getLoggerProvider().info(this.internalCloudNetwork.getLoaded().getClient_shutdown_process()
                    .replace("%name%", proxyProcess.getProxyStartupInfo().getName()));
            ReformCloudLibraryService.sleep(1000);
        });
        this.cloudProcessScreenService.getRegisteredServerProcesses().forEach(serverProcess -> {
            serverProcess.shutdown(false);
            this.getLoggerProvider().info(this.internalCloudNetwork.getLoaded().getClient_shutdown_process()
                    .replace("%name%", serverProcess.getServerStartupInfo().getName()));
            ReformCloudLibraryService.sleep(1000);
        });

        this.addonParallelLoader.disableAddons();
        ReformCloudLibraryService.sleep(1000);
    }

    /**
     * Get used memory of ReformCloudClient
     *
     * @return {@link Integer} of used memory by adding the maximal memory of the ProxyProcesses
     * to the maximal memory of the ServerProcesses
     * @see ServerProcessManager#getUsedProxyMemory()
     * @see ServerProcessManager#getUsedServerMemory()
     */
    public int getMemory() {
        int used = 0;

        List<ProxyInfo> infos = this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredProxyProcesses()
                .stream()
                .filter(e -> e.getCloudProcess().getClient().equals(this.cloudConfiguration.getClientName()))
                .collect(Collectors.toList());
        List<ServerInfo> serverInfos = this.internalCloudNetwork
                .getServerProcessManager()
                .getAllRegisteredServerProcesses()
                .stream()
                .filter(e -> e.getCloudProcess().getClient().equals(this.cloudConfiguration.getClientName()))
                .collect(Collectors.toList());

        for (ProxyInfo proxyInfo : infos)
            used = used + proxyInfo.getMaxMemory();

        for (ServerInfo serverInfo : serverInfos)
            used = used + serverInfo.getMaxMemory();

        return used;
    }

    /**
     * Connects to the Controller by using {@link Boolean}
     *
     * @param ssl
     */
    public void connect(final boolean ssl) {
        this.nettySocketClient.setConnections(1);

        while (this.nettySocketClient.getConnections() != -1 && !shutdown) {
            if (this.nettySocketClient.getConnections() == 8)
                System.exit(ExitUtil.CONTROLLER_NOT_REACHABLE);

            this.nettySocketClient.connect(cloudConfiguration.getEthernetAddress(), this.channelHandler, ssl);

            ReformCloudLibraryService.sleep(2000);
        }
    }

    public void checkForUpdates() {
        if (VersionController.isVersionAvailable()) {
            loggerProvider.warn(this.internalCloudNetwork.getLoaded().getVersion_available());
        } else {
            loggerProvider.info(this.internalCloudNetwork.getLoaded().getVersion_update());
        }
    }

    public boolean isPortUseable(final int port) {
        boolean useable = false;
        try {
            Socket socket = new Socket(this.cloudConfiguration.getStartIP(), port);
            if (socket.isClosed() && !socket.isConnected())
                useable = true;

            socket.close();
        } catch (final IOException ignored) {
            useable = true;
        }

        return useable;
    }

    public void updateInternalTime(final long controller) {
        this.internalTime = controller;
        this.loggerProvider.setControllerTime(controller);
    }

    @Override
    public void startGameServer(final String serverGroupName) {
        this.startGameServer(serverGroupName, new Configuration());
    }

    @Override
    public void startGameServer(final String serverGroupName, final Configuration preConfig) {
        final ServerGroup serverGroup = this.internalCloudNetwork.getServerGroups().getOrDefault(serverGroupName, null);
        if (serverGroup == null)
            return;

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
        if (proxyGroup == null)
            return;

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
        int max = 0;
        for (ProxyGroup proxyGroup : this.internalCloudNetwork.getProxyGroups().values())
            max += proxyGroup.getMaxPlayers();

        return max;
    }

    @Override
    public int getOnlineCount() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.getAllRegisteredProxies().stream()
                .filter(info -> info.getCloudProcess().getClient().equals(this.cloudConfiguration.getClientName()))
                .forEach(proxyInfo1 -> atomicInteger.addAndGet(proxyInfo1.getOnline()));

        return atomicInteger.get();
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
                channel, this.cloudConfiguration.getClientName(), packet, onSuccess
        );
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess, NetworkQueryInboundHandler onFailure) {
        this.channelHandler.sendPacketQuerySync(
                channel, this.cloudConfiguration.getClientName(), packet, onSuccess, onFailure
        );
    }

    @Override
    public PacketFuture createPacketFuture(Packet packet, String networkComponent) {
        this.channelHandler.toQueryPacket(packet, UUID.randomUUID(), this.cloudConfiguration.getClientName());
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
                channel, this.cloudConfiguration.getClientName(), packet
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

    @Override
    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    @Override
    public Optional<ISaveAPIService> getAPISave() {
        return Optional.ofNullable(ISaveAPIService.instance.get());
    }

    @Override
    public void removeInternalProcess() {
        System.exit(ExitUtil.STOPPED_SUCESS);
    }
}
