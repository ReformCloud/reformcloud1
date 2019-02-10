/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.defaults.DefaultProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.defaults.DefaultGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandCreate extends Command implements Serializable {
    public CommandCreate() {
        super("create", "Creates a new ServerGroup, ProxyGroup or Client", "reformcloud.command.create", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("servergroup")) {
            LoggerProvider loggerProvider = ReformCloudController.getInstance().getLoggerProvider();
            CloudConfiguration cloudConfiguration = ReformCloudController.getInstance().getCloudConfiguration();

            loggerProvider.info("Please enter the name of the ServerGroup");
            String name = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().get(s) == null);
            loggerProvider.info("Please enter the client of the ServerGroup");
            String client = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(s) != null);

            loggerProvider.info("Please choose a minecraft version");
            SpigotVersions.AVAILABLE_VERSIONS.forEach(e -> loggerProvider.info(e));
            String version = cloudConfiguration.readString(loggerProvider, s -> SpigotVersions.AVAILABLE_VERSIONS.contains(s));
            loggerProvider.info("Which Spigot-Version should be used?");
            SpigotVersions.sortedByVersion(version).values().forEach(e -> loggerProvider.info("   " + e.name()));
            String provider = cloudConfiguration.readString(loggerProvider, s -> SpigotVersions.getByName(s) != null);

            commandSender.sendMessage("Trying to create new ServerGroup...");
            ReformCloudController.getInstance().getCloudConfiguration().createServerGroup(new DefaultGroup(name, client, SpigotVersions.getByName(provider)));
            return;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("proxygroup")) {
            LoggerProvider loggerProvider = ReformCloudController.getInstance().getLoggerProvider();
            CloudConfiguration cloudConfiguration = ReformCloudController.getInstance().getCloudConfiguration();

            loggerProvider.info("Please enter the name of the ProxyGroup");
            String name = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(s) == null);
            loggerProvider.info("Please enter the client of the ProxyGroup");
            String client = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(s) != null);

            loggerProvider.info("Which Proxy-Version should be used?");
            ProxyVersions.sorted().values().forEach(e -> loggerProvider.info("   " + e.name()));
            String in = cloudConfiguration.readString(loggerProvider, s -> ProxyVersions.getByName(s) != null);

            commandSender.sendMessage("Trying to create new ProxyGroup...");
            ReformCloudController.getInstance().getCloudConfiguration().createProxyGroup(new DefaultProxyGroup(name, client, ProxyVersions.getByName(in)));
            return;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("client")) {
            LoggerProvider loggerProvider = ReformCloudController.getInstance().getLoggerProvider();
            CloudConfiguration cloudConfiguration = ReformCloudController.getInstance().getCloudConfiguration();

            loggerProvider.info("Please enter the name of the client");
            String name = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(s) == null);
            loggerProvider.info("Please enter the name of the client");
            String ip = cloudConfiguration.readString(loggerProvider, s -> s.split("\\.").length == 4);

            commandSender.sendMessage("Trying to create new Client...");
            ReformCloudController.getInstance().getCloudConfiguration().createClient(new Client(name, ip, null));
            return;
        }

        if (args.length < 2) {
            commandSender.sendMessage("create CLIENT");
            commandSender.sendMessage("create SERVERGROUP");
            commandSender.sendMessage("create PROXYGROUP");
            commandSender.sendMessage("create WEBUSER <name> <password>");
            commandSender.sendMessage("create TEMPLATE <group> <name>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "webuser": {
                if (args.length != 3) {
                    commandSender.sendMessage("create user <name> <password>");
                    return;
                }

                if (ReformCloudController.getInstance()
                        .getCloudConfiguration()
                        .getWebUsers()
                        .stream()
                        .filter(e -> e.getUser().equals(args[1]))
                        .findFirst()
                        .orElse(null) != null) {
                    commandSender.sendMessage("WebUser already exists");
                    return;
                }

                final WebUser webUser = new WebUser(args[1], StringEncrypt.encrypt(args[2]), new HashMap<>());
                ReformCloudController.getInstance().getCloudConfiguration().createWebUser(webUser);
                commandSender.sendMessage("WebUser \"" + webUser.getUser() + "\" was created successfully with password \"" + args[2] + "\"");
                break;
            }
            case "template": {
                if (args.length != 3) {
                    commandSender.sendMessage("create TEMPLATE <group> <name>");
                    return;
                }

                ServerGroup serverGroup = ReformCloudController
                        .getInstance()
                        .getInternalCloudNetwork()
                        .getServerGroups()
                        .get(args[1]);
                if (serverGroup != null) {
                    if (serverGroup.getTemplate(args[2]) != null) {
                        commandSender.sendMessage("Template already exists");
                        return;
                    }

                    serverGroup.getTemplates().add(new Template(args[2], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                    commandSender.sendMessage("Template was created successfully");
                    return;
                } else {
                    ProxyGroup proxyGroup = ReformCloudController
                            .getInstance()
                            .getInternalCloudNetwork()
                            .getProxyGroups()
                            .get(args[1]);
                    if (proxyGroup == null) {
                        commandSender.sendMessage("Group doesn't exists");
                        return;
                    }

                    if (proxyGroup.getTemplate(args[2]) != null) {
                        commandSender.sendMessage("Template already exists");
                        return;
                    }

                    proxyGroup.getTemplates().add(new Template(args[2], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    commandSender.sendMessage("Template was created successfully");
                }
                break;
            }
            default:
                commandSender.sendMessage("create CLIENT");
                commandSender.sendMessage("create SERVERGROUP");
                commandSender.sendMessage("create PROXYGROUP");
                commandSender.sendMessage("create WEBUSER <name> <password>");
                commandSender.sendMessage("create TEMPLATE <group> <name>");
        }
    }
}
