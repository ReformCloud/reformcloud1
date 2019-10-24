/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import com.google.gson.reflect.TypeToken;
import io.netty.channel.ChannelFutureListener;
import systems.reformcloud.addons.AddonParallelLoader;
import systems.reformcloud.api.*;
import systems.reformcloud.api.permissions.PermissionHelper;
import systems.reformcloud.api.save.SaveAPIService;
import systems.reformcloud.commands.*;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.event.DefaultEventManager;
import systems.reformcloud.event.abstracts.EventManager;
import systems.reformcloud.event.events.internal.ReloadDoneEvent;
import systems.reformcloud.event.events.internal.StartedEvent;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.logging.ColouredConsoleProvider;
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
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.system.RuntimeSnapshot;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketClient;
import systems.reformcloud.network.abstracts.AbstractChannelHandler;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.packets.in.*;
import systems.reformcloud.network.packets.out.*;
import systems.reformcloud.network.packets.query.in.PacketInQueryGetRuntimeInformation;
import systems.reformcloud.network.packets.query.out.PacketOutQueryGetOnlinePlayer;
import systems.reformcloud.network.packets.query.out.PacketOutQueryGetPlayer;
import systems.reformcloud.network.packets.query.out.PacketOutQueryGetRuntimeInformation;
import systems.reformcloud.network.packets.query.out.PacketOutQueryStartQueuedProcess;
import systems.reformcloud.network.packets.sync.in.*;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncClientDisconnects;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncClientUpdateSuccess;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.serverprocess.CloudProcessStartupHandler;
import systems.reformcloud.serverprocess.screen.CloudProcessScreenService;
import systems.reformcloud.serverprocess.screen.internal.ClientScreenHandler;
import systems.reformcloud.serverprocess.shutdown.ShutdownWorker;
import systems.reformcloud.serverprocess.startup.CloudServerStartupHandler;
import systems.reformcloud.serverprocess.startup.ProxyStartupHandler;
import systems.reformcloud.synchronization.SynchronizationHandler;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.defaults.DefaultCloudService;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.parameters.ParameterManager;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.threading.AbstractTaskScheduler;
import systems.reformcloud.versioneering.VersionController;

import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 23.10.2018
 */

public final class ReformCloudClient implements Serializable, Shutdown, Reload, APIService {

    public static volatile boolean RUNNING = false;

    private static ReformCloudClient instance;

    private AbstractLoggerProvider colouredConsoleProvider;

    private AbstractCommandManager commandManager;

    private boolean ssl;

    private ClientInfo clientInfo;

    private final EventManager eventManager = new DefaultEventManager();

    private final AddonParallelLoader addonParallelLoader = new AddonParallelLoader();

    private final ParameterManager parameterManager = new ParameterManager(new ArrayList<>());

    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private final AbstractTaskScheduler taskScheduler;

    private final AbstractChannelHandler channelHandler;

    private final NettySocketClient nettySocketClient = new NettySocketClient();

    private final SynchronizationHandler synchronizationHandler;

    private final CloudProcessStartupHandler cloudProcessStartupHandler = new CloudProcessStartupHandler();

    private final CloudProcessScreenService cloudProcessScreenService = new CloudProcessScreenService();

    private CloudConfiguration cloudConfiguration;

    private final ClientScreenHandler clientScreenHandler;

    private long internalTime = System.currentTimeMillis();

    private final NetworkInboundHandler internalConnectionHandler;

