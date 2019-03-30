/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.addons.AddonParallelLoader;
import systems.reformcloud.api.*;
import systems.reformcloud.api.documentation.RestAPIDocumentation;
import systems.reformcloud.api.ingame.command.IngameCommandMangerImpl;
import systems.reformcloud.api.network.event.EventAdapter;
import systems.reformcloud.api.player.PlayerProvider;
import systems.reformcloud.commands.*;
import systems.reformcloud.commands.ingame.IngameCommandManger;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.database.DatabaseProvider;
import systems.reformcloud.database.DatabaseSaver;
import systems.reformcloud.database.player.PlayerDatabase;
import systems.reformcloud.database.statistics.StatisticsProvider;
import systems.reformcloud.event.EventManager;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.LoadSuccessEvent;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ClientInfo;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.NettyHandler;
import systems.reformcloud.network.NettySocketServer;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.in.*;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.out.PacketOutStartGameServer;
import systems.reformcloud.network.out.PacketOutStartProxy;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.query.in.PacketInQueryGetOnlinePlayer;
import systems.reformcloud.network.query.in.PacketInQueryGetPlayer;
import systems.reformcloud.network.sync.in.*;
import systems.reformcloud.network.sync.out.PacketOutSyncUpdateClient;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.implementations.OnlinePlayer;
import systems.reformcloud.startup.CloudProcessOfferService;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.screen.ScreenSessionProvider;
import systems.reformcloud.utility.threading.TaskScheduler;
import systems.reformcloud.utility.time.TimeSync;
import systems.reformcloud.versioneering.VersionController;
import systems.reformcloud.web.ReformWebServer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@Getter
@Setter
public class ReformCloudController implements Shutdown, Reload, IAPIService {
    public static volatile boolean RUNNING = false;

    @Getter
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
     * @param loggerProvider            Main Cloud logger, will be used everywhere
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

        this.taskScheduler = ReformCloudLibraryServiceProvider.getInstance().getTaskScheduler();
        this.channelHandler = new ChannelHandler(this.taskScheduler);

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

        new EventAdapter();

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

        this.taskScheduler
                .schedule(DatabaseSaver.class, TimeUnit.SECONDS, 30)
                .schedule(TimeSync.class, TimeUnit.SECONDS, 1);

        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(this.cloudProcessOfferService);

