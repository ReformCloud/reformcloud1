/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.logging.LoggerProvider;
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
import systems.reformcloud.utility.checkable.Checkable;
import systems.reformcloud.utility.cloudsystem.EthernetAddress;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@Getter
public class CloudConfiguration {
    private EthernetAddress nettyAddress;
    private EthernetAddress webAddress;
    private String controllerKey, host, splitter, certFile, keyFile, loadedLang;
    private List<Client> clients;
    private List<ServerGroup> serverGroups = new ArrayList<>();
    private List<ProxyGroup> proxyGroups = new ArrayList<>();
    private List<WebUser> webUsers = new ArrayList<>();

    /**
     * Prepares and loads ReformCloudController Configuration
     *
     * @throws Throwable
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
        })
            directory.mkdirs();

        this.defaultSetup();
        this.load();
        this.loadMessages();
        this.loadProxyGroups();
        this.loadServerGroups();
        this.createTemplatesIfNotExists();
    }

    private void init(final String ip, final String lang) {
        if (Files.exists(Paths.get("configuration.properties")))
            return;

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

        try (OutputStream outputStream = Files.newOutputStream(Paths.get("configuration.properties"))) {
            properties.store(outputStream, "ReformCloud default Configuration");
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Cannot store configuration file", ex);
        }

        FileUtils.writeToFile(Paths.get("reformcloud/files/ControllerKEY"), ReformCloudLibraryService.newKey());
    }

    private boolean defaultSetup() {
        if (Files.exists(Paths.get("reformcloud/clients.json")))
            return false;

        final LoggerProvider loggerProvider = ReformCloudController.getInstance().getLoggerProvider();

        loggerProvider.info("Please provide the controllerIP");
        String controllerIP = this.readString(loggerProvider, s -> s.split("\\.").length == 4);

        loggerProvider.info("Please provide the first Client name");
        String clientName = this.readString(loggerProvider, s -> true);
        loggerProvider.info("Please provide the ip of the client");
        String ip = this.readString(loggerProvider, s -> s.split("\\.").length == 4);

        new Configuration().addProperty("client", Collections.singletonList(new Client(clientName, ip, null))).write(Paths.get("reformcloud/clients.json"));

        loggerProvider.info("How many mb ram should the default Lobby-Group have?");
        int lobbyMemory = this.readInt(loggerProvider, integer -> integer >= 50);
        loggerProvider.info("Please choose a minecraft version");
        SpigotVersions.AVAILABLE_VERSIONS.forEach(e -> loggerProvider.info(e));
        String version = this.readString(loggerProvider, s -> SpigotVersions.AVAILABLE_VERSIONS.contains(s));
        loggerProvider.info("Which Spigot-Version should be used?");
        SpigotVersions.sortedByVersion(version).values().forEach(e -> loggerProvider.info("   " + e.name()));
        String provider = this.readString(loggerProvider, s -> SpigotVersions.getByName(s) != null);

        new Configuration().addProperty("group", new LobbyGroup(SpigotVersions.getByName(provider), lobbyMemory, clientName)).write(Paths.get("reformcloud/groups/servers/Lobby.json"));

        loggerProvider.info("How many mb ram should the default Proxy-Group have?");
        int memory = this.readInt(loggerProvider, integer -> integer >= 50);
        loggerProvider.info("Which Proxy-Version should be used?");
        ProxyVersions.sorted().values().forEach(e -> loggerProvider.info("   " + e.name()));
        String in = this.readString(loggerProvider, s -> ProxyVersions.getByName(s) != null);

        new Configuration().addProperty("group", new DefaultProxyGroup(memory, clientName, ProxyVersions.getByName(in))).write(Paths.get("reformcloud/groups/proxies/Proxy.json"));

        loggerProvider.info("Do you want to load the sign addon [\"yes\", \"no\"]");
        String signs = this.readString(loggerProvider, s -> s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no"));
        if (Boolean.parseBoolean(signs)) {
            DownloadManager.downloadAndDisconnect("SignAddon", "https://dl.reformcloud.systems/addons/ReformCloudSigns.jar", "reformcloud/addons/SignAddon.jar");
        }

        loggerProvider.info("Do you want to load the discord addon [\"yes\", \"no\"]");
        String discordbot = this.readString(loggerProvider, s -> s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no"));
        if (Boolean.parseBoolean(discordbot)) {
            DownloadManager.downloadAndDisconnect("DiscordBot", "https://dl.reformcloud.systems/addons/ReformCloudDiscord.jar", "reformcloud/addons/DiscordBot.jar");
        }

        loggerProvider.info("Do you want to load the permission addon [\"yes\", \"no\"]");
        String permissions = this.readString(loggerProvider, s -> s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("no"));
        if (Boolean.parseBoolean(permissions)) {
            DownloadManager.downloadAndDisconnect("PermissionsAddon", "https://dl.reformcloud.systems/addons/ReformCloudPermissions.jar", "reformcloud/addons/PermissionsAddon.jar");
        }
        
        loggerProvider.info("Please enter a language [\"german\", \"english\"]");
        String lang = this.readString(loggerProvider, s -> s.equalsIgnoreCase("german") || s.equalsIgnoreCase("english"));

        final String web = ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE)
                + StringUtil.EMPTY
                + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong(0, Long.MAX_VALUE);

        new Configuration().addProperty("users",
                Collections.singletonList(new WebUser("administrator", StringEncrypt.encrypt(web),
                        ReformCloudLibraryService.concurrentHashMap()))).write(Paths.get("reformcloud/users.json"));

        loggerProvider.info("New default WebUser \"administrator\" was created with password \"" + web + "\"");
        this.init(controllerIP, lang);

        return true;
    }

    private void load() {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get("configuration.properties")))) {
            properties.load(inputStreamReader);
        } catch (final IOException ex) {
            StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load configuration", ex);
        }

        this.host = properties.getProperty("server.ip");
        this.nettyAddress = new EthernetAddress(this.host, Integer.parseInt(properties.getProperty("server.port")));

        this.splitter = properties.getProperty("general.server-separator");

        if (Boolean.parseBoolean(properties.getProperty("webServer.enabled"))) {
            this.webAddress = new EthernetAddress(properties.getProperty("webServer.ip"), Integer.parseInt(properties.getProperty("webServer.port")));
        }
        this.certFile = properties.getProperty("ssl.certFilePath", StringUtil.NULL);
        this.keyFile = properties.getProperty("ssl.keyFilePath", StringUtil.NULL);
        this.loadedLang = properties.getProperty("general.language");

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/clients.json"));
        this.clients = configuration.getValue("client", new TypeToken<List<Client>>() {
        }.getType());
        this.webUsers = Configuration.parse(Paths.get("reformcloud/users.json")).getValue("users", new TypeToken<List<WebUser>>() {
        }.getType());

        this.controllerKey = FileUtils.readFileAsString(new File("reformcloud/files/ControllerKEY"));
    }

    private void loadServerGroups() {
        File dir = new File("reformcloud/groups/servers");
        Configuration configuration;

        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".json")) {
                    try {
                        configuration = Configuration.parse(file);
                        ServerGroup serverGroup = configuration.getValue("group", TypeTokenAdaptor.getSERVER_GROUP_TYPE());
                        serverGroups.add(serverGroup);
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load ServerGroup", throwable);
                        ReformCloudController.getInstance().getLoggerProvider().serve("Failed to load ServerGroup " + file.getName() + "!");
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
                        ProxyGroup proxyGroup = configuration.getValue("group", TypeTokenAdaptor.getPROXY_GROUP_TYPE());
                        proxyGroups.add(proxyGroup);
                    } catch (final Throwable throwable) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load ProxyGroup", throwable);
                        ReformCloudController.getInstance().getLoggerProvider().serve("Failed to load DefaultProxyGroup " + file.getName() + "!");
                    }
                }
            }
        }
    }

    private void loadMessages() {
        String messagePath = "reformcloud/messages.json";
        if (!Files.exists(Paths.get(messagePath))) {
            new Configuration()
                    .addStringProperty("internal-global-prefix", "§2R§feform§2C§floud §7┃ ")

                    .addStringProperty("internal-api-bungee-command-no-permission", "§cYou do not have permission to execute this command")

                    .addStringProperty("internal-api-bungee-command-hub-already", "%prefix% §7You are already connected to a hub server")
                    .addStringProperty("internal-api-bungee-command-hub-not-available", "%prefix% §7There is now hub server available")

                    .addStringProperty("internal-api-bungee-command-jumpto-server-player-not-found", "%prefix% §cCould not find player or server to go to")
                    .addStringProperty("internal-api-bungee-command-jumpto-success", "%prefix% §aYou was connected to the server")

                    .addStringProperty("internal-api-bungee-command-reformcloud-invalid-syntax", "%prefix% §7/reformcloud <command>")
                    .addStringProperty("internal-api-bungee-command-reformcloud-no-permission", "%prefix% §7Command not allowed")
                    .addStringProperty("internal-api-bungee-command-reformcloud-command-success", "%prefix% §7Command has been executed successfully \n §7Please check the Controller Console for more details")

                    .addStringProperty("internal-api-bungee-maintenance-join-no-permission", "§cWhitelist is enabled, but you are not added")
                    .addStringProperty("internal-api-bungee-connect-hub-no-server", "%prefix% §7There is no hub server available")

                    .addStringProperty("internal-api-bungee-startup-server", "%prefix% §7ServerProcess §6%server-name% §7is starting...")
                    .addStringProperty("internal-api-bungee-startup-proxy", "%prefix% §7ProxyProcess §6%proxy-name% §7is starting...")
                    .addStringProperty("internal-api-bungee-remove-server", "%prefix% §7ServerProcess §6%server-name% §7is stopping...")
                    .addStringProperty("internal-api-bungee-remove-proxy", "%prefix% §7ProxyProcess §6%proxy-name% §7is stopping...")

                    .addStringProperty("internal-api-spigot-connect-no-permission", "%prefix% §cYou do not have permission to join this server")
                    .addStringProperty("internal-api-spigot-connect-only-proxy", "%prefix% §cOnly Proxy join allowed")

                    .addStringProperty("internal-api-spigot-command-signs-not-enabled", "%prefix% §7Signs aren't enabled")
                    .addStringProperty("internal-api-spigot-command-signs-create-usage", "%prefix% §7/selectors selector signs new <group>")
                    .addStringProperty("internal-api-spigot-command-signs-create-success", "%prefix% §7Sign was created successfully")
                    .addStringProperty("internal-api-spigot-command-signs-create-already-exists", "%prefix% §7Sign already exits")
                    .addStringProperty("internal-api-spigot-command-signs-block-not-sign", "%prefix% §7Target block isn't a sign")
                    .addStringProperty("internal-api-spigot-command-signs-delete-success", "%prefix% §7Sign was deleted")
                    .addStringProperty("internal-api-spigot-command-signs-item-success", "%prefix% §7The Sign-Item was added to your Inventory")
                    .addStringProperty("internal-api-spigot-command-signs-delete-not-exists", "%prefix% §7Sign doesn't exits")
                    .addStringProperty("internal-api-spigot-command-signs-list", "%prefix% §7The Following Signs are registered:")
                    .addStringProperty("internal-api-spigot-command-signs-usage-1", "%prefix% §7/selectors selector <signs> new <group>")
                    .addStringProperty("internal-api-spigot-command-signs-usage-2", "%prefix% §7/selectors selector <signs> remove <group-name>")
                    .addStringProperty("internal-api-spigot-command-signs-usage-3", "%prefix% §7/selectors selector <signs> list")

                    .write(Paths.get(messagePath));
        }

        ReformCloudController.getInstance().getInternalCloudNetwork().setMessages(Configuration.parse(Paths.get(messagePath)));
        ReformCloudController.getInstance().getInternalCloudNetwork().setPrefix(ReformCloudController.getInstance().getInternalCloudNetwork().getMessages().getStringValue("internal-global-prefix"));
    }

    private void createTemplatesIfNotExists() {
        this.serverGroups.forEach(serverGroup ->
                serverGroup.getTemplates().forEach(template -> {
                    if (!Files.exists(Paths.get("reformcloud/templates/servers/" + serverGroup.getName() + "/" + template.getName())))
                        FileUtils.createDirectory(Paths.get("reformcloud/templates/servers/" + serverGroup.getName() + "/" + template.getName()));
                })
        );

        this.proxyGroups.forEach(proxyGroup ->
                proxyGroup.getTemplates().forEach(template -> {
                    if (!Files.exists(Paths.get("reformcloud/templates/proxies/" + proxyGroup.getName() + "/" + template.getName())))
                        FileUtils.createDirectory(Paths.get("reformcloud/templates/proxies/" + proxyGroup.getName() + "/" + template.getName()));
                })
        );
    }

    public void createServerGroup(final ServerGroup serverGroup) {
        new Configuration().addProperty("group", serverGroup).write(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));
        ReformCloudController.getInstance().getLoggerProvider().info("Loading ServerGroup [Name=" + serverGroup.getName() + "/Clients=" + serverGroup.getClients() + "]...");
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().put(serverGroup.getName(), serverGroup);
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void createProxyGroup(final ProxyGroup proxyGroup) {
        new Configuration().addProperty("group", proxyGroup).write(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));
        ReformCloudController.getInstance().getLoggerProvider().info("Loading ProxyGroup [Name=" + proxyGroup.getName() + "/Clients=" + proxyGroup.getClients() + "]...");
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().put(proxyGroup.getName(), proxyGroup);
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void createClient(final Client client) {
        Configuration configuration = Configuration.parse(Paths.get("reformcloud/clients.json"));

        List<Client> clients = configuration.getValue("client", new TypeToken<List<Client>>() {
        }.getType());
        clients.add(client);
        configuration.addProperty("client", clients).write(Paths.get("reformcloud/clients.json"));

        ReformCloudController.getInstance().getLoggerProvider().info("Loading Client [Name=" + client.getName() + "/Address=" + client.getIp() + "]...");
        ReformCloudController.getInstance().getInternalCloudNetwork().getClients().put(client.getName(), client);
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void createWebUser(final WebUser webUser) {
        Configuration configuration = Configuration.parse(Paths.get("reformcloud/users.json"));
        this.webUsers.add(webUser);
        configuration.addProperty("users", this.webUsers).write(Paths.get("reformcloud/users.json"));
    }

    public void updateWebUser(final WebUser webUser) {
        List<WebUser> users = new ArrayList<>(this.webUsers);
        users.parallelStream().filter(e -> webUser.getUser().equals(e.getUser())).forEach(e -> this.webUsers.remove(e));
        this.webUsers.add(webUser);
        Configuration.parse(Paths.get("reformcloud/users.json"))
                .addProperty("users", this.webUsers)
                .write(Paths.get("reformcloud/users.json"));
    }

    public void updateServerGroup(final ServerGroup serverGroup) {
        try {
            Files.delete(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Configuration().addProperty("group", serverGroup).write(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().remove(serverGroup.getName());
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().put(serverGroup.getName(), serverGroup);

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void updateProxyGroup(final ProxyGroup proxyGroup) {
        try {
            Files.delete(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Configuration().addProperty("group", proxyGroup).write(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().remove(proxyGroup.getName());
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().put(proxyGroup.getName(), proxyGroup);

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void deleteServerGroup(final ServerGroup serverGroup) throws IOException {
        Files.delete(Paths.get("reformcloud/groups/servers/" + serverGroup.getName() + ".json"));

        ReformCloudController.getInstance().getLoggerProvider().info("Deleting ServerGroup [Name=" + serverGroup.getName() + "/Clients=" + serverGroup.getClients() + "]...");
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().remove(serverGroup.getName());
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredServerProcesses().stream()
                .filter(e -> e.getServerGroup().getName().equals(serverGroup.getName()))
                .forEach(e -> {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(e.getCloudProcess().getClient(), new PacketOutStopProcess(e.getCloudProcess().getName()));
                    ReformCloudLibraryService.sleep(2000);
                });
    }

    public void deleteProxyGroup(final ProxyGroup proxyGroup) throws IOException {
        Files.delete(Paths.get("reformcloud/groups/proxies/" + proxyGroup.getName() + ".json"));

        ReformCloudController.getInstance().getLoggerProvider().info("Deleting ProxyGroup [Name=" + proxyGroup.getName() + "/Clients=" + proxyGroup.getClients() + "]...");
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().remove(proxyGroup.getName());
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getAllRegisteredProxyProcesses().stream()
                .filter(e -> e.getProxyGroup().getName().equals(proxyGroup.getName()))
                .forEach(e -> {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(e.getCloudProcess().getClient(), new PacketOutStopProcess(e.getCloudProcess().getName()));
                    ReformCloudLibraryService.sleep(2000);
                });
    }

    public void deleteClient(final Client client) {
        Configuration configuration = Configuration.parse(Paths.get("reformcloud/clients.json"));

        List<Client> clients = configuration.getValue("client", new TypeToken<List<Client>>() {
        }.getType());
        clients.remove(client);
        configuration.addProperty("client", clients).write(Paths.get("reformcloud/clients.json"));

        ReformCloudController.getInstance().getLoggerProvider().info("Deleting Client [Name=" + client.getName() + "/Address=" + client.getIp() + "]...");
        ReformCloudController.getInstance().getInternalCloudNetwork().getClients().remove(client.getName());
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());

        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()));
    }

    public void addPlayerToWhitelist(final String group, UUID playerUuid) {
        ProxyGroup proxyGroup = ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(group);
        proxyGroup.getWhitelist().add(playerUuid);
        Configuration.parse(Paths.get("reformcloud/groups/proxies/" + group + ".json")).addProperty("group", proxyGroup).write(Paths.get("reformcloud/groups/proxies/" + group + ".json"));
    }

    public void removePlayerFromWhitelist(final String group, final UUID playerUuid) {
        ProxyGroup proxyGroup = ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(group);
        proxyGroup.getWhitelist().remove(playerUuid);
        Configuration.parse(Paths.get("reformcloud/groups/proxies/" + group + ".json")).addProperty("group", proxyGroup).write(Paths.get("reformcloud/groups/proxies/" + group + ".json"));
    }

    public String readString(final LoggerProvider loggerProvider, Checkable<String> checkable) {
        String readLine = loggerProvider.readLine();
        while (readLine == null || !checkable.isChecked(readLine) || readLine.trim().isEmpty()) {
            loggerProvider.info("Input invalid, please try again");
            readLine = loggerProvider.readLine();
        }

        return readLine;
    }

    private Integer readInt(final LoggerProvider loggerProvider, Checkable<Integer> checkable) {
        String readLine = loggerProvider.readLine();
        while (readLine == null || readLine.trim().isEmpty() || !ReformCloudLibraryService.checkIsInteger(readLine) || !checkable.isChecked(Integer.parseInt(readLine))) {
            loggerProvider.info("Input invalid, please try again");
            readLine = loggerProvider.readLine();
        }

        return Integer.parseInt(readLine);
    }
}