    /**
     * Creates a new Instance of the ReformCloudClient
     *
     * @param loggerProvider The default logger provider of the cloud system
     * @param commandManager The command manger which should be used in the cloud system
     * @param ssl If ssl should be enabled or not
     * @param time The startup time of the client
     * @throws Throwable If any exception occurs it will be catch and printed
     */
    public ReformCloudClient(AbstractLoggerProvider loggerProvider, AbstractCommandManager commandManager,
                             boolean ssl, final long time) throws Throwable {
        if (instance == null) {
            instance = this;
        } else {
            StringUtil.printError(colouredConsoleProvider, "ReformCloudClient Instance already exists",
                new LoadException(new InstanceAlreadyExistsException()));
        }

        FileUtils.deleteFileIfExists(Paths.get("reformcloud/files/ReformCloudProcess.jar"));

        this.ssl = ssl;
        this.commandManager = commandManager;
        this.colouredConsoleProvider = loggerProvider;

        SaveAPIService.instance.set(new SaveAPIImpl());
        APIService.instance.set(this);
        new DefaultCloudService(this);
        DefaultPlayerProvider.instance.set(new PlayerProvider());

        this.cloudConfiguration = new CloudConfiguration(false);
        new ReformCloudLibraryServiceProvider(colouredConsoleProvider,
            this.cloudConfiguration.getControllerKey(),
            cloudConfiguration.getEthernetAddress().getHost(), eventManager, StringUtil.NULL);

        this.taskScheduler = ReformCloudLibraryServiceProvider.getInstance().getTaskScheduler();
        this.channelHandler = new ChannelHandler();

        this.registerCommands();

        this.clientScreenHandler = new ClientScreenHandler();
        colouredConsoleProvider.registerLoggerHandler(clientScreenHandler);

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

        this.synchronizationHandler = new SynchronizationHandler();

        this.taskScheduler
            .schedule(new ShutdownWorker(), -1, TimeUnit.SECONDS.toMillis(10));

        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(this.cloudProcessScreenService);
        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(this.cloudProcessStartupHandler);
        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(this.synchronizationHandler);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownAll, "Shutdown-Hook"));

        Lock connectionLock = new ReentrantLock();

        try {
            connectionLock.lock();
            Condition condition = connectionLock.newCondition();

            this.internalConnectionHandler = new PacketInInitializeInternal(
                connectionLock, condition
            );

            this.registerNetworkHandlers();
            this.connect(ssl);

            condition.await();
        } finally {
            connectionLock.unlock();
        }

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        RUNNING = true;

        colouredConsoleProvider.info(this.getInternalCloudNetwork().getLoaded().getLoading_done()
            .replace("%time%", String.valueOf(System.currentTimeMillis() - time)));
        this.eventManager.fire(new StartedEvent());
    }

    public static ReformCloudClient getInstance() {
        return ReformCloudClient.instance;
    }

    public void updateServerInfoInternal(ServerInfo serverInfo) {
        internalCloudNetwork.getServerProcessManager().updateServerInfo(serverInfo);
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(internalCloudNetwork);
    }

    public void updateProxyInfoInternal(ProxyInfo proxyInfo) {
        internalCloudNetwork.getServerProcessManager().updateProxyInfo(proxyInfo);
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(internalCloudNetwork);
    }

    private void registerNetworkHandlers() {
        getNettyHandler()
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
            .registerHandler("UpdateProxyGroupPluginTemplate",
                new PacketInUpdateProxyGroupPluginTemplate())
            .registerHandler("UpdateServerGroupPluginTemplate",
                new PacketInUpdateServerGroupPluginTemplate())
            .registerHandler("PacketInUploadLog", new PacketInUploadLog())
            .registerHandler("UpdateClientSetting", new PacketInUpdateClientSetting())
            .registerHandler("JoinScreen", new PacketInSyncScreenJoin())
            .registerHandler("ScreenDisable", new PacketInSyncScreenDisable())
            .registerHandler("ReloadClient", new PacketInSyncUpdateClient())
            .registerHandler("ExecuteClientCommand", new PacketInExecuteClientCommand())
            .registerHandler("DeployServer", new PacketInDeployServer())
            .registerHandler("TemplateDeployReady", new PacketInTemplateDeployReady())
            .registerHandler("EnableDebug", new PacketInEnableDebug())
            .registerHandler("ClientProcessQueue", new PacketInGetClientProcessQueue())
            .registerHandler("ParametersUpdate", new PacketInParameterUpdate())
            .registerHandler("EnableProperties", new PacketInEnableProperties())
            .registerHandler("DisableProperties", new PacketInDisableProperties())
            .registerHandler("DeleteTemplate", new PacketInDeleteTemplate())
            .registerHandler("SyncStandby", new PacketInSyncStandby())
            .registerHandler("SyncControllerTime", new PacketInSyncControllerTime())
            .registerHandler("RemoveProxyQueueProcess", new PacketInRemoveProxyProcessQueue())
            .registerHandler("GetControllerTemplateResult",
                new PacketInGetControllerTemplateResult())
            .registerHandler("DisableBackup", new PacketInDisableBackup())
            .registerHandler("EnableBackup", new PacketInEnableBackup())
            .registerHandler("RemoveServerQueueProcess", new PacketInRemoveServerQueueProcess())
            .registerQueryHandler("GetRuntimeInformation", new PacketInQueryGetRuntimeInformation())
            .registerHandler("ProxyInfoUpdate", new PacketInProxyInfoUpdate())
            .registerHandler("InitializeCloudNetwork", internalConnectionHandler);
    }

    private void registerCommands() {
        commandManager
            .registerCommand(new CommandExit())
            .registerCommand(new CommandUpdate())
            .registerCommand(new CommandHelp())
            .registerCommand(new CommandVersion())
            .registerCommand(new CommandAddons())
            .registerCommand(new CommandClear())
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
        this.eventManager.unregisterAll();

        this.cloudConfiguration = new CloudConfiguration(true);
        this.cloudConfiguration.setClientName(oldName);

        this.addonParallelLoader.loadAddons();

        this.registerNetworkHandlers();
        this.registerCommands();

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        this.clientInfo.setMaxMemory(this.cloudConfiguration.getMemory());

        RUNNING = true;
        this.colouredConsoleProvider.info(this.internalCloudNetwork.getLoaded().getGlobal_reload_done());
        this.channelHandler.sendPacketAsynchronous("ReformCloudController",
            new PacketOutSyncClientUpdateSuccess());
        this.eventManager.fire(new ReloadDoneEvent());
    }

    private boolean shutdown = false;

    @Override
    public void shutdownAll() {
        if (shutdown) {
            return;
        }

        shutdown = true;
        RUNNING = false;
        this.synchronizationHandler.delete();

        if (this.channelHandler.getChannel("ReformCloudController") != null) {
            this.channelHandler.getChannel("ReformCloudController")
                .writeAndFlush(new PacketOutSyncClientDisconnects())
                .addListener(ChannelFutureListener.CLOSE);
        }

        ReformCloudLibraryService.sleep(500);

        this.cloudProcessScreenService.getRegisteredProxyProcesses().forEach(proxyProcess -> {
            proxyProcess.shutdown(null, false);
            this.getColouredConsoleProvider()
                .info(this.internalCloudNetwork.getLoaded().getClient_shutdown_process()
                    .replace("%name%", proxyProcess.getProxyStartupInfo().getName()));
            ReformCloudLibraryService.sleep(1000);
        });
        this.cloudProcessScreenService.getRegisteredServerProcesses().forEach(serverProcess -> {
            serverProcess.shutdown(false);
            this.getColouredConsoleProvider()
                .info(this.internalCloudNetwork.getLoaded().getClient_shutdown_process()
                    .replace("%name%", serverProcess.getServerStartupInfo().getName()));
            ReformCloudLibraryService.sleep(1000);
        });

        this.nettySocketClient.close();
        this.addonParallelLoader.disableAddons();
        ReformCloudLibraryService.sleep(1000);
    }

    /**
     * Get used memory of ReformCloudClient
     *
     * @return of used memory by adding the maximal memory of the ProxyProcesses to the maximal memory
     * of the ServerProcesses
     */
    public int getMemory() {
        int used = 0;

        List<ProxyInfo> infos = this.internalCloudNetwork
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .stream()
            .filter(e -> e.getCloudProcess().getClient()
                .equals(this.cloudConfiguration.getClientName()))
            .collect(Collectors.toList());
        List<ServerInfo> serverInfos = this.internalCloudNetwork
            .getServerProcessManager()
            .getAllRegisteredServerProcesses()
            .stream()
            .filter(e -> e.getCloudProcess().getClient()
                .equals(this.cloudConfiguration.getClientName()))
            .collect(Collectors.toList());

        for (ProxyInfo proxyInfo : infos) {
            used = used + proxyInfo.getMaxMemory();
        }

        for (ServerInfo serverInfo : serverInfos) {
            used = used + serverInfo.getMaxMemory();
        }

        return used;
    }

    /**
     * Connects to the Controller
     *
     * @param ssl If ssl is enabled or not
     */
    public void connect(final boolean ssl) {
        this.nettySocketClient.setConnections(1);
        this.nettySocketClient.close();

        while (this.nettySocketClient.getConnections() != -1 && !shutdown) {
            if (this.nettySocketClient.getConnections() == 8) {
                System.exit(ExitUtil.CONTROLLER_NOT_REACHABLE);
            }

            this.nettySocketClient
                .connect(cloudConfiguration.getEthernetAddress(), this.channelHandler, ssl);

            ReformCloudLibraryService.sleep(2000);
        }
    }

    public void checkForUpdates() {
        if (VersionController.isVersionAvailable()) {
            colouredConsoleProvider.warn(this.internalCloudNetwork.getLoaded().getVersion_available());
        } else {
            colouredConsoleProvider.info(this.internalCloudNetwork.getLoaded().getVersion_update());
        }
    }

    public void updateInternalTime(final long controller) {
        this.internalTime = controller;
        this.colouredConsoleProvider.setControllerTime(controller);
    }

    @Override
    public void startGameServer(final String serverGroupName) {
        this.startGameServer(serverGroupName, new Configuration());
    }

    @Override
    public void startGameServer(final String serverGroupName, final Configuration preConfig) {
        final ServerGroup serverGroup = this.internalCloudNetwork.getServerGroups()
            .getOrDefault(serverGroupName, null);
        if (serverGroup == null) {
            return;
        }

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
        if (proxyGroup == null) {
            return;
        }

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
            new AutoStart(true, 510, TimeUnit.MINUTES.toSeconds(20)),
            new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
            serverModeType,
            spigotVersions,
            true
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
            SpigotVersions.SPIGOT_1_8_8,
            true
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
            spigotVersions,
            true
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
            new AutoStart(true, 45, TimeUnit.MINUTES.toSeconds(20)),
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
    public void updateServerName(ServerInfo serverInfo, String newName) {
        if (serverInfo == null || newName == null) {
            return;
        }

        serverInfo.getCloudProcess().setName(newName);
        this.updateServerInfo(serverInfo);
    }

    @Override
    public void updateServerName(String serverName, String newName) {
        this.updateServerName(this.getServerInfo(serverName), newName);
    }

    @Override
    public void updateProxyName(ProxyInfo proxyInfo, String newName) {
        if (proxyInfo == null || newName == null) {
            return;
        }

        proxyInfo.getCloudProcess().setName(newName);
        this.updateProxyInfo(proxyInfo);
    }

    @Override
    public void updateProxyName(String proxyName, String newName) {
        this.updateProxyName(this.getProxyInfo(proxyName), newName);
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
    public void executeCommandOnServer(String serverName, String commandLine) {
        Require.requiresNotNull(serverName);
        Require.requiresNotNull(commandLine);
        CloudServerStartupHandler cloudServerStartupHandler = this.cloudProcessScreenService
            .getRegisteredServerHandler(serverName);
        if (cloudServerStartupHandler == null) {
            return;
        }

        cloudServerStartupHandler.executeCommand(commandLine);
    }

    @Override
    public void executeCommandOnServer(String serverName, CharSequence commandLine) {
        this.executeCommandOnServer(serverName, String.valueOf(commandLine));
    }

    @Override
    public void executeCommandOnProxy(String proxyName, String commandLine) {
        Require.requiresNotNull(proxyName);
        Require.requiresNotNull(commandLine);
        ProxyStartupHandler proxyStartupHandler = this.cloudProcessScreenService
            .getRegisteredProxyHandler(proxyName);
        if (proxyStartupHandler == null) {
            return;
        }

        proxyStartupHandler.executeCommand(commandLine);
    }

    @Override
    public void executeCommandOnProxy(String proxyName, CharSequence commandLine) {
        this.executeCommandOnProxy(proxyName, String.valueOf(commandLine));
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
        Packet result = packetFuture.sendOnCurrentThread().syncUninterruptedly(2, TimeUnit.SECONDS);
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
        Packet result = packetFuture.sendOnCurrentThread().syncUninterruptedly(2, TimeUnit.SECONDS);
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
        Packet result = packetFuture.sendOnCurrentThread().syncUninterruptedly(2, TimeUnit.SECONDS);
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
        Packet result = packetFuture.sendOnCurrentThread().syncUninterruptedly(2, TimeUnit.SECONDS);
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
        int max = 0;
        for (ProxyGroup proxyGroup : this.internalCloudNetwork.getProxyGroups().values()) {
            max += proxyGroup.getMaxPlayers();
        }

        return max;
    }

    @Override
    public int getOnlineCount() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.getAllRegisteredProxies().stream()
            .filter(info -> info.getCloudProcess().getClient()
                .equals(this.cloudConfiguration.getClientName()))
            .forEach(proxyInfo1 -> atomicInteger.addAndGet(proxyInfo1.getOnline()));

        return atomicInteger.get();
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
    public boolean sendPacket(String subChannel, DefaultPacket packet) {
        return this.channelHandler.sendPacketAsynchronous(subChannel, packet);
    }

    @Override
    public boolean sendPacketSync(String subChannel, DefaultPacket packet) {
        return this.channelHandler.sendPacketSynchronized(subChannel, packet);
    }

    @Override
    public void sendPacketToAll(DefaultPacket packet) {
        this.channelHandler.sendToAllAsynchronous(packet);
    }

    @Override
    public void sendPacketToAllSync(DefaultPacket packet) {
        this.channelHandler.sendToAllSynchronized(packet);
    }

    @Override
    public void sendPacketQuery(String channel, DefaultPacket packet,
                                NetworkQueryInboundHandler onSuccess) {
        this.channelHandler.sendPacketQuerySync(
            channel, this.cloudConfiguration.getClientName(), packet, onSuccess
        );
    }

    @Override
    public void sendPacketQuery(String channel, DefaultPacket packet, NetworkQueryInboundHandler onSuccess,
                                NetworkQueryInboundHandler onFailure) {
        this.channelHandler.sendPacketQuerySync(
            channel, this.cloudConfiguration.getClientName(), packet, onSuccess, onFailure
        );
    }

    @Override
    public PacketFuture createPacketFuture(DefaultPacket packet, String networkComponent) {
        this.channelHandler
            .toQueryPacket(packet, UUID.randomUUID(), this.cloudConfiguration.getClientName());
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
    public PacketFuture sendPacketQuery(String channel, DefaultPacket packet) {
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

    @Override
    public RuntimeSnapshot shiftControllerRuntimeSnapshot() {
        return this.channelHandler.sendPacketQuerySync(
            "ReformCloudController",
            "ReformCloudClient",
            new PacketOutQueryGetRuntimeInformation("Controller", "controller")
        )
            .sendOnCurrentThread()
            .syncUninterruptedly()
            .getConfiguration()
            .getValue("info", new TypeToken<RuntimeSnapshot>() {
            });
    }

    @Override
    public RuntimeSnapshot shiftClientRuntimeSnapshot(String clientName) {
        Require.requireNotNull(clientName);
        if (clientName.equals(this.cloudConfiguration.getClientName())) {
            return new RuntimeSnapshot();
        }

        return this.channelHandler.sendPacketQuerySync(
            "ReformCloudController",
            "ReformCloudClient",
            new PacketOutQueryGetRuntimeInformation(clientName, "client")
        )
            .sendOnCurrentThread()
            .syncUninterruptedly()
            .getConfiguration()
            .getValue("info", new TypeToken<RuntimeSnapshot>() {
            });
    }

    @Override
    public RuntimeSnapshot shiftClientRuntimeSnapshot(Client client) {
        Require.requireNotNull(client);
        return shiftClientRuntimeSnapshot(client.getName());
    }

    @Override
    public RuntimeSnapshot shiftProxyRuntimeSnapshot(String proxyName) {
        Require.requireNotNull(proxyName);
        return this.channelHandler.sendPacketQuerySync(
            "ReformCloudController",
            "ReformCloudClient",
            new PacketOutQueryGetRuntimeInformation(proxyName, "proxy")
        )
            .sendOnCurrentThread()
            .syncUninterruptedly()
            .getConfiguration()
            .getValue("info", new TypeToken<RuntimeSnapshot>() {
            });
    }

    @Override
    public RuntimeSnapshot shiftProxyRuntimeSnapshot(ProxyInfo proxyInfo) {
        Require.requireNotNull(proxyInfo);
        return shiftProxyRuntimeSnapshot(proxyInfo.getCloudProcess().getName());
    }

    @Override
    public RuntimeSnapshot shiftServerRuntimeSnapshot(String serverName) {
        Require.requireNotNull(serverName);
        return this.channelHandler.sendPacketQuerySync(
            "ReformCloudController",
            "ReformCloudClient",
            new PacketOutQueryGetRuntimeInformation(serverName, "server")
        )
            .sendOnCurrentThread()
            .syncUninterruptedly()
            .getConfiguration()
            .getValue("info", new TypeToken<RuntimeSnapshot>() {
            });
    }

    @Override
    public RuntimeSnapshot shiftServerRuntimeSnapshot(ServerInfo serverInfo) {
        Require.requireNotNull(serverInfo);
        return shiftServerRuntimeSnapshot(serverInfo.getCloudProcess().getName());
    }

    @Override
    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    @Override
    public Optional<SaveAPIService> getAPISave() {
        return Optional.ofNullable(SaveAPIService.instance.get());
    }

    @Override
    public Optional<PermissionHelper> getPermissionHelper() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void removeInternalProcess() {
        System.exit(ExitUtil.STOPPED_SUCESS);
    }

    public AbstractLoggerProvider getColouredConsoleProvider() {
        return this.colouredConsoleProvider;
    }

    public AbstractCommandManager getCommandManager() {
        return this.commandManager;
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public AddonParallelLoader getAddonParallelLoader() {
        return this.addonParallelLoader;
    }

    public ParameterManager getParameterManager() {
        return this.parameterManager;
    }

    public InternalCloudNetwork getInternalCloudNetwork() {
        return this.internalCloudNetwork;
    }

    public AbstractTaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    public AbstractChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    public NettySocketClient getNettySocketClient() {
        return this.nettySocketClient;
    }

    public SynchronizationHandler getSynchronizationHandler() {
        return this.synchronizationHandler;
    }

    public CloudProcessStartupHandler getCloudProcessStartupHandler() {
        return this.cloudProcessStartupHandler;
    }

    public CloudProcessScreenService getCloudProcessScreenService() {
        return this.cloudProcessScreenService;
    }

    public CloudConfiguration getCloudConfiguration() {
        return this.cloudConfiguration;
    }

    public ClientScreenHandler getClientScreenHandler() {
        return this.clientScreenHandler;
    }

    public long getInternalTime() {
        return this.internalTime;
    }

    public boolean isShutdown() {
        return this.shutdown;
    }

    public void setColouredConsoleProvider(ColouredConsoleProvider colouredConsoleProvider) {
        this.colouredConsoleProvider = colouredConsoleProvider;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setInternalCloudNetwork(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public void setCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public void setInternalTime(long internalTime) {
        this.internalTime = internalTime;
    }
}
