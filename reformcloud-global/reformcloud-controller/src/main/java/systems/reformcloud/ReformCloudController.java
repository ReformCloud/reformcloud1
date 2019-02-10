/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.addons.AddonParallelLoader;
import systems.reformcloud.api.*;
import systems.reformcloud.api.documentation.RestAPIDocumentation;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.database.DatabaseProvider;
import systems.reformcloud.database.statistics.StatisticsProvider;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.LoadSuccessEvent;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.netty.NettyHandler;
import systems.reformcloud.netty.NettySocketServer;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.in.*;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.out.PacketOutStopProcess;
import systems.reformcloud.netty.out.PacketOutUpdateAll;
import systems.reformcloud.netty.sync.in.PacketInSyncClientDisconnects;
import systems.reformcloud.netty.sync.in.PacketInSyncClientReloadSuccess;
import systems.reformcloud.netty.sync.in.PacketInSyncScreenUpdate;
import systems.reformcloud.netty.sync.in.PacketInSyncUpdateClientInfo;
import systems.reformcloud.netty.sync.out.PacketOutSyncUpdateClient;
import systems.reformcloud.startup.CloudProcessOfferService;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.screen.ScreenSessionProvider;
import systems.reformcloud.utility.threading.scheduler.Scheduler;
import systems.reformcloud.utility.time.TimeSync;
import systems.reformcloud.versioneering.VersionController;
import systems.reformcloud.web.ReformWebServer;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.commands.*;

import javax.management.InstanceAlreadyExistsException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@Getter
@Setter
public class ReformCloudController implements Shutdown, Reload {
    public static volatile boolean RUNNING = false;

    @Getter
    private static ReformCloudController instance;

    private CommandManager commandManager;
    private LoggerProvider loggerProvider;
    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private final StatisticsProvider statisticsProvider = new StatisticsProvider();
    private final ChannelHandler channelHandler = new ChannelHandler();
    private final Scheduler scheduler = new Scheduler(40);
    private final AddonParallelLoader addonParallelLoader = new AddonParallelLoader();
    private final CloudProcessOfferService cloudProcessOfferService = new CloudProcessOfferService();
    private final EventManager eventManager = new EventManager();
    private final ScreenSessionProvider screenSessionProvider = new ScreenSessionProvider();

    private List<UUID> uuid = new ArrayList<>();
    private List<DatabaseProvider> databaseProviders = new ArrayList<>();

    private ReformWebServer reformWebServer;
    private final NettySocketServer nettySocketServer;
    private ReformCloudLibraryServiceProvider reformCloudLibraryServiceProvider;
    private CloudConfiguration cloudConfiguration;

    private Thread shutdownHook;