        this.shutdownHook = new Thread(this::shutdownAll, "Shutdown-Hook");
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);

        this.addonParallelLoader.enableAddons();
        this.checkForUpdates();

        RUNNING = true;

        IAPIService.instance.set(this);
        IDefaultPlayerProvider.instance.set(new PlayerProvider());

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
                //Internal
                .registerHandler("UpdateInternalCloudNetwork", new PacketInUpdateInternalCloudNetwork())
                .registerHandler("SendControllerConsoleMessage", new PacketInSendControllerConsoleMessage())
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

                //Helpful
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
                .registerHandler("PlayerAccepted", new PacketInPlayerAccepted())

                //PlayerProvider Handlers
                .registerHandler("ConnectPlayer", new PacketInConnectPlayer())
                .registerHandler("SendPlayerMessage", new PacketInSendPlayerMessage())
                .registerHandler("KickPlayer", new PacketInKickPlayer())

                //Player Query Handlers
                .registerQueryHandler("QueryGetPlayer", new PacketInQueryGetPlayer())
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
                .registerCommand(new CommandUpload())
                .registerCommand(new CommandAssignment())
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
            this.loggerProvider.info(this.getLoadedLanguage().getController_servprocess_stopped()
                        .replace("%name%", e.getCloudProcess().getName()));
        });

        this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses().forEach(e -> {
            this.loggerProvider.info(this.getLoadedLanguage().getController_proxyprocess_stopped()
                    .replace("%name%", e.getCloudProcess().getName()));
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
        final Client client = ReformCloudController.getInstance().getBestClient(serverGroup.getClients(), serverGroup.getMemory());

        if (client == null)
            return;

        final Collection<String> servers = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().getOnlineServers(serverGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting(serverGroup.getName());

        final int waitingAndOnline = servers.size() + waiting.size();
        final String id = Integer.toString(ReformCloudController.getInstance().getCloudProcessOfferService().nextServerID(serverGroup.getName()));
        final String name = serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerID(serverGroup.getName(), name, Integer.valueOf(id));

        if (serverGroup.getMaxOnline() > waitingAndOnline || serverGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                    new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), preConfig, id)
            );
        }
    }

    @Override
    public void startGameServer(final ServerGroup serverGroup, final Configuration preConfig, final String template) {
        final Client client = ReformCloudController.getInstance().getBestClient(serverGroup.getClients(), serverGroup.getMemory());

        if (client == null)
            return;

        final Collection<String> servers = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().getOnlineServers(serverGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting(serverGroup.getName());

        final int waitingAndOnline = servers.size() + waiting.size();
        final String id = Integer.toString(ReformCloudController.getInstance().getCloudProcessOfferService().nextServerID(serverGroup.getName()));
        final String name = serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerID(serverGroup.getName(), name, Integer.valueOf(id));

        if (serverGroup.getMaxOnline() > waitingAndOnline || serverGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                    new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), preConfig, id, template)
            );
        }
    }

    @Override
    public void startProxy(final String proxyGroupName) {
        this.startProxy(proxyGroupName, new Configuration());
    }

    @Override
    public void startProxy(final String proxyGroupName, final Configuration preConfig) {
        final ProxyGroup proxyGroup = this.internalCloudNetwork.getProxyGroups().getOrDefault(proxyGroupName, null);
        final Client client = ReformCloudController.getInstance().getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

        if (client == null)
            return;

        final Collection<String> proxies = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().getOnlineServers(proxyGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting(proxyGroup.getName());

        final int waitingAndOnline = proxies.size() + waiting.size();
        final String id = Integer.toString(ReformCloudController.getInstance().getCloudProcessOfferService().nextProxyID(proxyGroup.getName()));
        final String name = proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerProxyID(
                proxyGroup.getName(), name, Integer.valueOf(id)
        );

        if (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
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
        final Client client = ReformCloudController.getInstance().getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

        if (client == null)
            return;

        final Collection<String> proxies = ReformCloudController.getInstance().getInternalCloudNetwork()
                .getServerProcessManager().getOnlineServers(proxyGroup.getName());
        final Collection<String> waiting = ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting(proxyGroup.getName());

        final int waitingAndOnline = proxies.size() + waiting.size();
        final String id = Integer.toString(ReformCloudController.getInstance().getCloudProcessOfferService().nextProxyID(proxyGroup.getName()));
        final String name = proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;
        ReformCloudController.getInstance().getCloudProcessOfferService().registerProxyID(
                proxyGroup.getName(), name, Integer.valueOf(id)
        );

        if (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1) {
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                    new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), preConfig, id, template)
            );
        }
    }

    @Override
    public OnlinePlayer getOnlinePlayer(UUID uniqueId) {
        return this.playerDatabase.getOnlinePlayer(uniqueId);
    }

    @Override
    public OnlinePlayer getOnlinePlayer(String name) {
        UUID uuid = this.playerDatabase.getFromName(name);
        if (uuid == null)
            return null;

        return this.playerDatabase.getOnlinePlayer(uuid);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID uniqueId) {
        return this.playerDatabase.getOfflinePlayer(uniqueId);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        UUID uuid = this.playerDatabase.getFromName(name);
        if (uuid == null)
            return null;

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
        if (uuid == null)
            return false;

        return this.playerDatabase.getOnlinePlayer(uuid) != null;
    }

    @Override
    public boolean isRegistered(UUID uniqueId) {
        return this.playerDatabase.getOfflinePlayer(uniqueId) != null;
    }

    @Override
    public boolean isRegistered(String name) {
        UUID uuid = this.playerDatabase.getFromName(name);
        if (uuid == null)
            return false;

        return this.playerDatabase.getOfflinePlayer(uuid) != null;
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
        int online = 0;
        for (ProxyInfo proxyInfo : this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses())
            online += proxyInfo.getOnline();

        return online;
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
                channel, "ReformCloudController", packet, onSuccess
        );
    }

    @Override
    public void sendPacketQuery(String channel, Packet packet, NetworkQueryInboundHandler onSuccess, NetworkQueryInboundHandler onFailure) {
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
    public void removeInternalProcess() {
        System.exit(0);
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
}
