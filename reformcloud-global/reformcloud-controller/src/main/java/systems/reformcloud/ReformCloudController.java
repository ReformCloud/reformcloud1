/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.addons.AddonParallelLoader;
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
import systems.reformcloud.netty.sync.in.PacketInSyncUpdateClientInfo;
import systems.reformcloud.startup.CloudProcessOfferService;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.cloudsystem.InternalCloudNetwork;
import systems.reformcloud.utility.runtime.Reload;
import systems.reformcloud.utility.runtime.Shutdown;
import systems.reformcloud.utility.threading.scheduler.Scheduler;
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
    private final NettyHandler nettyHandler = new NettyHandler();
    private final ChannelHandler channelHandler = new ChannelHandler();
    private final Scheduler scheduler = new Scheduler(40);
    private final AddonParallelLoader addonParallelLoader = new AddonParallelLoader();
    private final CloudProcessOfferService cloudProcessOfferService = new CloudProcessOfferService();
    private final EventManager eventManager = new EventManager();

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

        this.preparePacketHandlers();
        this.prepareCommands();

        this.reformCloudLibraryServiceProvider.setInternalCloudNetwork(internalCloudNetwork);

        this.addonParallelLoader.loadAddons();

        this.nettySocketServer = new NettySocketServer(ssl, this.cloudConfiguration.getNettyAddress(),
                !cloudConfiguration.getCertFile().equalsIgnoreCase(StringUtil.NULL) ?
                        new File(cloudConfiguration.getCertFile()) : null,
                !cloudConfiguration.getKeyFile().equalsIgnoreCase(StringUtil.NULL) ?
                        new File(cloudConfiguration.getKeyFile()) : null);

        if (cloudConfiguration.getWebAddress() != null)
            reformWebServer = new ReformWebServer(cloudConfiguration.getWebAddress(), ssl,
                    !cloudConfiguration.getCertFile().equalsIgnoreCase(StringUtil.NULL) ?
                            new File(cloudConfiguration.getCertFile()) : null,
                    !cloudConfiguration.getKeyFile().equalsIgnoreCase(StringUtil.NULL) ?
                            new File(cloudConfiguration.getKeyFile()) : null);

        Thread thread = new Thread(this.scheduler);
        thread.setDaemon(true);
        thread.start();

        Thread autoSave = new Thread(() -> {
            while (Thread.currentThread().isInterrupted()) {
                this.databaseProviders.forEach(databaseProvider -> databaseProvider.save());

                try {
                    Thread.sleep(15000);
                } catch (final InterruptedException ignored) {
                }
            }
        }, "AutoSave");
        autoSave.setDaemon(true);
        autoSave.start();

        this.shutdownHook = new Thread(this::shutdownAll, "Shutdown-Hook");
        Runtime.getRuntime().addShutdownHook(this.shutdownHook);

        this.scheduler.runTaskRepeatAsync(cloudProcessOfferService, 0, 250);

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
        this.nettyHandler
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
                .registerHandler("ClientDisconnects", new PacketInSyncClientDisconnects());
    }

    /**
     * Prepares all commands
     *
     * @see CommandManager#registerCommand(String, Command)
     */
    private void prepareCommands() {
        this.commandManager
                .registerCommand("exit", new CommandExit())
                .registerCommand("help", new CommandHelp())
                .registerCommand("execute", new CommandExecute())
                .registerCommand("process", new CommandProcess())
                .registerCommand("reload", new CommandReload())
                .registerCommand("create", new CommandCreate())
                .registerCommand("delete", new CommandDelete())
                .registerCommand("clear", new CommandClear())
                .registerCommand("info", new CommandInfo())
                .registerCommand("copy", new CommandCopy())
                .registerCommand("update", new CommandUpdate())
                .registerCommand("whitelist", new CommandWhitelist())
                .registerCommand("log", new CommandLog())
                .registerCommand("addons", new CommandAddons());
    }

    /**
     * Reloads the ReformCloudController
     *
     * @throws Throwable
     */
    @Override
    public void reloadAll() throws Throwable {
        this.loggerProvider.info(this.getLoadedLanguage().getController_reload());

        this.cloudConfiguration = null;
        this.nettyHandler.clearHandlers();
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

        this.loggerProvider.info(this.getLoadedLanguage().getController_reload_done());
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
        });

        this.internalCloudNetwork.getServerProcessManager().getAllRegisteredProxyProcesses().forEach(e -> {
            this.channelHandler.sendPacketSynchronized(e.getCloudProcess().getClient(),
                    new PacketOutStopProcess(e.getCloudProcess().getName()));
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
        List<Client> available = new ArrayList<>();

        this.internalCloudNetwork.getClients()
                .values()
                .stream()
                .filter(e -> this.channelHandler.isChannelRegistered(e.getName()) && clients.contains(e.getName()))
                .forEach(e -> available.add(e));

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

    public Language getLoadedLanguage() {
        return this.internalCloudNetwork.getLoaded();
    }
}
