/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import systems.reformcloud.addons.AddonParallelLoader;
import systems.reformcloud.api.APIService;
import systems.reformcloud.api.DefaultPlayerProvider;
import systems.reformcloud.api.RestAPIAuth;
import systems.reformcloud.api.RestAPIClientList;
import systems.reformcloud.api.RestAPIClients;
import systems.reformcloud.api.RestAPIGetOfflinePlayer;
import systems.reformcloud.api.RestAPIPermissionCheck;
import systems.reformcloud.api.RestAPIProxyGroupList;
import systems.reformcloud.api.RestAPIProxyList;
import systems.reformcloud.api.RestAPIServerGroupList;
import systems.reformcloud.api.RestAPIServerList;
import systems.reformcloud.api.RestAPIStartGameserver;
import systems.reformcloud.api.RestAPIStartProxy;
import systems.reformcloud.api.RestAPIStopProxy;
import systems.reformcloud.api.RestAPIStopServer;
import systems.reformcloud.api.deployment.incoming.RestAPIDeploymentService;
import systems.reformcloud.api.deployment.outgoing.RestAPIDownloadService;
import systems.reformcloud.api.documentation.RestAPIDocumentation;
import systems.reformcloud.api.ingame.command.IngameCommandMangerImpl;
import systems.reformcloud.api.network.event.EventAdapter;
import systems.reformcloud.api.player.PlayerProvider;
import systems.reformcloud.api.save.SaveAPIImpl;
import systems.reformcloud.api.save.SaveAPIService;
import systems.reformcloud.commands.CommandAddons;
import systems.reformcloud.commands.CommandAssignment;
import systems.reformcloud.commands.CommandClear;
import systems.reformcloud.commands.CommandCopy;
import systems.reformcloud.commands.CommandCreate;
import systems.reformcloud.commands.CommandDelete;
import systems.reformcloud.commands.CommandDeploy;
import systems.reformcloud.commands.CommandDeveloper;
import systems.reformcloud.commands.CommandExecute;
import systems.reformcloud.commands.CommandExit;
import systems.reformcloud.commands.CommandHelp;
import systems.reformcloud.commands.CommandInfo;
import systems.reformcloud.commands.CommandInstall;
import systems.reformcloud.commands.CommandListGroups;
import systems.reformcloud.commands.CommandLog;
import systems.reformcloud.commands.CommandManager;
import systems.reformcloud.commands.CommandProcess;
import systems.reformcloud.commands.CommandReload;
import systems.reformcloud.commands.CommandScreen;
import systems.reformcloud.commands.CommandUpdate;
import systems.reformcloud.commands.CommandUpload;
import systems.reformcloud.commands.CommandVersion;
import systems.reformcloud.commands.CommandWebPermissions;
import systems.reformcloud.commands.CommandWhitelist;
import systems.reformcloud.commands.ingame.IngameCommandManger;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.database.DatabaseProvider;
import systems.reformcloud.database.DatabaseSaver;
import systems.reformcloud.database.player.PlayerDatabase;
import systems.reformcloud.database.statistics.StatisticsProvider;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.event.events.ProxyInfoUpdateEvent;
import systems.reformcloud.event.events.ReloadDoneEvent;
import systems.reformcloud.event.events.ServerInfoUpdateEvent;
import systems.reformcloud.event.events.StartedEvent;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
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
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketServer;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.in.PacketInAddProcess;
import systems.reformcloud.network.in.PacketInAuthSuccess;
import systems.reformcloud.network.in.PacketInClientProcessQueue;
import systems.reformcloud.network.in.PacketInCommandExecute;
import systems.reformcloud.network.in.PacketInConnectPlayer;
import systems.reformcloud.network.in.PacketInCreateClient;
import systems.reformcloud.network.in.PacketInCreateProxyGroup;
import systems.reformcloud.network.in.PacketInCreateServerGroup;
import systems.reformcloud.network.in.PacketInCreateWebUser;
import systems.reformcloud.network.in.PacketInDispatchConsoleCommand;
import systems.reformcloud.network.in.PacketInExecuteCommandSilent;
import systems.reformcloud.network.in.PacketInGetControllerTemplate;
import systems.reformcloud.network.in.PacketInGetLog;
import systems.reformcloud.network.in.PacketInIconSizeIncorrect;
import systems.reformcloud.network.in.PacketInKickPlayer;
import systems.reformcloud.network.in.PacketInLoginPlayer;
import systems.reformcloud.network.in.PacketInLogoutPlayer;
import systems.reformcloud.network.in.PacketInProxyInfoUpdate;
import systems.reformcloud.network.in.PacketInRemoveInternalProcess;
import systems.reformcloud.network.in.PacketInRemoveProcess;
import systems.reformcloud.network.in.PacketInSendControllerConsoleMessage;
import systems.reformcloud.network.in.PacketInSendPlayerMessage;
import systems.reformcloud.network.in.PacketInServerInfoUpdate;
import systems.reformcloud.network.in.PacketInStartGameProcess;
import systems.reformcloud.network.in.PacketInStartProxyProcess;
import systems.reformcloud.network.in.PacketInStopProcess;
import systems.reformcloud.network.in.PacketInUpdateControllerTemplate;
import systems.reformcloud.network.in.PacketInUpdateInternalCloudNetwork;
import systems.reformcloud.network.in.PacketInUpdateOfflinePlayer;
import systems.reformcloud.network.in.PacketInUpdateOnlinePlayer;
import systems.reformcloud.network.in.PacketInUpdateServerGroup;
import systems.reformcloud.network.in.PacketInUpdateServerTempStats;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.out.PacketOutProxyInfoUpdate;
import systems.reformcloud.network.out.PacketOutServerInfoUpdate;
import systems.reformcloud.network.out.PacketOutStartGameServer;
import systems.reformcloud.network.out.PacketOutStartProxy;
import systems.reformcloud.network.out.PacketOutStopProcess;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.query.in.PacketInQueryGetOnlinePlayer;
import systems.reformcloud.network.query.in.PacketInQueryGetPlayer;
import systems.reformcloud.network.query.in.PacketInQueryPlayerAccepted;
import systems.reformcloud.network.query.in.PacketInQueryStartDevProcess;
import systems.reformcloud.network.sync.in.PacketInSyncClientDisconnects;
import systems.reformcloud.network.sync.in.PacketInSyncClientReloadSuccess;
import systems.reformcloud.network.sync.in.PacketInSyncExceptionThrown;
import systems.reformcloud.network.sync.in.PacketInSyncNameToUUID;
import systems.reformcloud.network.sync.in.PacketInSyncScreenUpdate;
import systems.reformcloud.network.sync.in.PacketInSyncUpdateClientInfo;
import systems.reformcloud.network.sync.out.PacketOutSyncUpdateClient;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.startup.CloudProcessOfferService;
import systems.reformcloud.utility.ExitUtil;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.defaults.DefaultCloudService;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.screen.ScreenSessionProvider;
import systems.reformcloud.utility.threading.TaskScheduler;
import systems.reformcloud.utility.time.TimeSync;
import systems.reformcloud.versioneering.VersionController;
import systems.reformcloud.web.ReformWebServer;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class ReformCloudController implements Serializable, Shutdown, Reload, APIService {

    private static volatile boolean RUNNING = false;

    private static ReformCloudController instance;

    private CommandManager commandManager;

    private LoggerProvider loggerProvider;

    private InternalCloudNetwork internalCloudNetwork = new InternalCloudNetwork();

    private final StatisticsProvider statisticsProvider = new StatisticsProvider();

    private final PlayerDatabase playerDatabase = new PlayerDatabase();

    private final ChannelHandler channelHandler;

    private final AddonParallelLoader addonParallelLoader = new AddonParallelLoader();

    private final CloudProcessOfferService cloudProcessOfferService = new CloudProcessOfferService();

    private final EventManager eventManager = new EventManager();

    private final ScreenSessionProvider screenSessionProvider = new ScreenSessionProvider();

    private final IngameCommandManger ingameCommandManger = new IngameCommandMangerImpl();

    private final TaskScheduler taskScheduler;

    private List<UUID> uuid = new ArrayList<>();

    private List<DatabaseProvider> databaseProviders = new ArrayList<>();

    private final Map<String, ClientInfo> clientInfos = new HashMap<>();

    private ReformWebServer reformWebServer;

    private final NettySocketServer nettySocketServer;

    private ReformCloudLibraryServiceProvider reformCloudLibraryServiceProvider;

    private CloudConfiguration cloudConfiguration;

    private Thread shutdownHook;

    /**
     * Creates a new instance of the ReformCloudController and prepares all needed handlers
     *
     * @param loggerProvider Main Cloud logger, will be used everywhere
     * @param commandManager Main CommandManager to manage all available commands
     * @param ssl If this is {@code true} the cloud will use a self- signed certificate
     * @param time Startup time for start time
     * @throws Throwable If an error occurs while starting CloudSystem the error will be thrown here
     */
    public ReformCloudController(LoggerProvider loggerProvider, CommandManager commandManager,
        boolean ssl, long time) throws Throwable {
        if (instance == null) {
            instance = this;
        } else {
            StringUtil.printError(loggerProvider, "ReformCloudController Instance already exists",
                new LoadException(new InstanceAlreadyExistsException()));
        }

        this.loggerProvider = loggerProvider;
        this.commandManager = commandManager;

        cloudConfiguration = new CloudConfiguration();
        this.reformCloudLibraryServiceProvider = new ReformCloudLibraryServiceProvider(
            loggerProvider,
            this.cloudConfiguration.getControllerKey(), null, eventManager,
            cloudConfiguration.getLoadedLang());
        this.internalCloudNetwork
            .setLoaded(ReformCloudLibraryServiceProvider.getInstance().getLoaded());

        this.taskScheduler = ReformCloudLibraryServiceProvider.getInstance().getTaskScheduler();
        this.channelHandler = new ChannelHandler();

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

        this.internalCloudNetwork.getServerGroups().forEach((k, v) ->
            this.getAllRegisteredServers(k).forEach(e -> e.setServerGroup(v))
        );

        this.internalCloudNetwork.getProxyGroups().forEach((k, v) ->
            this.getAllRegisteredProxies(k).forEach(e -> e.setProxyGroup(v))
        );

        this.databaseProviders.add(statisticsProvider);

        statisticsProvider.load();
        statisticsProvider.setLastStartup();
        statisticsProvider.addStartup();

        if (StringUtil.USER_NAME.equalsIgnoreCase("root")
            && StringUtil.OS_NAME.toLowerCase().contains("linux")) {
            statisticsProvider.addRootStartup();
        }

        new EventAdapter();

        if (cloudConfiguration.getWebAddress() != null) {
            reformWebServer = new ReformWebServer(cloudConfiguration.getWebAddress(), ssl,
                !cloudConfiguration.getCertFile().equalsIgnoreCase(StringUtil.NULL) ?
                    new File(cloudConfiguration.getCertFile()) : null,
                !cloudConfiguration.getKeyFile().equalsIgnoreCase(StringUtil.NULL) ?
                    new File(cloudConfiguration.getKeyFile()) : null);
        }

        this.preparePacketHandlers();
        this.prepareCommands();

        this.reformCloudLibraryServiceProvider.setInternalCloudNetwork(internalCloudNetwork);

        this.addonParallelLoader.loadAddons();

        this.nettySocketServer = new NettySocketServer(ssl,
            this.cloudConfiguration.getNettyAddress(),
            !cloudConfiguration.getCertFile().equalsIgnoreCase(StringUtil.NULL) ?
                new File(cloudConfiguration.getCertFile()) : null,
            !cloudConfiguration.getKeyFile().equalsIgnoreCase(StringUtil.NULL) ?
                new File(cloudConfiguration.getKeyFile()) : null);

        this.taskScheduler
            .schedule(new DatabaseSaver(), -1, TimeUnit.SECONDS.toMillis(30))
            .schedule(new TimeSync(), -1, TimeUnit.SECONDS.toMillis(1));

        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(this.cloudProcessOfferService);

        this.shutdownHook = new Thread(this::shutdownAll, "Shutdown-Hook");
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        RUNNING = true;

        SaveAPIService.instance.set(new SaveAPIImpl());
        APIService.instance.set(this);
        new DefaultCloudService(this);
        DefaultPlayerProvider.instance.set(new PlayerProvider());

        loggerProvider.info(this.getLoadedLanguage().getLoading_done()
            .replace("%time%", String.valueOf(System.currentTimeMillis() - time)));

        this.eventManager.fire(new StartedEvent());
    }

    public static ReformCloudController getInstance() {
        return ReformCloudController.instance;
    }

    /**
     * Prepares all packet Handlers
     *
     * @see NettyHandler#registerHandler(String, NetworkInboundHandler)
     */
    private void preparePacketHandlers() {
        this.getNettyHandler()
            //Internal
            .registerHandler("UpdateInternalCloudNetwork", new PacketInUpdateInternalCloudNetwork())
            .registerHandler("SendControllerConsoleMessage",
                new PacketInSendControllerConsoleMessage())
            .registerHandler("ProcessLog", new PacketInGetLog())
            .registerHandler("ScreenUpdate", new PacketInSyncScreenUpdate())

            //Authentication
            .registerHandler("AuthSuccess", new PacketInAuthSuccess())

            //Process management
            .registerHandler("ProcessAdd", new PacketInAddProcess())
            .registerHandler("ProcessRemove", new PacketInRemoveProcess())
            .registerHandler("InternalProcessRemove", new PacketInRemoveInternalProcess())
            .registerHandler("ServerInfoUpdate", new PacketInServerInfoUpdate())
            .registerHandler("ProxyInfoUpdate", new PacketInProxyInfoUpdate())
            .registerHandler("StartGameProcess", new PacketInStartGameProcess())
            .registerHandler("StartProxyProcess", new PacketInStartProxyProcess())

            //Development
            .registerQueryHandler("QueryStartQueuedProcess", new PacketInQueryStartDevProcess())
            .registerQueryHandler("ExecuteCommandSilent", new PacketInExecuteCommandSilent())
            .registerHandler("UpdateServerGroup", new PacketInUpdateServerGroup())
            .registerHandler("CreateClient", new PacketInCreateClient())
            .registerHandler("CreateProxyGroup", new PacketInCreateProxyGroup())
            .registerHandler("CreateServerGroup", new PacketInCreateServerGroup())
            .registerHandler("CreateWebUser", new PacketInCreateWebUser())
            .registerHandler("StopProcess", new PacketInStopProcess())

            //Client communication
            .registerHandler("UpdateClientInfo", new PacketInSyncUpdateClientInfo())
            .registerHandler("ClientDisconnects", new PacketInSyncClientDisconnects())
            .registerHandler("ClientReloadSuccess", new PacketInSyncClientReloadSuccess())
            .registerHandler("ClientProcessQueue", new PacketInClientProcessQueue())

            //Client template management
            .registerHandler("GetControllerTemplate", new PacketInGetControllerTemplate())
            .registerHandler("UpdateControllerTemplate", new PacketInUpdateControllerTemplate())

            //Statistics
            .registerHandler("UpdateTempServerStats", new PacketInUpdateServerTempStats())

            //Exception handling
            .registerHandler("ExceptionThrown", new PacketInSyncExceptionThrown())

            //Extras
            .registerHandler("CommandExecute", new PacketInCommandExecute())
            .registerHandler("DispatchCommandLine", new PacketInDispatchConsoleCommand())
            .registerHandler("IconSizeIncorrect", new PacketInIconSizeIncorrect())
            .registerHandler("NameToUUID", new PacketInSyncNameToUUID())

            //Player Handlers
            .registerHandler("UpdateOnlinePlayer", new PacketInUpdateOnlinePlayer())
            .registerHandler("UpdateOfflinePlayer", new PacketInUpdateOfflinePlayer())
            .registerHandler("LoginPlayer", new PacketInLoginPlayer())
            .registerHandler("LogoutPlayer", new PacketInLogoutPlayer())

            //PlayerProvider Handlers
            .registerHandler("ConnectPlayer", new PacketInConnectPlayer())
            .registerHandler("SendPlayerMessage", new PacketInSendPlayerMessage())
            .registerHandler("KickPlayer", new PacketInKickPlayer())

            //Player Query Handlers
            .registerQueryHandler("QueryGetPlayer", new PacketInQueryGetPlayer())
            .registerQueryHandler("QueryCheckPlayer", new PacketInQueryPlayerAccepted())
            .registerQueryHandler("QueryGetOnlinePlayer", new PacketInQueryGetOnlinePlayer());

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

                .registerHandler("/api/player/get", new RestAPIGetOfflinePlayer())

                .registerHandler("/api/deploy", new RestAPIDeploymentService())
                .registerHandler("/api/download", new RestAPIDownloadService())

                .registerHandler("/api/stop/server", new RestAPIStopServer())
                .registerHandler("/api/stop/proxy", new RestAPIStopProxy());
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
            .registerCommand(new CommandDeveloper())
            .registerCommand(new CommandUpload())
            .registerCommand(new CommandAssignment())
            .registerCommand(new CommandInstall())
            .registerCommand(new CommandDeploy())
            .registerCommand(new CommandWebPermissions());
    }

    /**
     * Reloads the ReformCloudController
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
        this.eventManager.unregisterAll();

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

        this.internalCloudNetwork.getServerGroups().forEach((k, v) ->
            this.getAllRegisteredServers(k).forEach(e -> e.setServerGroup(v))
        );

        this.internalCloudNetwork.getProxyGroups().forEach((k, v) ->
            this.getAllRegisteredProxies(k).forEach(e -> e.setProxyGroup(v))
        );

        this.addonParallelLoader.loadAddons();

        final Language language = new LanguageManager(cloudConfiguration.getLoadedLang())
            .getLoaded();

        this.internalCloudNetwork.setLoaded(language);
        ReformCloudLibraryServiceProvider.getInstance().setLoaded(language);
        this.preparePacketHandlers();
        this.prepareCommands();

        this.shutdownHook = new Thread(this::shutdownAll, "Shutdown-Hook");
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);

        this.channelHandler
            .sendToAllSynchronized(new PacketOutUpdateAll(this.internalCloudNetwork));
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(this.internalCloudNetwork);

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        this.loggerProvider.info(this.getLoadedLanguage().getGlobal_reload_done());
        this.eventManager.fire(new ReloadDoneEvent());
    }

    private void updateAllConnectedClients() {
        this.internalCloudNetwork
            .getClients()
            .values()
            .stream()
            .filter(client -> client.getClientInfo() != null)
            .forEach(client -> this.channelHandler
                .sendPacketAsynchronous(client.getName(), new PacketOutSyncUpdateClient()));
    }

    /**
     * Shutdowns the Controller
     */
    @Override
    public void shutdownAll() {
        RUNNING = false;

        this.taskScheduler.close();
        this.statisticsProvider.save();

        this.internalCloudNetwork.getServerProcessManager().getAllRegisteredServerProcesses()
            .forEach(e -> this.loggerProvider
                .info(this.getLoadedLanguage().getController_servprocess_stopped()
                    .replace("%name%", e.getCloudProcess().getName())));

        this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses()
            .forEach(e -> this.loggerProvider
                .info(this.getLoadedLanguage().getController_proxyprocess_stopped()
                    .replace("%name%", e.getCloudProcess().getName())));

        this.loggerProvider.info(this.getLoadedLanguage().getWaiting_for_tasks());

        if (this.reformWebServer != null) {
            this.reformWebServer.shutdown();
        }

        this.nettySocketServer.close();
        this.addonParallelLoader.disableAddons();

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
                && e.getClientInfo().isReady()
                && clients.contains(e.getName()))
            .collect(Collectors.toList());

        if (available.size() == 0) {
            return null;
        }

        for (Client client : available) {
            if (client.getClientInfo() == null || !this.clientInfos.get(client.getName()).isReady()
                || this.clientInfos.get(client.getName()).getMaxMemory() < memory) {
                continue;
            }

            int maxMemory = this.clientInfos.get(client.getName()).getMaxMemory();
            int usedMemory = this.clientInfos.get(client.getName()).getUsedMemory();
            int freeMemory = maxMemory - usedMemory;
            if (best == null) {
                if (freeMemory >= memory) {
                    best = client;
                }
            } else if ((this.clientInfos.get(best.getName()).getMaxMemory() - this.clientInfos
                .get(best.getName()).getUsedMemory()) < freeMemory) {
                best = client;
            }

            if (best != null && available.size() > 1) {
                if (this.clientInfos.get(best.getName()).getCpuUsage() > 70
                    && this.clientInfos.get(best.getName()).getCpuUsage()
                    > this.clientInfos.get(client.getName()).getCpuUsage()
                    && client.getClientInfo().getCpuUsage() < 70) {
                    best = client;
                }
            }
        }

        return best;
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
        final Client client = ReformCloudController.getInstance()
            .getBestClient(serverGroup.getClients(), serverGroup.getMemory());

        if (client == null) {
            return;
        }

        final Collection<String> servers = ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager().getOnlineServers(serverGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance()
            .getCloudProcessOfferService().getWaiting(serverGroup.getName());

        final int waitingAndOnline = servers.size() + waiting.size();
        final String id = Integer.toString(
            ReformCloudController.getInstance().getCloudProcessOfferService()
                .nextServerID(serverGroup.getName()));
        final String name =
            serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration()
                .getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService()
            .registerID(serverGroup.getName(), name, Integer.valueOf(id));

        if (serverGroup.getMaxOnline() > waitingAndOnline || serverGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler()
                .sendPacketAsynchronous(client.getName(),
                    new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), preConfig,
                        id)
                );
        }
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig,
        final String template) {
        final Client client = ReformCloudController.getInstance()
            .getBestClient(serverGroup.getClients(), serverGroup.getMemory());

        if (client == null) {
            return;
        }

        final Collection<String> servers = ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager().getOnlineServers(serverGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance()
            .getCloudProcessOfferService().getWaiting(serverGroup.getName());

        final int waitingAndOnline = servers.size() + waiting.size();
        final String id = Integer.toString(
            ReformCloudController.getInstance().getCloudProcessOfferService()
                .nextServerID(serverGroup.getName()));
        final String name =
            serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration()
                .getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService()
            .registerID(serverGroup.getName(), name, Integer.valueOf(id));

        if (serverGroup.getMaxOnline() > waitingAndOnline || serverGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler()
                .sendPacketAsynchronous(client.getName(),
                    new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), preConfig,
                        id, template)
                );
        }
    }

    @Override
    public void startProxy(final String proxyGroupName) {
        this.startProxy(proxyGroupName, new Configuration());
    }

    @Override
    public void startProxy(final String proxyGroupName, final Configuration preConfig) {
        final ProxyGroup proxyGroup = this.internalCloudNetwork.getProxyGroups()
            .getOrDefault(proxyGroupName, null);
        final Client client = ReformCloudController.getInstance()
            .getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

        if (client == null) {
            return;
        }

        final Collection<String> proxies = ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager().getOnlineServers(proxyGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance()
            .getCloudProcessOfferService().getWaiting(proxyGroup.getName());

        final int waitingAndOnline = proxies.size() + waiting.size();
        final String id = Integer.toString(
            ReformCloudController.getInstance().getCloudProcessOfferService()
                .nextProxyID(proxyGroup.getName()));
        final String name =
            proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration()
                .getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerProxyID(
            proxyGroup.getName(), name, Integer.valueOf(id)
        );

        if (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler()
                .sendPacketAsynchronous(client.getName(),
                    new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), preConfig, id)
                );
        }
    }

    @Override
    public void startProxy(ProxyGroup proxyGroup) {
        this.startProxy(proxyGroup.getName(), new Configuration());
    }

    @Override
    public void startProxy(ProxyGroup proxyGroup, Configuration preConfig) {
        this.startProxy(proxyGroup.getName(), preConfig);
    }

    @Override
    public void startProxy(ProxyGroup proxyGroup, Configuration preConfig, String template) {
        final Client client = ReformCloudController.getInstance()
            .getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

        if (client == null) {
            return;
        }

        final Collection<String> proxies = ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager().getOnlineServers(proxyGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance()
            .getCloudProcessOfferService().getWaiting(proxyGroup.getName());

        final int waitingAndOnline = proxies.size() + waiting.size();
        final String id = Integer.toString(
            ReformCloudController.getInstance().getCloudProcessOfferService()
                .nextProxyID(proxyGroup.getName()));
        final String name =
            proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration()
                .getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerProxyID(
            proxyGroup.getName(), name, Integer.valueOf(id)
        );

        if (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler()
                .sendPacketAsynchronous(client.getName(),
                    new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), preConfig, id,
                        template)
                );
        }
    }

    @Override
    public boolean stopProxy(String name) {
        return stopProxy(getProxyInfo(name));
    }

    @Override
    public boolean stopProxy(ProxyInfo proxyInfo) {
        if (proxyInfo == null) {
            return false;
        }

        return ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
            proxyInfo.getCloudProcess().getClient(),
            new PacketOutStopProcess(proxyInfo.getCloudProcess().getName())
        );
    }

    @Override
    public boolean stopServer(String name) {
        return stopServer(getServerInfo(name));
    }

    @Override
    public boolean stopServer(ServerInfo serverInfo) {
        if (serverInfo == null) {
            return false;
        }

        return ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
            serverInfo.getCloudProcess().getClient(),
            new PacketOutStopProcess(serverInfo.getCloudProcess().getName())
        );
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

        cloudConfiguration.createServerGroup(serverGroup);
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

        cloudConfiguration.createProxyGroup(proxyGroup);
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

        cloudConfiguration.createClient(client);
    }

    @Override
    public void updateServerInfo(ServerInfo serverInfo) {
        this.internalCloudNetwork.getServerProcessManager().updateServerInfo(serverInfo);
        channelHandler
            .sendToAllSynchronized(new PacketOutServerInfoUpdate(serverInfo, internalCloudNetwork));
        eventManager.fire(new ServerInfoUpdateEvent(serverInfo));
    }

    @Override
    public void updateProxyInfo(ProxyInfo proxyInfo) {
        this.internalCloudNetwork.getServerProcessManager().updateProxyInfo(proxyInfo);
        channelHandler.sendToAllSynchronized(new PacketOutProxyInfoUpdate(proxyInfo));
        eventManager.fire(new ProxyInfoUpdateEvent(proxyInfo));
    }

    @Override
    public void updateServerGroup(ServerGroup serverGroup) {
        cloudConfiguration.updateServerGroup(serverGroup);
    }

    @Override
    public void updateProxyGroup(ProxyGroup proxyGroup) {
        cloudConfiguration.updateProxyGroup(proxyGroup);
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
        if (cloudConfiguration.getWebUsers().contains(webUser)) {
            return;
        }

        cloudConfiguration.createWebUser(webUser);
    }

    @Override
    public void dispatchConsoleCommand(String commandLine) {
        this.commandManager.dispatchCommand(commandLine);
    }

    @Override
    public void dispatchConsoleCommand(CharSequence commandLine) {
        this.dispatchConsoleCommand(String.valueOf(commandLine));
    }

    @Override
    public String dispatchConsoleCommandAndGetResult(String commandLine) {
        CompletableFuture<String> result = new CompletableFuture<>();
        CommandSender commandSender = new CommandSender() {
            @Override
            public boolean hasPermission(String permission) {
                return true;
            }

            @Override
            public void sendMessage(String message) {
                result.complete(message);
            }
        };
        this.commandManager.dispatchCommand(commandSender, commandLine);
        String resultAsMessage;
        try {
            resultAsMessage = result.get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            resultAsMessage = null;
        }

        return resultAsMessage;
    }

    @Override
    public String dispatchConsoleCommandAndGetResult(CharSequence commandLine) {
        return dispatchConsoleCommandAndGetResult(String.valueOf(commandLine));
    }

    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup) {
        DevProcess devProcess = new DevProcess(
            serverGroup,
            new Configuration(),
            "default",
            System.currentTimeMillis()
        );
        ReformCloudController.getInstance().getCloudProcessOfferService().getDevProcesses()
            .offer(devProcess);
        return devProcess;
    }

    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup, String template) {
        DevProcess devProcess = new DevProcess(
            serverGroup,
            new Configuration(),
            template,
            System.currentTimeMillis()
        );
        ReformCloudController.getInstance().getCloudProcessOfferService().getDevProcesses()
            .offer(devProcess);
        return devProcess;
    }

    @Override
    public DevProcess startQueuedProcess(ServerGroup serverGroup, String template,
        Configuration preConfig) {
        DevProcess devProcess = new DevProcess(
            serverGroup,
            preConfig,
            template,
            System.currentTimeMillis()
        );
        ReformCloudController.getInstance().getCloudProcessOfferService().getDevProcesses()
            .offer(devProcess);
        return devProcess;
    }

    @Override
    public OnlinePlayer getOnlinePlayer(UUID uniqueId) {
        return this.playerDatabase.getOnlinePlayer(uniqueId);
    }

    @Override
    public OnlinePlayer getOnlinePlayer(String name) {
        UUID uuid = this.playerDatabase.getFromName(name);
        if (uuid == null) {
            return null;
        }

        return this.playerDatabase.getOnlinePlayer(uuid);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID uniqueId) {
        return this.playerDatabase.getOfflinePlayer(uniqueId);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        UUID uuid = this.playerDatabase.getFromName(name);
        if (uuid == null) {
            return null;
        }

        return this.playerDatabase.getOfflinePlayer(uuid);
    }

    @Override
    public void updateOnlinePlayer(OnlinePlayer onlinePlayer) {
        this.playerDatabase.updateOnlinePlayer(onlinePlayer);
    }

    @Override
    public void updateOfflinePlayer(OfflinePlayer offlinePlayer) {
        this.playerDatabase.updateOfflinePlayer(offlinePlayer);
    }

    @Override
    public boolean isOnline(UUID uniqueId) {
        return this.playerDatabase.getOnlinePlayer(uniqueId) != null;
    }

    @Override
    public boolean isOnline(String name) {
        UUID uuid = this.playerDatabase.getFromName(name);
        if (uuid == null) {
            return false;
        }

        return this.playerDatabase.getOnlinePlayer(uuid) != null;
    }

    @Override
    public boolean isRegistered(UUID uniqueId) {
        return this.playerDatabase.getOfflinePlayer(uniqueId) != null;
    }

    @Override
    public boolean isRegistered(String name) {
        UUID uuid = this.playerDatabase.getFromName(name);
        if (uuid == null) {
            return false;
        }

        return this.playerDatabase.getOfflinePlayer(uuid) != null;
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
        int online = 0;
        for (ProxyInfo proxyInfo : this.internalCloudNetwork.getServerProcessManager()
            .getAllRegisteredProxyProcesses()) {
            online += proxyInfo.getOnline();
        }

        return online;
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
            channel, "ReformCloudController", packet, onSuccess
        );
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess,
        NetworkQueryInboundHandler onFailure) {
        this.channelHandler.sendPacketQuerySync(
            channel, "ReformCloudController", packet, onSuccess, onFailure
        );
    }

    @Override
    public PacketFuture createPacketFuture(Packet packet, String networkComponent) {
        this.channelHandler.toQueryPacket(packet, UUID.randomUUID(), "ReformCloudController");
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
            channel, "ReformCloudController", packet
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
    public NettyHandler getNettyHandler() {
        return ReformCloudLibraryServiceProvider.getInstance().getNettyHandler();
    }

    @Override
    public Optional<SaveAPIService> getAPISave() {
        return Optional.ofNullable(SaveAPIService.instance.get());
    }

    @Override
    public void removeInternalProcess() {
        System.exit(ExitUtil.STOPPED_SUCESS);
    }

    public void reloadAllSave() {
        try {
            this.reloadAll();
        } catch (Throwable throwable) {
            StringUtil.printError(loggerProvider, "Error while reloading cloudsystem", throwable);
        }
    }

    public Language getLoadedLanguage() {
        return this.internalCloudNetwork.getLoaded();
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public LoggerProvider getLoggerProvider() {
        return this.loggerProvider;
    }

    public InternalCloudNetwork getInternalCloudNetwork() {
        return this.internalCloudNetwork;
    }

    public StatisticsProvider getStatisticsProvider() {
        return this.statisticsProvider;
    }

    public PlayerDatabase getPlayerDatabase() {
        return this.playerDatabase;
    }

    public ChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    public AddonParallelLoader getAddonParallelLoader() {
        return this.addonParallelLoader;
    }

    public CloudProcessOfferService getCloudProcessOfferService() {
        return this.cloudProcessOfferService;
    }

    public EventManager getEventManager() {
        return this.eventManager;
    }

    public ScreenSessionProvider getScreenSessionProvider() {
        return this.screenSessionProvider;
    }

    public IngameCommandManger getIngameCommandManger() {
        return this.ingameCommandManger;
    }

    public TaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    public List<UUID> getUuid() {
        return this.uuid;
    }

    public List<DatabaseProvider> getDatabaseProviders() {
        return this.databaseProviders;
    }

    public Map<String, ClientInfo> getClientInfos() {
        return this.clientInfos;
    }

    public ReformWebServer getReformWebServer() {
        return this.reformWebServer;
    }

    public NettySocketServer getNettySocketServer() {
        return this.nettySocketServer;
    }

    public ReformCloudLibraryServiceProvider getReformCloudLibraryServiceProvider() {
        return this.reformCloudLibraryServiceProvider;
    }

    public CloudConfiguration getCloudConfiguration() {
        return this.cloudConfiguration;
    }

    public Thread getShutdownHook() {
        return this.shutdownHook;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setLoggerProvider(LoggerProvider loggerProvider) {
        this.loggerProvider = loggerProvider;
    }

    public void setInternalCloudNetwork(InternalCloudNetwork internalCloudNetwork) {
        this.internalCloudNetwork = internalCloudNetwork;
    }

    public void setUuid(List<UUID> uuid) {
        this.uuid = uuid;
    }

    public void setDatabaseProviders(List<DatabaseProvider> databaseProviders) {
        this.databaseProviders = databaseProviders;
    }

    public void setReformWebServer(ReformWebServer reformWebServer) {
        this.reformWebServer = reformWebServer;
    }

    public void setReformCloudLibraryServiceProvider(
        ReformCloudLibraryServiceProvider reformCloudLibraryServiceProvider) {
        this.reformCloudLibraryServiceProvider = reformCloudLibraryServiceProvider;
    }

    public void setCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }

    public void setShutdownHook(Thread shutdownHook) {
        this.shutdownHook = shutdownHook;
    }
}
