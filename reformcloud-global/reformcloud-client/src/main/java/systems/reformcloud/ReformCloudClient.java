/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.addons.AddonParallelLoader;
import systems.reformcloud.commands.*;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.LoadSuccessEvent;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.NettyHandler;
import systems.reformcloud.netty.NettySocketClient;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.packets.in.*;
import systems.reformcloud.netty.packets.sync.in.PacketInSyncScreenDisable;
import systems.reformcloud.netty.packets.sync.in.PacketInSyncScreenJoin;
import systems.reformcloud.netty.packets.sync.in.PacketInSyncUpdateClient;
import systems.reformcloud.netty.packets.sync.out.PacketOutSyncClientDisconnects;
import systems.reformcloud.netty.packets.sync.out.PacketOutSyncClientUpdateSuccess;
import systems.reformcloud.serverprocess.CloudProcessStartupHandler;
import systems.reformcloud.serverprocess.screen.CloudProcessScreenService;
import systems.reformcloud.serverprocess.screen.internal.ClientScreenHandler;
import systems.reformcloud.serverprocess.shutdown.ShutdownWorker;
import systems.reformcloud.synchronization.SynchronizationHandler;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.cloudsystem.ServerProcessManager;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.threading.scheduler.Scheduler;
import systems.reformcloud.versioneering.VersionController;
import lombok.Getter;
import lombok.Setter;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 23.10.2018
 */

@Getter
@Setter
public class ReformCloudClient implements Shutdown, Reload {
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

    private final ChannelHandler channelHandler = new ChannelHandler();
    private final NettySocketClient nettySocketClient = new NettySocketClient();
    private final Scheduler scheduler = new Scheduler(40);
    private final CloudProcessStartupHandler cloudProcessStartupHandler = new CloudProcessStartupHandler();
    private final CloudProcessScreenService cloudProcessScreenService = new CloudProcessScreenService();

    private CloudConfiguration cloudConfiguration;

    private final ClientScreenHandler clientScreenHandler;

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

        this.cloudConfiguration = new CloudConfiguration(false);
        new ReformCloudLibraryServiceProvider(loggerProvider, this.cloudConfiguration.getControllerKey(), cloudConfiguration.getEthernetAddress().getHost(), eventManager, null);

        this.registerNetworkHandlers();
        this.registerCommands();

        this.clientScreenHandler = new ClientScreenHandler();
        loggerProvider.registerLoggerHandler(clientScreenHandler);

        this.addonParallelLoader.loadAddons();

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

        Thread scheduler = new Thread(this.scheduler);
        scheduler.setPriority(Thread.MIN_PRIORITY);
        scheduler.setDaemon(true);
        scheduler.start();

        Thread startup = new Thread(this.cloudProcessStartupHandler, "Startup Handler Thread");
        startup.setPriority(Thread.MIN_PRIORITY);
        startup.setDaemon(true);
        startup.start();

        this.scheduler.runTaskRepeatAsync(new SynchronizationHandler(), 0, 200);
        this.scheduler.runTaskRepeatSync(new ShutdownWorker(), 0, 10);
        this.scheduler.runTaskRepeatAsync(this.cloudProcessScreenService, 0, 50);

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
                .registerHandler("CopyServerIntoTemplate", new PacketInCopyServerIntoTemplate())
                .registerHandler("PacketInUploadLog", new PacketInUploadLog())
                .registerHandler("JoinScreen", new PacketInSyncScreenJoin())
                .registerHandler("ScreenDisable", new PacketInSyncScreenDisable())
                .registerHandler("ReloadClient", new PacketInSyncUpdateClient())
                .registerHandler("ExecuteClientCommand", new PacketInExecuteClientCommand())
                .registerHandler("ClientProcessQueue", new PacketInGetClientProcessQueue())
                .registerHandler("RemoveProxyQueueProcess", new PacketInRemoveProxyProcessQueue())
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
            ReformCloudLibraryService.sleep(200);
        });
        this.cloudProcessScreenService.getRegisteredServerProcesses().forEach(serverProcess -> {
            serverProcess.shutdown(false);
            this.getLoggerProvider().info(this.internalCloudNetwork.getLoaded().getClient_shutdown_process()
                    .replace("%name%", serverProcess.getServerStartupInfo().getName()));
            ReformCloudLibraryService.sleep(200);
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
                System.exit(1);

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

    private NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }
}
