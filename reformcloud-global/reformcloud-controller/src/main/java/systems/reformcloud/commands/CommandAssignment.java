/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.client.settings.ClientSettings;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.proxy.versions.ProxyVersions;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;
import systems.reformcloud.network.out.PacketOutUpdateClientSetting;

import java.io.Serializable;

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

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if ((args.length == 5 || args.length == 4) && args[0].equalsIgnoreCase("servergroup")) {
            ServerGroup serverGroup = ReformCloudController.getInstance().getServerGroup(args[1]);
            if (serverGroup == null) {
                commandSender.sendMessage("The specified servergroup doesn't exists");
                return;
            }

            if (args[2].equalsIgnoreCase("permission")) {
                serverGroup.setJoin_permission(args[3]);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("You set the join permission for the servergroup §e" + serverGroup.getName() + "§r to §e" + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("clients")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (!serverGroup.getClients().contains(args[3])) {
                        commandSender.sendMessage("The client isn't available for the servergroup");
                        return;
                    }

                    serverGroup.getClients().remove(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("You §asuccessfully §rremoved the client §e" + args[3]);
                } else {
                    if (serverGroup.getClients().contains(args[3])) {
                        commandSender.sendMessage("The client cannot added to the servergroup");
                        return;
                    }

                    serverGroup.getClients().add(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("You §asuccessfully §radded the client §e" + args[3]);
                }

                return;
            }

            if (args[2].equalsIgnoreCase("templates")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (serverGroup.getTemplateOrElseNull(args[3]) == null) {
                        commandSender.sendMessage("Cannot remove template from servergroup");
                        return;
                    }

                    serverGroup.deleteTemplate(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("§aSuccessfully §rdeleted template §e" + args[3]);
                } else {
                    if (serverGroup.getTemplateOrElseNull(args[3]) != null) {
                        commandSender.sendMessage("Cannot add template to the servergroup");
                        return;
                    }

                    serverGroup.getTemplates().add(new Template(args[3], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("§aSuccessfully §rcreated template §e" + args[3]);
                }

                return;
            }

            if (args[2].equalsIgnoreCase("memory")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 50) {
                    commandSender.sendMessage("Please provide a number bigger than 50");
                    return;
                }

                serverGroup.setMemory(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the memory to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("minonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage("Please provide a number bigger than 0");
                    return;
                }

                serverGroup.setMinOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the minonline count to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("maxonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < -1) {
                    commandSender.sendMessage("Please provide a number bigger than -2");
                    return;
                }

                serverGroup.setMaxOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the maxonline count to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("maxplayers")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage("Please provide a number bigger than 0");
                    return;
                }

                serverGroup.setMaxPlayers(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the maxplayer count to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("startport")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 1000) {
                    commandSender.sendMessage("Please provide a number bigger than 1000");
                    return;
                }

                serverGroup.setStartPort(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the startport to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("maintenance")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage("Please provide a boolean as value");
                    return;
                }

                boolean maintenance = Boolean.parseBoolean(args[3]);
                serverGroup.setMaintenance(maintenance);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the maintenance mode");
                return;
            }

            if (args[2].equalsIgnoreCase("savelogs")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage("Please provide a boolean as value");
                    return;
                }

                boolean save = Boolean.parseBoolean(args[3]);
                serverGroup.setSave_logs(save);
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the save logs mode");
                return;
            }

            if (args[2].equalsIgnoreCase("servermodetype")) {
                if (!args[3].equalsIgnoreCase("lobby")
                        && !args[3].equalsIgnoreCase("static")
                        && !args[3].equalsIgnoreCase("dynamic")) {
                    commandSender.sendMessage("Please provide a valid reset type");
                    return;
                }

                serverGroup.setServerModeType(ServerModeType.valueOf(args[3].toUpperCase()));
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the servergroup mode");
                return;
            }

            if (args[2].equalsIgnoreCase("version")) {
                if (SpigotVersions.getByName(args[3]) == null) {
                    commandSender.sendMessage("Please provide a valid serverVersion");
                    return;
                }

                serverGroup.setSpigotVersions(SpigotVersions.getByName(args[3]));
                ReformCloudController.getInstance().getCloudConfiguration().updateServerGroup(serverGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the server version");
                return;
            }

            this.sendHelp(commandSender);
        } else if ((args.length == 5 || args.length == 4) && args[0].equalsIgnoreCase("proxygroup")) {
            ProxyGroup proxyGroup = ReformCloudController.getInstance().getProxyGroup(args[1]);
            if (proxyGroup == null) {
                commandSender.sendMessage("The specified proxygroup doesn't exists");
                return;
            }

            if (args[2].equalsIgnoreCase("clients")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (!proxyGroup.getClients().contains(args[3])) {
                        commandSender.sendMessage("The client isn't available for the proxyGroup");
                        return;
                    }

                    proxyGroup.getClients().remove(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("You §asuccessfully §rremoved the client §e" + args[3]);
                } else {
                    if (proxyGroup.getClients().contains(args[3])) {
                        commandSender.sendMessage("The client cannot added to the proxyGroup");
                        return;
                    }

                    proxyGroup.getClients().add(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("You §asuccessfully §radded the client §e" + args[3]);
                }

                return;
            }

            if (args[2].equalsIgnoreCase("disabledgroups")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (!proxyGroup.getDisabledServerGroups().contains(args[3])) {
                        commandSender.sendMessage("The servergroup isn't ignored");
                        return;
                    }

                    proxyGroup.getDisabledServerGroups().remove(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("You §asuccessfully §rremoved the group §e" + args[3]);
                } else {
                    if (proxyGroup.getDisabledServerGroups().contains(args[3])) {
                        commandSender.sendMessage("The servergroup cannot be ignored");
                        return;
                    }

                    proxyGroup.getClients().add(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("You §asuccessfully §radded the group §e" + args[3]);
                }

                return;
            }

            if (args[2].equalsIgnoreCase("templates")) {
                boolean toRemove = args[3].startsWith("-");
                if (toRemove) {
                    args[3] = args[3].replaceFirst("-", "");

                    if (proxyGroup.getTemplateOrElseNull(args[3]) == null) {
                        commandSender.sendMessage("Cannot remove template from proxygroup");
                        return;
                    }

                    proxyGroup.deleteTemplate(args[3]);
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("§aSuccessfully §rdeleted template §e" + args[3]);
                } else {
                    if (proxyGroup.getTemplateOrElseNull(args[3]) != null) {
                        commandSender.sendMessage("Cannot add template to the proxygroup");
                        return;
                    }

                    proxyGroup.getTemplates().add(new Template(args[3], null, TemplateBackend.CLIENT));
                    ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                    if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                        ReformCloudController.getInstance().reloadAllSave();
                    }

                    commandSender.sendMessage("§aSuccessfully §rcreated template §e" + args[3]);
                }

                return;
            }

            if (args[2].equalsIgnoreCase("maintenance")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage("Please provide a boolean as value");
                    return;
                }

                boolean maintenance = Boolean.parseBoolean(args[3]);
                proxyGroup.setMaintenance(maintenance);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the maintenance mode");
                return;
            }

            if (args[2].equalsIgnoreCase("savelogs")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage("Please provide a boolean as value");
                    return;
                }

                boolean save = Boolean.parseBoolean(args[3]);
                proxyGroup.setSave_logs(save);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the save logs mode");
                return;
            }

            if (args[2].equalsIgnoreCase("commandlogging")) {
                if (!ReformCloudLibraryService.checkIsValidBoolean(args[3])) {
                    commandSender.sendMessage("Please provide a boolean as value");
                    return;
                }

                boolean save = Boolean.parseBoolean(args[3]);
                proxyGroup.setControllerCommandLogging(save);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the controller logging mode");
                return;
            }

            if (args[2].equalsIgnoreCase("memory")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 50) {
                    commandSender.sendMessage("Please provide a number bigger than 50");
                    return;
                }

                proxyGroup.setMemory(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the memory to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("minonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage("Please provide a number bigger than 0");
                    return;
                }

                proxyGroup.setMinOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the minonline count to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("maxonline")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < -1) {
                    commandSender.sendMessage("Please provide a number bigger than -2");
                    return;
                }

                proxyGroup.setMaxOnline(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the maxonline count to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("maxplayers")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 0) {
                    commandSender.sendMessage("Please provide a number bigger than 0");
                    return;
                }

                proxyGroup.setMaxPlayers(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the maxplayer count to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("startport")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3])) {
                    commandSender.sendMessage("Please provide a number as value");
                    return;
                }

                int integer = Integer.parseInt(args[3]);
                if (integer < 1000) {
                    commandSender.sendMessage("Please provide a number bigger than 1000");
                    return;
                }

                proxyGroup.setStartPort(integer);
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rset the the startport to " + args[3]);
                return;
            }

            if (args[2].equalsIgnoreCase("version")) {
                if (ProxyVersions.getByName(args[3]) == null) {
                    commandSender.sendMessage("Please provide a valid serverVersion");
                    return;
                }

                proxyGroup.setProxyVersions(ProxyVersions.getByName(args[3]));
                ReformCloudController.getInstance().getCloudConfiguration().updateProxyGroup(proxyGroup);
                if (args.length == 5 && args[4].equalsIgnoreCase("--update")) {
                    ReformCloudController.getInstance().reloadAllSave();
                }

                commandSender.sendMessage("§aSuccessfully §rupdated the server version");
                return;
            }

            this.sendHelp(commandSender);
        } else if (args.length == 4 && args[0].equalsIgnoreCase("client")) {
            Client client = ReformCloudController.getInstance().getClient(args[1]);
            if (client == null) {
                commandSender.sendMessage("The client isn't registered");
                return;
            }

            if (!ReformCloudController.getInstance().getChannelHandler().isChannelRegistered(client.getName())) {
                commandSender.sendMessage("The client isn't connected");
                return;
            }

            if (args[2].equalsIgnoreCase("starthost")) {
                if (args[3].split("\\.").length != 4) {
                    commandSender.sendMessage("Please provide a valid ip address");
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client.getName(), new PacketOutUpdateClientSetting(ClientSettings.START_HOST, args[3])
                );
                commandSender.sendMessage("Trying to update the start host...");
                return;
            }

            if (args[2].equalsIgnoreCase("memory")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3]) || Integer.valueOf(args[3]) < 100) {
                    commandSender.sendMessage("Please provide a valid number which is bigger than 100");
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client.getName(), new PacketOutUpdateClientSetting(ClientSettings.MEMORY, args[3])
                );
                commandSender.sendMessage("Trying to update the memory...");
                return;
            }

            if (args[2].equalsIgnoreCase("maxcpu")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3]) || Integer.valueOf(args[3]) < 10) {
                    commandSender.sendMessage("Please provide a valid number which is bigger than 10");
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client.getName(), new PacketOutUpdateClientSetting(ClientSettings.MAX_CPU_USAGE, args[3])
                );
                commandSender.sendMessage("Trying to update the max cpu usage...");
                return;
            }

            if (args[2].equalsIgnoreCase("maxlogsize")) {
                if (!ReformCloudLibraryService.checkIsInteger(args[3]) || Integer.valueOf(args[3]) < 5) {
                    commandSender.sendMessage("Please provide a valid number which is bigger than 5");
                    return;
                }

                ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client.getName(), new PacketOutUpdateClientSetting(ClientSettings.MAX_LOG_SIZE, args[3])
                );
                commandSender.sendMessage("Trying to update the max log size...");
                return;
            }

            this.sendHelp(commandSender);
        } else {
            this.sendHelp(commandSender);
        }
    }

    private void sendHelp(CommandSender commandSender) {
        commandSender.sendMessage("assignment SERVERGROUP <name> <permission, clients, templates, memory, maxonline, " +
                "minonline, maxplayers, startport, maintenance, savelogs, servermodetype, version> <value> <--update>");
        commandSender.sendMessage("assignment PROXYGROUP <name> <clients, templates, disabledgroups, maintenance, " +
                "savelogs, memory, maxonline, minonline, maxplayers, commandlogging, version> <value> <--update>");
        commandSender.sendMessage("assignment CLIENT <name> <starthost, memory, maxcpu, maxlogsize> <value>");
    }
}