    /**
     * Creates a new instance of the ReformCloudController and prepares all needed handlers
     *
     * @see LoggerProvider
     * @see CommandManager
     *
     * @param loggerProvider    Main Cloud logger, will be used everywhere
     * @param commandManager            Main CommandManager to manage all available commands
     * @param ssl                       If this is {@code true} the cloud will use a self-
     *                                  signed certificate
     * @param time                      Startup time for start time
     * @throws Throwable                If an error occurs while starting CloudSystem
     *                                  the error will be thrown here
     */
    public ReformCloudController(LoggerProvider loggerProvider, CommandManager commandManager, boolean ssl, long time) throws Throwable {
        if (instance == null)
            instance = this;
        else
            StringUtil.printError(loggerProvider, "ReformCloudController Instance already exists", new LoadException(new InstanceAlreadyExistsException()));

        this.loggerProvider = loggerProvider;
        this.commandManager = commandManager;

        cloudConfiguration = new CloudConfiguration();
        this.reformCloudLibraryServiceProvider = new ReformCloudLibraryServiceProvider(loggerProvider,
                this.cloudConfiguration.getControllerKey(), null, eventManager, cloudConfiguration.getLoadedLang());
        this.internalCloudNetwork.setLoaded(ReformCloudLibraryServiceProvider.getInstance().getLoaded());

        cloudConfiguration.getClients().forEach(client -> {
            loggerProvider.info(this.getLoadedLanguage().getController_loading_client()
                    .replace("%name%", client.getName())
                    .replace("%ip%", client.getIp()));
            this.internalCloudNetwork.getClients().put(client.getName(), client);
        });
        cloudConfiguration.getServerGroups().forEach(group -> {
            loggerProvider.info(this.getLoadedLanguage().getController_loading_server()
                    .replace("%name%", group.getName())
                    .replace("%clients%", group.getClients().toString()));
            this.internalCloudNetwork.getServerGroups().put(group.getName(), group);
        });
        cloudConfiguration.getProxyGroups().forEach(proxy -> {
            loggerProvider.info(this.getLoadedLanguage().getController_loading_proxy()
                    .replace("%name%", proxy.getName())
                    .replace("%clients%", proxy.getClients().toString()));
            this.internalCloudNetwork.getProxyGroups().put(proxy.getName(), proxy);
        });

        this.databaseProviders.add(statisticsProvider);

        statisticsProvider.load();
        statisticsProvider.setLastStartup();
        statisticsProvider.addStartup();

        if (StringUtil.USER_NAME.equalsIgnoreCase("root")
                && StringUtil.OS_NAME.toLowerCase().contains("linux")) {
            statisticsProvider.addRootStartup();
        }

        if (cloudConfiguration.getWebAddress() != null)
            reformWebServer = new ReformWebServer(cloudConfiguration.getWebAddress(), ssl,
                    !cloudConfiguration.getCertFile().equalsIgnoreCase(StringUtil.NULL) ?
                            new File(cloudConfiguration.getCertFile()) : null,
                    !cloudConfiguration.getKeyFile().equalsIgnoreCase(StringUtil.NULL) ?
                            new File(cloudConfiguration.getKeyFile()) : null);

        this.preparePacketHandlers();
        this.prepareCommands();

        this.reformCloudLibraryServiceProvider.setInternalCloudNetwork(internalCloudNetwork);

        this.addonParallelLoader.loadAddons();

        this.nettySocketServer = new NettySocketServer(ssl, this.cloudConfiguration.getNettyAddress(),
                !cloudConfiguration.getCertFile().equalsIgnoreCase(StringUtil.NULL) ?
                        new File(cloudConfiguration.getCertFile()) : null,
                !cloudConfiguration.getKeyFile().equalsIgnoreCase(StringUtil.NULL) ?
                        new File(cloudConfiguration.getKeyFile()) : null);

        Thread thread = new Thread(this.scheduler);
        thread.setDaemon(true);
        thread.start();

        this.scheduler.runTaskRepeatAsync(() -> {
            this.databaseProviders.forEach(databaseProvider -> databaseProvider.save());
        }, 0, 150000);
        this.scheduler.runTaskRepeatAsync(cloudProcessOfferService, 0, 250);
        this.scheduler.runTaskRepeatAsync(new TimeSync(), 0, 250);

        this.shutdownHook = new Thread(this::shutdownAll, "Shutdown-Hook");
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        RUNNING = true;

        loggerProvider.info(this.getLoadedLanguage().getLoading_done()
                    .replace("%time%", String.valueOf(System.currentTimeMillis() - time)));
        this.eventManager.callEvent(EventTargetType.LOAD_SUCCESS, new LoadSuccessEvent(true));
    }

    /**
     * Prepares all packet Handlers
     *
     * @see NettyHandler#registerHandler(String, NetworkInboundHandler)
     */
    private void preparePacketHandlers() {
        this.getNettyHandler()
                .registerHandler("AuthSuccess", new PacketInAuthSuccess())
                .registerHandler("SendControllerConsoleMessage", new PacketInSendControllerConsoleMessage())
                .registerHandler("ProcessAdd", new PacketInAddProcess())
                .registerHandler("ProcessRemove", new PacketInRemoveProcess())
                .registerHandler("UpdateInternalCloudNetwork", new PacketInUpdateInternalCloudNetwork())
                .registerHandler("InternalProcessRemove", new PacketInRemoveInternalProcess())
                .registerHandler("CommandExecute", new PacketInCommandExecute())
                .registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate())
                .registerHandler("LoginPlayer", new PacketInLoginPlayer())
                .registerHandler("LogoutPlayer", new PacketInLogoutPlayer())
                .registerHandler("PlayerAccepted", new PacketInPlayerAccepted())
                .registerHandler("DispatchCommandLine", new PacketInDispatchConsoleCommand())
                .registerHandler("StartGameProcess", new PacketInStartGameProcess())
                .registerHandler("ServerDisable", new PacketInServerDisable())
                .registerHandler("StartProxyProcess", new PacketInStartProxyProcess())
                .registerHandler("ProcessLog", new PacketInGetLog())
                .registerHandler("UpdateClientInfo", new PacketInSyncUpdateClientInfo())
                .registerHandler("ClientDisconnects", new PacketInSyncClientDisconnects())
                .registerHandler("ScreenUpdate", new PacketInSyncScreenUpdate())
                .registerHandler("ClientReloadSuccess", new PacketInSyncClientReloadSuccess())
                .registerHandler("ClientProcessQueue", new PacketInClientProcessQueue())
                .registerHandler("ProxyInfoUpdate", new PacketInProxyInfoUpdate());

