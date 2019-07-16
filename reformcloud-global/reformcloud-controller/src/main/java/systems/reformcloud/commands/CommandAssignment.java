/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.language.utility.Language;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.client.settings.ClientSettings;
import systems.reformcloud.meta.enums.ProxyModeType;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.network.out.PacketOutUpdateClientSetting;

/**
 * @author _Klaro | Pasqual K. / created on 26.03.2019
 */

public final class CommandAssignment extends Command implements Serializable {

    public CommandAssignment() {
        super(
            "assignment",
            "Changes the given setting of a servergroup/proxygroup or client",
            "reformcloud.command.assignment",
            new String[]{"settings", "asg", "service"}
        );
    }

    private final Language language = ReformCloudController.getInstance().getLoadedLanguage();

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if ((args.length == 5 || args.length == 4) && args[0].equalsIgnoreCase("servergroup")) {
            ServerGroup serverGroup = ReformCloudController.getInstance().getServerGroup(args[1]);
            if (serverGroup == null) {
                commandSender.sendMessage(language.getServergroup_not_found());
                return;
            }

            if (args[2].equalsIgnoreCase("permission")) {
                serverGroup.setJoinPermission(args[3]);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", "JoinPermission")
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("clients")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (!serverGroup.getClients().contains(args[3])) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%",
                                    "The client isn't available for the servergroup"));
                        return;
                    }

                    serverGroup.getClients().remove(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_removed()
                        .replace("%value%", args[3])
                        .replace("%group%", serverGroup.getName()));
                } else {
                    if (ReformCloudController.getInstance().getClient(args[3]) == null) {
                        commandSender.sendMessage(language.getClient_not_found());
                        return;
                    }

                    if (serverGroup.getClients().contains(args[3])) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%",
                                    "The client is already added to the servergroup"));
                        return;
                    }

                    serverGroup.getClients().add(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_added()
                        .replace("%value%", args[3])
                        .replace("%group%", serverGroup.getName()));
                }

                return;
            }

            if (args[2].equalsIgnoreCase("templates")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (serverGroup.getTemplateOrElseNull(args[3]) == null) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%", "The template doesn't exists"));
                        return;
                    }

                    serverGroup.deleteTemplate(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_removed()
                        .replace("%value%", args[3])
                        .replace("%group%", serverGroup.getName()));
                } else {
                    if (serverGroup.getTemplateOrElseNull(args[3]) != null) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%", "The template already exists"));
                        return;
                    }

                    serverGroup.getTemplates()
                        .add(new Template(args[3], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_added()
                        .replace("%value%", args[3])
                        .replace("%group%", serverGroup.getName()));
                }

                return;
            }

            if (args[2].equalsIgnoreCase("memory")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 50) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 50"));
                    return;
                }

                serverGroup.setMemory(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("minonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 0"));
                    return;
                }

                serverGroup.setMinOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("maxonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < -1) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than -2"));
                    return;
                }

                serverGroup.setMaxOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("maxplayers")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 0"));
                    return;
                }

                serverGroup.setMaxPlayers(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("startport")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 1000) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 1000"));
                    return;
                }

                serverGroup.setStartPort(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("maintenance")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a boolean as argument"));
                    return;
                }

                boolean maintenance = Boolean.parseBoolean(args[3]);
                serverGroup.setMaintenance(maintenance);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("savelogs")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a boolean as argument"));
                    return;
                }

                boolean save = Boolean.parseBoolean(args[3]);
                serverGroup.setSaveLogs(save);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("servermodetype")) {
                if (!args[3].equalsIgnoreCase("lobby")
                    && !args[3].equalsIgnoreCase("static")
                    && !args[3].equalsIgnoreCase("dynamic")) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a valid reset type"));
                    return;
                }

                serverGroup.setServerModeType(ServerModeType.valueOf(args[3].toUpperCase()));
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("version")) {
                if (SpigotVersions.getByName(args[3]) == null) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a valid server version"));
                    return;
                }

                serverGroup.setSpigotVersions(SpigotVersions.getByName(args[3]));
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("onlyproxyjoin")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a boolean as argument"));
                    return;
                }

                boolean save = Boolean.parseBoolean(args[3]);
                serverGroup.setOnlyProxyJoin(save);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", serverGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            this.sendHelp(commandSender);
        } else if ((args.length == 5 || args.length == 4) && args[0]
            .equalsIgnoreCase("proxygroup")) {
            ProxyGroup proxyGroup = ReformCloudController.getInstance().getProxyGroup(args[1]);
            if (proxyGroup == null) {
                commandSender.sendMessage(language.getProxygroup_not_found());
                return;
            }

            if (args[2].equalsIgnoreCase("clients")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (!proxyGroup.getClients().contains(args[3])) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%", "The client isn't added to the proxy group"));
                        return;
                    }

                    proxyGroup.getClients().remove(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_removed()
                        .replace("%value%", args[3])
                        .replace("%group%", proxyGroup.getName()));
                } else {
                    if (ReformCloudController.getInstance().getClient(args[3]) == null) {
                        commandSender.sendMessage(language.getClient_not_found());
                        return;
                    }

                    if (proxyGroup.getClients().contains(args[3])) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%",
                                    "The client is already added to the proxy group"));
                        return;
                    }

                    proxyGroup.getClients().add(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_added()
                        .replace("%value%", args[3])
                        .replace("%group%", proxyGroup.getName()));
                }

                return;
            }

            if (args[2].equalsIgnoreCase("disabledgroups")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (!proxyGroup.getDisabledServerGroups().contains(args[3])) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%", "The server group isn't ignored"));
                        return;
                    }

                    proxyGroup.getDisabledServerGroups().remove(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_removed()
                        .replace("%value%", args[3])
                        .replace("%group%", proxyGroup.getName()));
                } else {
                    if (proxyGroup.getDisabledServerGroups().contains(args[3])) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%", "The server group is already ignored"));
                        return;
                    }

                    proxyGroup.getClients().add(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_added()
                        .replace("%value%", args[3])
                        .replace("%group%", proxyGroup.getName()));
                }

                return;
            }

            if (args[2].equalsIgnoreCase("templates")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (proxyGroup.getTemplateOrElseNull(args[3]) == null) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%", "The template doesn't exists"));
                        return;
                    }

                    proxyGroup.deleteTemplate(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_removed()
                        .replace("%value%", args[3])
                        .replace("%group%", proxyGroup.getName()));
                } else {
                    if (proxyGroup.getTemplateOrElseNull(args[3]) != null) {
                        commandSender
                            .sendMessage(language.getCommand_assignment_value_not_updatable()
                                .replace("%reason%", "The template already exists"));
                        return;
                    }

                    proxyGroup.getTemplates()
                        .add(new Template(args[3], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration()
                        .updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage(language.getCommand_assignment_value_added()
                        .replace("%value%", args[3])
                        .replace("%group%", proxyGroup.getName()));
                }

                return;
            }

            if (args[2].equalsIgnoreCase("maintenance")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a boolean as argument"));
                    return;
                }

                boolean maintenance = Boolean.parseBoolean(args[3]);
                proxyGroup.setMaintenance(maintenance);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("savelogs")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a boolean as argument"));
                    return;
                }

                boolean save = Boolean.parseBoolean(args[3]);
                proxyGroup.setSaveLogs(save);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("commandlogging")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a boolean as argument"));
                    return;
                }

                boolean save = Boolean.parseBoolean(args[3]);
                proxyGroup.setControllerCommandLogging(save);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("memory")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 50) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 50"));
                    return;
                }

                proxyGroup.setMemory(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("minonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 0"));
                    return;
                }

                proxyGroup.setMinOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("maxonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < -1) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than -2"));
                    return;
                }

                proxyGroup.setMaxOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("maxplayers")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 0"));
                    return;
                }

                proxyGroup.setMaxPlayers(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("startport")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number as argument"));
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 1000) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 1000"));
                    return;
                }

                proxyGroup.setStartPort(integer);
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("version")) {
                if (ProxyVersions.getByName(args[3]) == null) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a valid version"));
                    return;
                }

                proxyGroup.setProxyVersions(ProxyVersions.getByName(args[3]));
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("proxymodetype")) {
                if (!args[3].equalsIgnoreCase("static")
                    && !args[3].equalsIgnoreCase("dynamic")) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a valid reset type"));
                    return;
                }

                proxyGroup.setProxyModeType(ProxyModeType.valueOf(args[3].toUpperCase()));
                ReformCloudController.getInstance().getCloudConfiguration()
                    .updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", proxyGroup.getName())
                    .replace("%value%", args[3]));
                return;
            }

            this.sendHelp(commandSender);
        } else if (args.length == 4 && args[0].equalsIgnoreCase("client")) {
            Client client = ReformCloudController.getInstance().getClient(args[1]);
            if (client == null) {
                commandSender.sendMessage(language.getClient_not_found());
                return;
            }

            if (!ReformCloudController.getInstance().getChannelHandler()
                .isChannelRegistered(client.getName())) {
                commandSender.sendMessage(language.getClient_not_connected());
                return;
            }

            if (args[2].equalsIgnoreCase("starthost")) {
                if (args[3].split("\\.").length != 4) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a valid ip address"));
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    client.getName(),
                    new PacketOutUpdateClientSetting(ClientSettings.START_HOST, args[3])
                );
                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", client.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("memory")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])
                    || Integer.valueOf(args[3]) < 100) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 100"));
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    client.getName(),
                    new PacketOutUpdateClientSetting(ClientSettings.MEMORY, args[3])
                );
                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", client.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("maxcpu")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])
                    || Integer.valueOf(args[3]) < 10) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 10"));
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    client.getName(),
                    new PacketOutUpdateClientSetting(ClientSettings.MAX_CPU_USAGE, args[3])
                );
                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", client.getName())
                    .replace("%value%", args[3]));
                return;
            }

            if (args[2].equalsIgnoreCase("maxlogsize")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])
                    || Integer.valueOf(args[3]) < 5) {
                    commandSender.sendMessage(language.getCommand_assignment_value_not_updatable()
                        .replace("%reason%", "Please provide a number bigger than 5"));
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    client.getName(),
                    new PacketOutUpdateClientSetting(ClientSettings.MAX_LOG_SIZE, args[3])
                );
                commandSender.sendMessage(language.getCommand_assignment_value_updated()
                    .replace("%name%", args[2].toLowerCase())
                    .replace("%group%", client.getName())
                    .replace("%value%", args[3]));
                return;
            }

            this.sendHelp(commandSender);
        } else {
            this.sendHelp(commandSender);
        }
    }

    private void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage(
            "assignment SERVERGROUP <name> <permission, clients, templates, memory, maxonline, " +
                "minonline, maxplayers, startport, maintenance, savelogs, servermodetype, version> <value> <--update>");
        commandSender.sendMessage(
            "assignment PROXYGROUP <name> <clients, templates, disabledgroups, maintenance, " +
                "savelogs, memory, maxonline, minonline, proxymodetype, maxplayers, commandlogging, version> <value> <--update>");
        commandSender.sendMessage(
            "assignment CLIENT <name> <starthost, memory, maxcpu, maxlogsize> <value>");
    }

    @Override
    public List<String> complete(String commandLine, String[] args) {
        List<String> out = new LinkedList<>();

        if (args.length == 0) {
            out.addAll(asList("SERVERGROUP", "PROXYGROUP", "CLIENT"));
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("servergroup")) {
                out.addAll(serverGroups());
            } else if (args[0].equalsIgnoreCase("proxygroup")) {
                out.addAll(proxyGroups());
            } else if (args[0].equalsIgnoreCase("client")) {
                out.addAll(clients());
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("servergroup")) {
                out.addAll(asList("permission", "clients", "templates",
                    "memory", "maxonline", "minonline", "maxplayers", "startport",
                    "maintenance", "savelogs", "servermodetype", "version", "onlyproxyjoin"));
            } else if (args[0].equalsIgnoreCase("proxygroup")) {
                out.addAll(asList("clients", "templates", "disabledgroups",
                    "maintenance", "savelogs", "memory", "maxonline", "minonline",
                    "proxymodetype", "maxplayers", "commandlogging", "version"));
            } else if (args[0].equalsIgnoreCase("client")) {
                out.addAll(asList("starthost", "memory", "maxcpu", "maxlogsize"));
            }
        }

        if (args.length == 4) {
            out.add("--update");
        }
        return out;
    }

    private List<String> serverGroups() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getAllServerGroups()
            .forEach(group -> out.add(group.getName()));
        Collections.sort(out);
        return out;
    }

    private List<String> proxyGroups() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getAllProxyGroups()
            .forEach(group -> out.add(group.getName()));
        Collections.sort(out);
        return out;
    }

    private List<String> clients() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getAllConnectedClients()
            .forEach(client -> out.add(client.getName()));
        Collections.sort(out);
        return out;
    }
}
