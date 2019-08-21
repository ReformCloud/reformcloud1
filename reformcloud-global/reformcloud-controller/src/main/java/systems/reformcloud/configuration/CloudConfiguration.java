/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.api.events.ClientCreatedEvent;
import systems.reformcloud.api.events.ClientDeletedEvent;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.database.config.DatabaseConfig;
import systems.reformcloud.language.LanguageManager;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.AbstractLoggerProvider;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.defaults.DefaultProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.defaults.LobbyGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.out.PacketOutStopProcess;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public final class CloudConfiguration implements Serializable {

    private EthernetAddress nettyAddress;

    private EthernetAddress webAddress;

    private String controllerKey;

    private String host;

    private String splitter;

    private String certFile;

    private String keyFile;

    private String loadedLang;

    private List<Client> clients;

    private List<ServerGroup> serverGroups = new ArrayList<>();

    private List<ProxyGroup> proxyGroups = new ArrayList<>();

    private List<WebUser> webUsers = new ArrayList<>();

    private List<String> ipWhitelist = new ArrayList<>();

    private DatabaseConfig databaseConfig;

    /**
     * Prepares and loads ReformCloudController Configuration
     */
    public CloudConfiguration() {
        for (File directory : new File[]{
            new File("reformcloud/templates/servers"),
            new File("reformcloud/templates/proxies"),
            new File("reformcloud/groups/proxies"),
            new File("reformcloud/groups/servers"),
            new File("reformcloud/database/stats"),
            new File("reformcloud/database/players"),
            new File("reformcloud/database/players/nametouuid"),
            new File("reformcloud/files"),
            new File("reformcloud/addons")
        }) {
            directory.mkdirs();
        }

        this.defaultSetup();
        this.initDatabaseConfig();
        this.load();
        this.loadMessages();
        this.loadProxyGroups();
        this.loadServerGroups();
        this.createTemplatesIfNotExists();
    }

    private void init(final String ip, final String lang) {
        if (Files.exists(Paths.get("configuration.properties"))) {
            return;
        }

        Properties properties = new Properties();

        properties.setProperty("server.ip", ip);
        properties.setProperty("server.port", 5000 + StringUtil.EMPTY);

        properties.setProperty("webServer.enabled", true + StringUtil.EMPTY);
        properties.setProperty("webServer.ip", ip);
        properties.setProperty("webServer.port", 4790 + StringUtil.EMPTY);

        properties.setProperty("ssl.certFilePath", StringUtil.NULL);
        properties.setProperty("ssl.keyFilePath", StringUtil.NULL);

        properties.setProperty("general.language", lang.toLowerCase());
        properties.setProperty("general.server-separator", "-");

        try (OutputStream outputStream = Files
            .newOutputStream(Paths.get("configuration.properties"))) {
            properties.store(outputStream, "ReformCloud default Configuration");
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Cannot store configuration file", ex);
        }

        FileUtils.writeToFile(Paths.get("reformcloud/files/ControllerKEY"),
            ReformCloudLibraryService.newKey());
    }

    private void initDatabaseConfig() {
        final AbstractLoggerProvider colouredConsoleProvider = ReformCloudController.getInstance()
            .getColouredConsoleProvider();

        Language language = ReformCloudController.getInstance().getLoadedLanguage();

        if (!Files.exists(Paths.get("reformcloud/database/config.json"))) {
            Configuration configuration = new Configuration();
            colouredConsoleProvider.info(language.getSetup_choose_database());
            String type = this.readString(colouredConsoleProvider,
                s -> DatabaseConfig.DataBaseType.find(s) != null);
            DatabaseConfig.DataBaseType dataBaseType = DatabaseConfig.DataBaseType.find(type);
            if (dataBaseType.equals(DatabaseConfig.DataBaseType.FILE)) {
                configuration.addValue("config", new DatabaseConfig(
                    null,
                    -1,
                    null,
                    null,
                    null,
                    dataBaseType
                )).write(Paths.get("reformcloud/database/config.json"));
            } else {
                colouredConsoleProvider.info(language.getSetup_database_host());
                String host = this.readString(colouredConsoleProvider, s -> true);
                colouredConsoleProvider.info(language.getSetup_database_port());
                int port = this.readInt(colouredConsoleProvider, s -> s > 0);
                colouredConsoleProvider.info(language.getSetup_database_name());
                String database = this.readString(colouredConsoleProvider, s -> true);
                colouredConsoleProvider.info(language.getSetup_database_username());
                String userName = this.readString(colouredConsoleProvider, s -> true);
                colouredConsoleProvider.info(language.getSetup_database_password());
                String password = this.readString(colouredConsoleProvider, s -> true);
                configuration.addValue("config", new DatabaseConfig(
                    host,
                    port,
                    userName,
                    password,
                    database,
                    dataBaseType
                )).write(Paths.get("reformcloud/database/config.json"));
            }
        }

        this.databaseConfig = Configuration.parse(
            Paths.get("reformcloud/database/config.json")
        ).getValue("config", new TypeToken<DatabaseConfig>() {
        });
    }

    private boolean defaultSetup() {
        if (Files.exists(Paths.get("reformcloud/clients.json"))) {
            return false;
        }

        final AbstractLoggerProvider colouredConsoleProvider = ReformCloudController.getInstance()
            .getColouredConsoleProvider();

        colouredConsoleProvider.info("Please enter a language [\"german\", \"english\"]");
        String lang = this.readString(colouredConsoleProvider,
            s -> s.equalsIgnoreCase("german") || s.equalsIgnoreCase("english"));

        Language language = new LanguageManager(lang).getLoaded();

        colouredConsoleProvider.info(language.getSetup_controller_ip());
        String controllerIP = this.readString(colouredConsoleProvider,
            s -> s.split("\\.").length == 4).trim();

        colouredConsoleProvider.info(language.getSetup_name_of_first_client());
        String clientName = this.readString(colouredConsoleProvider, s -> true);
        colouredConsoleProvider.info(language.getSetup_ip_of_new_client());
        String ip = this.readString(colouredConsoleProvider, s -> s.split("\\" +
            ".").length == 4).trim();

        new Configuration()
            .addValue("client", Collections.singletonList(new Client(clientName, ip, null)))
            .write(Paths.get("reformcloud/clients.json"));

        colouredConsoleProvider.info(language.getSetup_ram_of_default_group());
        int lobbyMemory = this.readInt(colouredConsoleProvider, integer -> integer >= 50);
        colouredConsoleProvider.info(language.getSetup_choose_minecraft_version());
        SpigotVersions.AVAILABLE_VERSIONS.forEach(colouredConsoleProvider::info);
        String version = this
            .readString(colouredConsoleProvider, SpigotVersions.AVAILABLE_VERSIONS::contains);
        colouredConsoleProvider.info(language.getSetup_choose_spigot_version());
        SpigotVersions.sortedByVersion(version).values()
            .forEach(e -> colouredConsoleProvider.info("   " + e.name()));
        String provider = this.readString(colouredConsoleProvider, s -> SpigotVersions.getByName(s) != null);

        new Configuration().addValue("group",
            new LobbyGroup(SpigotVersions.getByName(provider), lobbyMemory, clientName))
            .write(Paths.get("reformcloud/groups/servers/Lobby.json"));

        colouredConsoleProvider.info(language.getSetup_ram_of_default_proxy_group());
        int memory = this.readInt(colouredConsoleProvider, integer -> integer >= 50);
        colouredConsoleProvider.info(language.getSetup_choose_proxy_version());
        ProxyVersions.sorted().values().forEach(e -> colouredConsoleProvider.info("   " + e.name()));
        String in = this.readString(colouredConsoleProvider, s -> ProxyVersions.getByName(s) != null);

        new Configuration().addValue("group",
            new DefaultProxyGroup(memory, clientName, ProxyVersions.getByName(in)))
            .write(Paths.get("reformcloud/groups/proxies/Proxy.json"));

        colouredConsoleProvider.info(language.getSetup_load_default_addons());
        String addons = this
            .readString(colouredConsoleProvider, s -> s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no"));
        if (addons.equalsIgnoreCase("yes")) {
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudSigns.jar",
                "reformcloud/addons/ReformCloudSigns.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudDiscord.jar",
                "reformcloud/addons/ReformCloudDiscordBot.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudPermissions.jar",
                "reformcloud/addons/ReformCloudPermissions.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudProxy.jar",
                "reformcloud/addons/ReformCloudProxy.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudParameters.jar",
                "reformcloud/addons/ReformCloudParameters.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudAutoIcon.jar",
                "reformcloud/addons/ReformCloudAutoIcon.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudProperties.jar",
                "reformcloud/addons/ReformCloudProperties.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudMobs.jar",
                "reformcloud/addons/ReformCloudMobs.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudCloudFlare.jar",
                "reformcloud/addons/ReformCloudCloudFlare.jar");
            DownloadManager.downloadSilentAndDisconnect(
                "https://dl.reformcloud.systems/addons/ReformCloudBackup.jar",
                "reformcloud/addons/ReformCloudBackup.jar");
        }

        final String web = ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE)
            + StringUtil.EMPTY
            + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE);

        new Configuration().addValue("users",
            Collections.singletonList(new WebUser("administrator", StringEncrypt.encryptSHA512(web),
                Collections.singletonMap("*", true)))).write(Paths.get("reformcloud/users.json"));

        colouredConsoleProvider.info(language.getSetup_default_user_created().replace("%password%", web));
        this.init(controllerIP, lang);

        return true;
    }

    private void load() {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(
            Files.newInputStream(Paths.get("configuration.properties")))) {
            properties.load(inputStreamReader);
        } catch (final IOException ex) {
            StringUtil
                .printError(ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                    "Could not load configuration", ex);
        }

        this.host = properties.getProperty("server.ip");
        this.nettyAddress = new EthernetAddress(this.host,
            Integer.parseInt(properties.getProperty("server.port")));

        this.splitter = properties.getProperty("general.server-separator");

        if (Boolean.parseBoolean(properties.getProperty("webServer.enabled"))) {
            this.webAddress = new EthernetAddress(properties.getProperty("webServer.ip"),
                Integer.parseInt(properties.getProperty("webServer.port")));
        }
        this.certFile = properties.getProperty("ssl.certFilePath", StringUtil.NULL);
        this.keyFile = properties.getProperty("ssl.keyFilePath", StringUtil.NULL);
        this.loadedLang = properties.getProperty("general.language");

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/clients.json"));
        this.clients = configuration.getValue("client", new TypeToken<List<Client>>() {
        }.getType());
        this.webUsers = Configuration.parse(Paths.get("reformcloud/users.json"))
            .getValue("users", new TypeToken<List<WebUser>>() {
            }.getType());

        this.controllerKey = FileUtils
            .readFileAsString(new File("reformcloud/files/ControllerKEY"));
    }

    private void loadServerGroups() {
        File dir = new File("reformcloud/groups/servers");
        Configuration configuration;

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".json")) {
                    try {
                        configuration = Configuration.parse(file);
                        ServerGroup serverGroup = configuration
                            .getValue("group", TypeTokenAdaptor.getSERVER_GROUP_TYPE());
                        serverGroups.add(serverGroup);
                    } catch (final Throwable throwable) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not load ServerGroup", throwable);
                        ReformCloudController.getInstance().getColouredConsoleProvider()
                            .serve("Failed to load ServerGroup " + file.getName() + "!");
                    }
                }
            }
        }
    }

    private void loadProxyGroups() {
        File dir = new File("reformcloud/groups/proxies");
        Configuration configuration;

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".json")) {
                    try {
                        configuration = Configuration.parse(file);
                        ProxyGroup proxyGroup = configuration
                            .getValue("group", TypeTokenAdaptor.getPROXY_GROUP_TYPE());
                        proxyGroups.add(proxyGroup);
                    } catch (final Throwable throwable) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not load ProxyGroup", throwable);
                        ReformCloudController.getInstance().getColouredConsoleProvider()
                            .serve("Failed to load DefaultProxyGroup " + file.getName() + "!");
                    }
                }
            }
        }
    }

    private void loadMessages() {
        String messagePath = "reformcloud/messages.json";
        if (!Files.exists(Paths.get(messagePath))) {
            if (loadedLang.equals("german")) {
                new Configuration()
                    .addStringValue("internal-global-prefix", "§2R§feform§2C§floud §7┃")

                    .addStringValue("internal-api-bungee-command-no-permission",
                        "§cDu hast keine Rechte diesen Command zu nutzen.")

                    .addStringValue("internal-api-bungee-command-hub-already",
                        "%prefix% §7Du bist bereits mit einem Hub Server verbunden.")
                    .addStringValue("internal-api-bungee-command-hub-not-available",
                        "%prefix% §7Aktuell ist kein Hub Server verfügbar.")

                    .addStringValue("internal-api-bungee-command-jumpto-server-player-not-found",
                        "%prefix% §cEs konnte kein Player oder Server gefunden werden, zu dem du wechseln kannst.")
                    .addStringValue("internal-api-bungee-command-jumpto-success",
                        "%prefix% §aDu hast dich erfolgreich zum Server verbunden.")

                    .addStringValue("internal-api-bungee-command-reformcloud-invalid-syntax",
                        "%prefix% §7/reformcloud <command>")
                    .addStringValue("internal-api-bungee-command-reformcloud-no-permission",
                        "%prefix% §7Dieser Command ist nicht erlaubt.")
                    .addStringValue("internal-api-bungee-command-reformcloud-command-success",
                        "%prefix% §7Der Command wurde erforlgreich ausgeführt \n §7Bitte prüfe die Console für mehr Details.")

                    .addStringValue("internal-api-bungee-maintenance-join-no-permission",
                        "§cWhitelist ist aktiviert, du bist aber nicht geaddet.")
                    .addStringValue("internal-api-bungee-connect-hub-no-server",
                        "%prefix% §7Aktuell ist kein Hub Server verfügbar.")

                    .addStringValue("internal-api-bungee-command-send-controller",
                        "%prefix% Der Command wurde zum Controller gesendet.")

                    .addStringValue("internal-api-bungee-server-kick",
                        "§7§m--------------§r§7| §2R§feform§2C§floud §7|§m--------------§r\n" +
                            "§cDer Server§8, §77auf dem du verbunden warst §8(§a%old_server§8)§8, §7ist leider abgestürtzt §8(§7oder wurde gestoppt§8) §7und daher wurdest du zum neuen Server gesendet §8(§a%new_server§8)"
                            +
                            "§7§m--------------§r§7| §2R§feform§2C§floud §7|§m--------------§r")

                    .addStringValue("internal-api-bungee-startup-server",
                        "%prefix% §7ServerProcess §6%server-name% §7wird gestartet...")
                    .addStringValue("internal-api-bungee-startup-proxy",
                        "%prefix% §7ProxyProcess §6%proxy-name% §7wird gestartet...")
                    .addStringValue("internal-api-bungee-remove-server",
                        "%prefix% §7ServerProcess §6%server-name% §7wird gestoppt...")
                    .addStringValue("internal-api-bungee-remove-proxy",
                        "%prefix% §7ProxyProcess §6%proxy-name% §7wird gestoppt...")

                    .addStringValue("internal-api-spigot-connect-no-permission",
                        "%prefix% §cDu hast nicht ausreichend Permissions diesen Server zu betreten.")
                    .addStringValue("internal-api-spigot-connect-only-proxy",
                        "%prefix% §cNur Only Proxy Join Erlaubt.")

                    .addStringValue("internal-api-spigot-command-signs-not-enabled",
                        "%prefix% §7Schilder sind deaktiviert.")
                    .addStringValue("internal-api-spigot-command-signs-create-usage",
                        "%prefix% §7/reformsigns <create/createitem/deleteall> <group-name>")
                    .addStringValue("internal-api-spigot-command-signs-create-success",
                        "%prefix% §7Schild wurde erforlgreich erstellt")
                    .addStringValue("internal-api-spigot-command-signs-create-already-exists",
                        "%prefix% §7Schild existiert bereits.")
                    .addStringValue("internal-api-spigot-command-signs-block-not-sign",
                        "%prefix% §7Zielblock ist kein Schild.")
                    .addStringValue("internal-api-spigot-command-signs-delete-success",
                        "%prefix% §7Schild wurde gelöscht.")
                    .addStringValue("internal-api-spigot-command-signs-item-success",
                        "%prefix% §7Das Sign-Item wurde zu deinem Inventar hinzugefügt.")
                    .addStringValue("internal-api-spigot-command-signs-delete-not-exists",
                        "%prefix% §7Schild existiert nicht.")
                    .addStringValue("internal-api-spigot-command-signs-list",
                        "%prefix% §7Die folgenden Schilder sind registriert:")
                    .addStringValue("internal-api-spigot-command-signs-usage-1",
                        "%prefix% §7/reformsigns <create/createitem> <group-name>")
                    .addStringValue("internal-api-spigot-command-signs-usage-2",
                        "%prefix% §7/reformsigns <delete/deleteitem>")
                    .addStringValue("internal-api-spigot-command-signs-usage-3",
                        "%prefix% §7/reformsigns list")

                    .write(Paths.get(messagePath));

            } else if (loadedLang.equals("english")) {
                new Configuration()
                    .addStringValue("internal-global-prefix", "§2R§feform§2C§floud §7┃")

                    .addStringValue("internal-api-bungee-command-no-permission",
                        "§cYou do not have permission to execute this command")

                    .addStringValue("internal-api-bungee-command-hub-already",
                        "%prefix% §7You are already connected to a hub server")
                    .addStringValue("internal-api-bungee-command-hub-not-available",
                        "%prefix% §7There is no hub server available")

                    .addStringValue("internal-api-bungee-command-jumpto-server-player-not-found",
                        "%prefix% §cCould not find player or server to go to")
                    .addStringValue("internal-api-bungee-command-jumpto-success",
                        "%prefix% §aYou was connected to the server")

                    .addStringValue("internal-api-bungee-command-reformcloud-invalid-syntax",
                        "%prefix% §7/reformcloud <command>")
                    .addStringValue("internal-api-bungee-command-reformcloud-no-permission",
                        "%prefix% §7Command not allowed")
                    .addStringValue("internal-api-bungee-command-reformcloud-command-success",
                        "%prefix% §7Command has been executed successfully \n §7Please check the Controller Console for more details")

                    .addStringValue("internal-api-bungee-maintenance-join-no-permission",
                        "§cWhitelist is enabled, but you are not added")
                    .addStringValue("internal-api-bungee-connect-hub-no-server",
                        "%prefix% §7There is no hub server available")

                    .addStringValue("internal-api-bungee-command-send-controller",
                        "%prefix% The command was send to the controller")

                    .addStringValue("internal-api-bungee-server-kick",
                        "§7§m--------------§r§7| §2R§feform§2C§floud §7|§m--------------§r\n" +
                            "§cThe server you were on (%old_server%) went down, and you have been connected to %new_server%§r\n"
                            +
                            "§7§m--------------§r§7| §2R§feform§2C§floud §7|§m--------------§r")

                    .addStringValue("internal-api-bungee-startup-server",
                        "%prefix% §7ServerProcess §6%server-name% §7is starting...")
                    .addStringValue("internal-api-bungee-startup-proxy",
                        "%prefix% §7ProxyProcess §6%proxy-name% §7is starting...")
                    .addStringValue("internal-api-bungee-remove-server",
                        "%prefix% §7ServerProcess §6%server-name% §7is stopping...")
                    .addStringValue("internal-api-bungee-remove-proxy",
                        "%prefix% §7ProxyProcess §6%proxy-name% §7is stopping...")

                    .addStringValue("internal-api-spigot-connect-no-permission",
                        "%prefix% §cYou do not have permission to join this server")
                    .addStringValue("internal-api-spigot-connect-only-proxy",
                        "%prefix% §cOnly Proxy join allowed")

                    .addStringValue("internal-api-spigot-command-signs-not-enabled",
                        "%prefix% §7Signs aren't enabled")
                    .addStringValue("internal-api-spigot-command-signs-create-usage",
                        "%prefix% §7/reformsigns <create/createitem/deleteall> <group-name>")
                    .addStringValue("internal-api-spigot-command-signs-create-success",
                        "%prefix% §7Sign was created successfully")
                    .addStringValue("internal-api-spigot-command-signs-create-already-exists",
                        "%prefix% §7Sign already exits")
                    .addStringValue("internal-api-spigot-command-signs-block-not-sign",
                        "%prefix% §7Target block isn't a sign")
                    .addStringValue("internal-api-spigot-command-signs-delete-success",
                        "%prefix% §7Sign was deleted")
                    .addStringValue("internal-api-spigot-command-signs-item-success",
                        "%prefix% §7The Sign-Item was added to your Inventory")
                    .addStringValue("internal-api-spigot-command-signs-delete-not-exists",
                        "%prefix% §7Sign doesn't exits")
                    .addStringValue("internal-api-spigot-command-signs-list",
                        "%prefix% §7The Following Signs are registered:")
                    .addStringValue("internal-api-spigot-command-signs-usage-1",
                        "%prefix% §7/reformsigns <create/createitem> <group-name>")
                    .addStringValue("internal-api-spigot-command-signs-usage-2",
                        "%prefix% §7/reformsigns <delete/deleteitem>")
                    .addStringValue("internal-api-spigot-command-signs-usage-3",
                        "%prefix% §7/reformsigns list")

                    .write(Paths.get(messagePath));
            }

            ReformCloudController.getInstance().getInternalCloudNetwork()
                .setMessages(Configuration.parse(Paths.get(messagePath)));
            ReformCloudController.getInstance().getInternalCloudNetwork().setPrefix(
                ReformCloudController.getInstance().getInternalCloudNetwork().getMessages()
                    .getStringValue("internal-global-prefix"));
        }
    }

    private void createTemplatesIfNotExists() {
        this.serverGroups.forEach(serverGroup ->
            serverGroup.getTemplates().forEach(template -> {
                if (!Files.exists(Paths.get(
                    "reformcloud/templates/servers/" + serverGroup.getName() + "/" + template
                        .getName()))) {
                    FileUtils.createDirectory(Paths.get(
                        "reformcloud/templates/servers/" + serverGroup.getName() + "/" + template
                            .getName()));
                }
            })
        );

        this.proxyGroups.forEach(proxyGroup ->
            proxyGroup.getTemplates().forEach(template -> {
                if (!Files.exists(Paths.get(
                    "reformcloud/templates/proxies/" + proxyGroup.getName() + "/" + template
                        .getName()))) {
                    FileUtils.createDirectory(Paths.get(
                        "reformcloud/templates/proxies/" + proxyGroup.getName() + "/" + template
                            .getName()));
                }
            })
        );
    }

    public void createServerGroup(final ServerGroup serverGroup) {
        new Configuration().addValue("group", serverGroup)
            .write(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));
        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage().getController_loading_server()
                .replace("%name%", serverGroup.getName())
                .replace("%clients%", serverGroup.getClients() + StringUtil.EMPTY));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
            .put(serverGroup.getName(), serverGroup);
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        FileUtils.createDirectory(
            Paths.get("reformcloud/templates/servers/" + serverGroup.getName() + "/" +
                serverGroup.getTemplate("default").getName()));

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void createProxyGroup(final ProxyGroup proxyGroup) {
        new Configuration().addValue("group", proxyGroup)
            .write(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));
        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage().getController_loading_proxy()
                .replace("%name%", proxyGroup.getName())
                .replace("%clients%", proxyGroup.getClients() + StringUtil.EMPTY));
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups()
            .put(proxyGroup.getName(), proxyGroup);
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        FileUtils.createDirectory(
            Paths.get("reformcloud/templates/proxies/" + proxyGroup.getName() + "/" +
                proxyGroup.getTemplate("default").getName()));

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void createClient(final Client client) {
        Configuration configuration = Configuration.parse(Paths.get("reformcloud/clients.json"));

        List<Client> clients = configuration.getValue("client", new TypeToken<List<Client>>() {
        }.getType());
        clients.add(client);
        configuration.addValue("client", clients).write(Paths.get("reformcloud/clients.json"));

        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage().getController_loading_client()
                .replace("%name%", client.getName())
                .replace("%ip%", client.getIp()));
        ReformCloudController.getInstance().getInternalCloudNetwork().getClients()
            .put(client.getName(), client);
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
        ReformCloudController.getInstance().getEventManager().fire(new ClientCreatedEvent(client));
    }

    public void createWebUser(final WebUser webUser) {
        Configuration configuration = Configuration.parse(Paths.get("reformcloud/users.json"));
        this.webUsers.add(webUser);
        configuration.addValue("users", this.webUsers).write(Paths.get("reformcloud/users.json"));
    }

    public void updateWebUser(final WebUser webUser) {
        List<WebUser> users = new ArrayList<>(this.webUsers);
        users.parallelStream().filter(e -> webUser.getUserName().equals(e.getUserName()))
            .forEach(e -> this.webUsers.remove(e));
        this.webUsers.add(webUser);
        Configuration.parse(Paths.get("reformcloud/users.json"))
            .addValue("users", this.webUsers)
            .write(Paths.get("reformcloud/users.json"));
    }

    public void updateServerGroup(final ServerGroup serverGroup) {
        try {
            Files
                .delete(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverGroup.getTemplates().forEach(template -> {
            if (!Files.exists(Paths.get(
                "reformcloud/templates/servers/" + serverGroup.getName() + "/" + template
                    .getName()))) {
                FileUtils.createDirectory(
                    Paths.get(
                        "reformcloud/templates/servers/" + serverGroup.getName() + "/" + template
                            .getName())
                );
            }
        });

        new Configuration().addValue("group", serverGroup)
            .write(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
            .remove(serverGroup.getName());
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
            .put(serverGroup.getName(), serverGroup);

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void updateProxyGroup(final ProxyGroup proxyGroup) {
        try {
            Files.delete(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        proxyGroup.getTemplates().forEach(template -> {
            if (!Files.exists(Paths.get(
                "reformcloud/templates/proxies/" + proxyGroup.getName() + "/" + template
                    .getName()))) {
                FileUtils.createDirectory(
                    Paths.get(
                        "reformcloud/templates/proxies/" + proxyGroup.getName() + "/" + template
                            .getName())
                );
            }
        });

        new Configuration().addValue("group", proxyGroup)
            .write(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups()
            .remove(proxyGroup.getName());
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups()
            .put(proxyGroup.getName(), proxyGroup);

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void deleteServerGroup(final ServerGroup serverGroup) throws IOException {
        Files.delete(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));

        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage()
                .getController_deleting_servergroup()
                .replace("%name%", serverGroup.getName())
                .replace("%clients%", serverGroup.getClients() + StringUtil.EMPTY));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
            .remove(serverGroup.getName());
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        serverGroup.getTemplates().forEach(template ->
            FileUtils.deleteFullDirectory(
                Paths.get("reformcloud/templates/servers/" + serverGroup.getName() + "/" + template
                    .getName())
            )
        );

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
            .getAllRegisteredServerProcesses().stream()
            .filter(e -> e.getServerGroup().getName().equals(serverGroup.getName()))
            .forEach(e -> {
                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(e.getCloudProcess().getClient(),
                        new PacketOutStopProcess(e.getCloudProcess().getName()));
                ReformCloudLibraryService.sleep(2000);
            });
    }

    public void deleteProxyGroup(final ProxyGroup proxyGroup) throws IOException {
        Files.delete(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));

        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage()
                .getController_deleting_proxygroup()
                .replace("%name%", proxyGroup.getName())
                .replace("%clients%", proxyGroup.getClients() + StringUtil.EMPTY));
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups()
            .remove(proxyGroup.getName());
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        proxyGroup.getTemplates().forEach(template ->
            FileUtils.deleteFullDirectory(
                Paths.get("reformcloud/templates/proxies/" + proxyGroup.getName() + "/" + template
                    .getName())
            )
        );

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
            .getAllRegisteredProxyProcesses().stream()
            .filter(e -> e.getProxyGroup().getName().equals(proxyGroup.getName()))
            .forEach(e -> {
                ReformCloudController.getInstance().getChannelHandler()
                    .sendPacketAsynchronous(e.getCloudProcess().getClient(),
                        new PacketOutStopProcess(e.getCloudProcess().getName()));
                ReformCloudLibraryService.sleep(2000);
            });
    }

    public void deleteClient(final Client client) {
        Configuration configuration = Configuration.parse(Paths.get("reformcloud/clients.json"));

        List<Client> clients = configuration.getValue("client", new TypeToken<List<Client>>() {
        }.getType());
        clients.remove(client);
        configuration.addValue("client", clients).write(Paths.get("reformcloud/clients.json"));

        ReformCloudController.getInstance().getColouredConsoleProvider().info(
            ReformCloudController.getInstance().getLoadedLanguage().getController_delete_client()
                .replace("%name%", client.getName())
                .replace("%ip%", client.getIp()));
        ReformCloudController.getInstance().getInternalCloudNetwork().getClients()
            .remove(client.getName());
        ReformCloudLibraryServiceProvider.getInstance()
            .setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
        ReformCloudController.getInstance().getEventManager().fire(new ClientDeletedEvent(client));
    }

    public void deleteWebuser(final WebUser webUser) {
        this.webUsers.remove(webUser);
        new Configuration().addValue("users", this.webUsers)
            .write(Paths.get("reformcloud/users.json"));

        ReformCloudController.getInstance().getColouredConsoleProvider()
            .info("Deleting WebUser [Name=" + webUser.getUserName() + "]...");
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void addPlayerToWhitelist(final String group, UUID playerUuid) {
        ProxyGroup proxyGroup = ReformCloudController.getInstance().getInternalCloudNetwork()
            .getProxyGroups().get(group);
        proxyGroup.getWhitelist().add(playerUuid);
        Configuration.parse(Paths.get("reformcloud/groups/proxies/" + group + ".json"))
            .addValue("group", proxyGroup)
            .write(Paths.get("reformcloud/groups/proxies/" + group + ".json"));
    }

    public void removePlayerFromWhitelist(final String group, final UUID playerUuid) {
        ProxyGroup proxyGroup = ReformCloudController.getInstance().getInternalCloudNetwork()
            .getProxyGroups().get(group);
        proxyGroup.getWhitelist().remove(playerUuid);
        Configuration.parse(Paths.get("reformcloud/groups/proxies/" + group + ".json"))
            .addValue("group", proxyGroup)
            .write(Paths.get("reformcloud/groups/proxies/" + group + ".json"));
    }

    public String readString(final AbstractLoggerProvider colouredConsoleProvider,
                             Predicate<String> checkable) {
        String readLine = colouredConsoleProvider.readLine();
        while (readLine == null
            || !checkable.test(readLine)
            || readLine.trim().isEmpty()) {
            colouredConsoleProvider.info("Input invalid, please try again");
            readLine = colouredConsoleProvider.readLine();
        }

        return readLine;
    }

    public Integer readInt(final AbstractLoggerProvider colouredConsoleProvider,
                            Predicate<Integer> checkable) {
        String readLine = colouredConsoleProvider.readLine();
        while (readLine == null
            || readLine.trim().isEmpty()
            || !ReformCloudLibraryService.checkIsInteger(readLine)
            || !checkable.test(Integer.parseInt(readLine))) {
            colouredConsoleProvider.info("Input invalid, please try again");
            readLine = colouredConsoleProvider.readLine();
        }

        return Integer.parseInt(readLine);
    }

    public EthernetAddress getNettyAddress() {
        return this.nettyAddress;
    }

    public EthernetAddress getWebAddress() {
        return this.webAddress;
    }

    public String getControllerKey() {
        return this.controllerKey;
    }

    public String getHost() {
        return this.host;
    }

    public String getSplitter() {
        return this.splitter;
    }

    public String getCertFile() {
        return this.certFile;
    }

    public String getKeyFile() {
        return this.keyFile;
    }

    public String getLoadedLang() {
        return this.loadedLang;
    }

    public List<Client> getClients() {
        return this.clients;
    }

    public List<ServerGroup> getServerGroups() {
        return this.serverGroups;
    }

    public List<ProxyGroup> getProxyGroups() {
        return this.proxyGroups;
    }

    public List<WebUser> getWebUsers() {
        return this.webUsers;
    }

    public List<String> getIpWhitelist() {
        return this.ipWhitelist;
    }

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }
}
