/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.addons.AddonParallelLoader;
import systems.reformcloud.commands.CommandExit;
import systems.reformcloud.commands.CommandHelp;
import systems.reformcloud.commands.CommandManager;
import systems.reformcloud.commands.CommandUpdate;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.LoadSuccessEvent;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.language.languages.defaults.English;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.netty.NettyHandler;
import systems.reformcloud.netty.NettySocketClient;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.packets.in.*;
import systems.reformcloud.netty.packets.sync.in.PacketInSyncScreenDisable;
import systems.reformcloud.netty.packets.sync.in.PacketInSyncScreenJoin;
import systems.reformcloud.netty.packets.sync.out.PacketOutSyncClientDisconnects;
import systems.reformcloud.serverprocess.CloudProcessStartupHandler;
import systems.reformcloud.serverprocess.screen.CloudProcessScreenService;
import systems.reformcloud.serverprocess.shutdown.ShutdownWorker;
import systems.reformcloud.synchronization.SynchronizationHandler;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.cloudsystem.ServerProcessManager;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.threading.scheduler.Scheduler;
import systems.reformcloud.versioneering.VersionController;
import lombok.Getter;
import lombok.Setter;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;

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

    private final NettyHandler nettyHandler = new NettyHandler();
    private final ChannelHandler channelHandler = new ChannelHandler();
    private final NettySocketClient nettySocketClient = new NettySocketClient();
    private final Scheduler scheduler = new Scheduler(40);
    private final CloudProcessStartupHandler cloudProcessStartupHandler = new CloudProcessStartupHandler();
    private final CloudProcessScreenService cloudProcessScreenService = new CloudProcessScreenService();

    private CloudConfiguration cloudConfiguration;

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

        this.cloudConfiguration = new CloudConfiguration();
        new ReformCloudLibraryServiceProvider(loggerProvider, this.cloudConfiguration.getControllerKey(), cloudConfiguration.getEthernetAddress().getHost(), eventManager, null);

        this.addonParallelLoader.loadAddons();

        nettyHandler.registerHandler("InitializeCloudNetwork", new PacketInInitializeInternal());
        nettyHandler.registerHandler("StartCloudServer", new PacketInStartGameServer());
        nettyHandler.registerHandler("StartProxy", new PacketInStartProxy());
        nettyHandler.registerHandler("UpdateAll", new PacketInUpdateAll());
        nettyHandler.registerHandler("ExecuteCommand", new PacketInExecuteCommand());
        nettyHandler.registerHandler("StopProcess", new PacketInStopProcess());
        nettyHandler.registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate());
        nettyHandler.registerHandler("CopyServerIntoTemplate", new PacketInCopyServerIntoTemplate());
        nettyHandler.registerHandler("PacketInUploadLog", new PacketInUploadLog());
        nettyHandler.registerHandler("JoinScreen", new PacketInSyncScreenJoin());
        nettyHandler.registerHandler("ScreenDisable", new PacketInSyncScreenDisable());

        commandManager
                .registerCommand("exit", new CommandExit())
                .registerCommand("update", new CommandUpdate())
                .registerCommand("help", new CommandHelp());

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

        Thread sync = new Thread(new SynchronizationHandler());
        sync.setPriority(Thread.MIN_PRIORITY);
        sync.setDaemon(true);
        sync.start();

        Thread shutdown = new Thread(new ShutdownWorker());
        shutdown.setPriority(Thread.MIN_PRIORITY);
        shutdown.setDaemon(true);
        shutdown.start();

        Thread log = new Thread(this.cloudProcessScreenService);
        log.setPriority(Thread.MIN_PRIORITY);
        log.setDaemon(true);
        log.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownAll, "ShutdownHook"));

        this.connect(ssl);

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        RUNNING = true;

        loggerProvider.info(this.getInternalCloudNetwork().getLoaded().getLoading_done()
                .replace("%time%", String.valueOf(System.currentTimeMillis() - time)));
        this.eventManager.callEvent(EventTargetType.LOAD_SUCCESS, new LoadSuccessEvent(true));
    }

    @Override
    public void reloadAll() {
    }

    @Override
    public void shutdownAll() {
        RUNNING = false;

        this.channelHandler.sendPacketSynchronized("ReformCloudController", new PacketOutSyncClientDisconnects());

        ReformCloudLibraryService.sleep(2000);
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
            ReformCloudLibraryService.sleep(1000);
        });

        this.addonParallelLoader.disableAddons();
        ReformCloudLibraryService.sleep(2000);
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
        return (this.internalCloudNetwork.getServerProcessManager().getUsedProxyMemory()
                + this.internalCloudNetwork.getServerProcessManager().getUsedServerMemory());
    }

    /**
     * Connects to the Controller by using {@link Boolean}
     *
     * @param ssl
     * @see NettySocketClient#connect(EthernetAddress, NettyHandler, ChannelHandler, boolean)
     */
    public void connect(final boolean ssl) {
        this.nettySocketClient.setConnections(1);

        while (this.nettySocketClient.getConnections() != -1) {
            if (this.nettySocketClient.getConnections() == 8)
                System.exit(1);

            this.nettySocketClient.connect(cloudConfiguration.getEthernetAddress(), this.nettyHandler, this.channelHandler, ssl);

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
}