        if (this.reformWebServer != null) {
            this.reformWebServer.getWebHandlerAdapter()
                    .registerHandler("/api/documentation", new RestAPIDocumentation())

                    .registerHandler("/api/auth", new RestAPIAuth())

                    .registerHandler("/api/permissions", new RestAPIPermissionCheck())

                    .registerHandler("/api/connected/list/servers", new RestAPIServerList())
                    .registerHandler("/api/connected/list/proxies", new RestAPIProxyList())
                    .registerHandler("/api/connected/list/clients", new RestAPIClientList())

                    .registerHandler("/api/groups/list/proxies", new RestAPIProxyGroupList())
                    .registerHandler("/api/groups/list/servers", new RestAPIServerGroupList())
                    .registerHandler("/api/groups/list/clients", new RestAPIClients())

                    .registerHandler("/api/start/server", new RestAPIStartGameserver())
                    .registerHandler("/api/start/proxy", new RestAPIStartProxy())

                    .registerHandler("/api/stop/server", new RestAPIStopServer())
                    .registerHandler("/api/stop/proxy", new RestAPIStartProxy());
        }
    }

    /**
     * Prepares all commands
     *
     * @see CommandManager#registerCommand(Command)
     */
    private void prepareCommands() {
        this.commandManager
                .registerCommand(new CommandExit())
                .registerCommand(new CommandHelp())
                .registerCommand(new CommandExecute())
                .registerCommand(new CommandProcess())
                .registerCommand(new CommandReload())
                .registerCommand(new CommandCreate())
                .registerCommand(new CommandDelete())
                .registerCommand(new CommandClear())
                .registerCommand(new CommandInfo())
                .registerCommand(new CommandCopy())
                .registerCommand(new CommandUpdate())
                .registerCommand(new CommandWhitelist())
                .registerCommand(new CommandLog())
                .registerCommand(new CommandAddons())
                .registerCommand(new CommandScreen())
                .registerCommand(new CommandVersion())
                .registerCommand(new CommandListGroups())
                .registerCommand(new CommandWebPermissions());
    }

    /**
     * Reloads the ReformCloudController
     *
     * @throws Throwable
     */
    @Override
    public void reloadAll() throws Throwable {
        this.loggerProvider.info(this.getLoadedLanguage().getController_reload());

        this.updateAllConnectedClients();

        this.cloudConfiguration = null;
        this.getNettyHandler().clearHandlers();
        this.commandManager.clearCommands();

        this.internalCloudNetwork.getServerGroups().clear();
        this.internalCloudNetwork.getProxyGroups().clear();
        this.internalCloudNetwork.getClients().clear();

        this.addonParallelLoader.disableAddons();
        this.eventManager.unregisterAllListener();

        Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
        this.shutdownHook = null;

        this.cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.getClients().forEach(client -> {
            loggerProvider.info(this.getLoadedLanguage().getController_loading_client()
                    .replace("%name%", client.getName())
                    .replace("%ip%", client.getIp()));
            this.internalCloudNetwork.getClients().put(client.getName(), client);
        });
        cloudConfiguration.getServerGroups().forEach(group -> {
            loggerProvider.info(this.getLoadedLanguage().getController_loading_server()
                    .replace("%name%", group.getName())
                    .replace("%clients%", group.getClients().toString()));
            this.internalCloudNetwork.getServerGroups().put(group.getName(), group);
        });
        cloudConfiguration.getProxyGroups().forEach(proxy -> {
            loggerProvider.info(this.getLoadedLanguage().getController_loading_proxy()
                    .replace("%name%", proxy.getName())
                    .replace("%clients%", proxy.getClients().toString()));
            this.internalCloudNetwork.getProxyGroups().put(proxy.getName(), proxy);
        });

        this.addonParallelLoader.loadAddons();

        final Language language = new LanguageManager(cloudConfiguration.getLoadedLang()).getLoaded();

        this.internalCloudNetwork.setLoaded(language);
        ReformCloudLibraryServiceProvider.getInstance().setLoaded(language);
        this.preparePacketHandlers();
        this.prepareCommands();

        this.shutdownHook = new Thread(this::shutdownAll, "Shutdown-Hook");
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);

        this.channelHandler.sendToAllSynchronized(new PacketOutUpdateAll(this.internalCloudNetwork));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(this.internalCloudNetwork);

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        this.loggerProvider.info(this.getLoadedLanguage().getGlobal_reload_done());
    }

    private void updateAllConnectedClients() {
        this.internalCloudNetwork
                .getClients()
                .values()
                .stream()
                .filter(client -> client.getClientInfo() != null)
                .forEach(client -> this.channelHandler.sendPacketAsynchronous(client.getName(), new PacketOutSyncUpdateClient()));
    }

    /**
     * Shutdowns the Controller
     */
    @Override
    public void shutdownAll() {
        RUNNING = false;

        this.internalCloudNetwork.getServerProcessManager().getAllRegisteredServerProcesses().forEach(e -> {
            this.channelHandler.sendPacketSynchronized(e.getCloudProcess().getClient(),
                    new PacketOutStopProcess(e.getCloudProcess().getName()));
            this.loggerProvider.info(this.getLoadedLanguage().getController_servprocess_stopped()
                        .replace("%name%", e.getCloudProcess().getName()));
            ReformCloudLibraryService.sleep(100);
        });

        this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses().forEach(e -> {
            this.channelHandler.sendPacketSynchronized(e.getCloudProcess().getClient(),
                    new PacketOutStopProcess(e.getCloudProcess().getName()));
            this.loggerProvider.info(this.getLoadedLanguage().getController_proxyprocess_stopped()
                    .replace("%name%", e.getCloudProcess().getName()));
            ReformCloudLibraryService.sleep(100);
        });

        this.loggerProvider.info(this.getLoadedLanguage().getWaiting_for_tasks());

        if (this.reformWebServer != null)
            this.reformWebServer.shutdown();

        this.nettySocketServer.close();

        ReformCloudLibraryService.sleep(1000);
    }

    public void checkForUpdates() {
        if (VersionController.isVersionAvailable()) {
            loggerProvider.warn(this.getLoadedLanguage().getVersion_available());
        } else {
            loggerProvider.info(this.getLoadedLanguage().getVersion_update());
        }
    }

    public Client getBestClient(List<String> clients, int memory) {
        Client best = null;
        List<Client> available = this.internalCloudNetwork.getClients()
                .values()
                .stream()
                .filter(e -> this.channelHandler.isChannelRegistered(e.getName())
                        && e.getClientInfo() != null
                        && clients.contains(e.getName()))
                .collect(Collectors.toList());

        if (available.size() == 0)
            return null;

        for (Client client : available) {
            if (client.getClientInfo() == null || !client.getClientInfo().isReady() || client.getClientInfo().getMaxMemory() < memory)
                continue;

            int maxMemory = client.getClientInfo().getMaxMemory();
            int usedMemory = client.getClientInfo().getUsedMemory();
            int freeMemory = maxMemory - usedMemory;
            if (best == null) {
                if (freeMemory >= memory) {
                    best = client;
                }
            } else if ((best.getClientInfo().getMaxMemory() - best.getClientInfo().getUsedMemory()) < freeMemory) {
                best = client;
            }

            if (best != null && available.size() > 1) {
                if (best.getClientInfo().getCpuUsage() > 70 && best.getClientInfo().getCpuUsage() > client.getClientInfo().getCpuUsage()
                        && client.getClientInfo().getCpuUsage() < 70) {
                    best = client;
                }
            }
        }

        return best;
    }

    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    public Language getLoadedLanguage() {
        return this.internalCloudNetwork.getLoaded();
    }
}
