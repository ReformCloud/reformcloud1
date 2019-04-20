/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.configuration.CloudConfiguration;
import systems.reformcloud.cryptic.StringEncrypt;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.logging.LoggerProvider;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.defaults.DefaultProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.defaults.DefaultGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.meta.web.WebUser;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public final class CommandCreate extends Command implements Serializable {
    public CommandCreate() {
        super("create", "Creates a new ServerGroup, ProxyGroup or Client", "reformcloud.command.create", new String[0]);
    }

    private final Language language = ReformCloudController.getInstance().getLoadedLanguage();

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("servergroup")) {
            LoggerProvider loggerProvider = ReformCloudController.getInstance().getLoggerProvider();
            CloudConfiguration cloudConfiguration = ReformCloudController.getInstance().getCloudConfiguration();

            loggerProvider.info(language.getSetup_name_of_group());
            String name = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().get(s) == null);
            loggerProvider.info(language.getSetup_name_of_client());
            String client = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(s) != null);

            loggerProvider.info(language.getSetup_choose_minecraft_version());
            SpigotVersions.AVAILABLE_VERSIONS.forEach(e -> loggerProvider.info(e));
            String version = cloudConfiguration.readString(loggerProvider, s -> SpigotVersions.AVAILABLE_VERSIONS.contains(s));
            loggerProvider.info(language.getSetup_choose_spigot_version());
            SpigotVersions.sortedByVersion(version).values().forEach(e -> loggerProvider.info("   " + e.name()));
            String provider = cloudConfiguration.readString(loggerProvider, s -> SpigotVersions.getByName(s) != null);

            loggerProvider.info(language.getSetup_choose_reset_type());
            String resetType = cloudConfiguration.readString(loggerProvider, s -> s.equalsIgnoreCase("dynamic") || s.equalsIgnoreCase("static") || s.equalsIgnoreCase("lobby"));

            commandSender.sendMessage(language.getSetup_trying_to_create().replace("%group%", name));
            ReformCloudController.getInstance().getCloudConfiguration().createServerGroup(new DefaultGroup(name, client, SpigotVersions.getByName(provider), ServerModeType.of(resetType)));
            return;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("proxygroup")) {
            LoggerProvider loggerProvider = ReformCloudController.getInstance().getLoggerProvider();
            CloudConfiguration cloudConfiguration = ReformCloudController.getInstance().getCloudConfiguration();

            loggerProvider.info(language.getSetup_name_of_group());
            String name = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(s) == null);
            loggerProvider.info(language.getSetup_name_of_client());
            String client = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(s) != null);

            loggerProvider.info(language.getSetup_choose_proxy_version());
            ProxyVersions.sorted().values().forEach(e -> loggerProvider.info("   " + e.name()));
            String in = cloudConfiguration.readString(loggerProvider, s -> ProxyVersions.getByName(s) != null);

            commandSender.sendMessage(language.getSetup_trying_to_create().replace("%group%", name));
            ReformCloudController.getInstance().getCloudConfiguration().createProxyGroup(new DefaultProxyGroup(name, client, ProxyVersions.getByName(in)));
            return;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("client")) {
            LoggerProvider loggerProvider = ReformCloudController.getInstance().getLoggerProvider();
            CloudConfiguration cloudConfiguration = ReformCloudController.getInstance().getCloudConfiguration();

            loggerProvider.info(language.getSetup_name_of_new_client());
            String name = cloudConfiguration.readString(loggerProvider, s -> ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(s) == null);
            loggerProvider.info(language.getSetup_ip_of_new_client());
            String ip = cloudConfiguration.readString(loggerProvider, s -> s.split("\\.").length == 4);

            commandSender.sendMessage(language.getSetup_trying_to_create().replace("%group%", name));
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
                    commandSender.sendMessage(language.getCommand_error_occurred().replace("%message%", "The webuser already exists"));
                    return;
                }

                final WebUser webUser = new WebUser(args[1], StringEncrypt.encryptSHA512(args[2]), new HashMap<>());
                ReformCloudController.getInstance().getCloudConfiguration().createWebUser(webUser);
                commandSender.sendMessage(
                        language.getCommand_create_webuser_created()
                                .replace("%name%", webUser.getUser())
                                .replace("%password%", args[2])
                );
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
                    if (serverGroup.getTemplateOrElseNull(args[2]) != null) {
                        commandSender.sendMessage(
                                language.getCommand_error_occurred().replace("%message%", "Template already exists")
                        );
                        return;
                    }

                    serverGroup.getTemplates().add(new Template(args[2], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                    commandSender.sendMessage(language.getCommand_create_template_created_success());
                    return;
                } else {
                    ProxyGroup proxyGroup = ReformCloudController
                            .getInstance()
                            .getInternalCloudNetwork()
                            .getProxyGroups()
                            .get(args[1]);
                    if (proxyGroup == null) {
                        commandSender.sendMessage(
                                language.getCommand_error_occurred().replace("%message%", "Group doesn't exists")
                        );
                        return;
                    }

                    if (proxyGroup.getTemplateOrElseNull(args[2]) != null) {
                        commandSender.sendMessage(
                                language.getCommand_error_occurred().replace("%message%", "Template already exists")
                        );
                        return;
                    }

                    proxyGroup.getTemplates().add(new Template(args[2], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    commandSender.sendMessage(language.getCommand_create_template_created_success());
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
