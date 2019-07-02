/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.out.PacketOutDeleteTemplate;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandDelete extends Command implements Serializable {

    public CommandDelete() {
        super("delete", "Deletes a ServerGroup, ProxyGroup or Client", "reformcloud.command.delete",
            new String[]{"delet", "del"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 3 && args[0].equalsIgnoreCase("servertemplate")) {
            ServerGroup serverGroup = ReformCloudController.getInstance().getServerGroup(args[1]);
            if (serverGroup == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "The server group doesn't exists"));
                return;
            }

            Template template = serverGroup.getTemplateOrElseNull(args[2]);
            if (template == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "The template doesn't exists"));
                return;
            }

            serverGroup.getClients().forEach(client -> {
                if (ReformCloudController.getInstance().getClient(client) != null
                    && ReformCloudController.getInstance().getChannelHandler()
                    .isChannelRegistered(client)) {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client, new PacketOutDeleteTemplate("server", template.getName(),
                            serverGroup.getName())
                    );
                }
            });

            serverGroup.getTemplates().remove(template);
            ReformCloudController.getInstance().updateServerGroup(serverGroup);
            //TODO: remove message and replace it with a message in message file (before pre release)
            commandSender.sendMessage("Done");
            return;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("proxytemplate")) {
            ProxyGroup proxyGroup = ReformCloudController.getInstance().getProxyGroup(args[1]);
            if (proxyGroup == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "The proxy group doesn't exists"));
                return;
            }

            Template template = proxyGroup.getTemplateOrElseNull(args[2]);
            if (template == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "The template doesn't exists"));
                return;
            }

            proxyGroup.getClients().forEach(client -> {
                if (ReformCloudController.getInstance().getClient(client) != null
                    && ReformCloudController.getInstance().getChannelHandler()
                    .isChannelRegistered(client)) {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                        client, new PacketOutDeleteTemplate("proxy", template.getName(),
                            proxyGroup.getName())
                    );
                }
            });

            proxyGroup.getTemplates().remove(template);
            ReformCloudController.getInstance().updateProxyGroup(proxyGroup);
            //TODO: remove message and replace it with a message in message file (before pre release)
            commandSender.sendMessage("Done");
            return;
        }

        if (args.length != 2) {
            commandSender.sendMessage("delete SERVERGROUP <name>");
            commandSender.sendMessage("delete PROXYGROUP <name>");
            commandSender.sendMessage("delete CLIENT <name>");
            commandSender.sendMessage("delete WEBUSER <name>");
            commandSender
                .sendMessage("delete <servertemplate, proxytemplate> <group> <template-name>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "servergroup": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
                    .containsKey(args[1])) {
                    try {
                        ReformCloudController.getInstance().getCloudConfiguration()
                            .deleteServerGroup(
                                ReformCloudController.getInstance().getInternalCloudNetwork()
                                    .getServerGroups().get(args[1]));
                    } catch (final IOException ex) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not delete servergroup", ex);
                    }
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The server group doesn't exists"));
                }
                break;
            }

            case "proxygroup": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups()
                    .containsKey(args[1])) {
                    try {
                        ReformCloudController.getInstance().getCloudConfiguration()
                            .deleteProxyGroup(
                                ReformCloudController.getInstance().getInternalCloudNetwork()
                                    .getProxyGroups().get(args[1]));
                    } catch (final IOException ex) {
                        StringUtil.printError(
                            ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                            "Could not delete proxygroup", ex);
                    }
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The proxy group doesn't exists"));
                }
                break;
            }

            case "client": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getClients()
                    .containsKey(args[1])) {
                    ReformCloudController.getInstance().getCloudConfiguration().deleteClient(
                        ReformCloudController.getInstance().getInternalCloudNetwork().getClients()
                            .get(args[1]));
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The client doesn't exists"));
                }
                break;
            }

            case "webuser": {
                WebUser webUser = ReformCloudController.getInstance()
                    .getCloudConfiguration()
                    .getWebUsers()
                    .stream()
                    .filter(e -> e.getUserName().equals(args[1]))
                    .findFirst()
                    .orElse(null);
                if (webUser == null) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The web user doesn't exists"));
                    return;
                }

                ReformCloudController.getInstance().getCloudConfiguration().deleteWebuser(webUser);
                break;
            }

            default: {
                commandSender.sendMessage("delete SERVERGROUP <name>");
                commandSender.sendMessage("delete PROXYGROUP <name>");
                commandSender.sendMessage("delete CLIENT <name>");
                commandSender.sendMessage("delete WEBUSER <name>");
                commandSender
                    .sendMessage("delete <servertemplate, proxytemplate> <group> <template-name>");
            }
        }
    }

    @Override
    public List<String> complete(String commandLine, String[] args) {
        List<String> out = new LinkedList<>();

        if(args.length == 0) {
            out.addAll(asList("SERVERGROUP", "PROXYGROUP", "CLIENT", "WEBUSER"));
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("servergroup")) {
                out.addAll(serverGroups());
            } else if (args[0].equalsIgnoreCase("proxygroup")) {
                out.addAll(proxyGroups());
            } else if (args[0].equalsIgnoreCase("client")) {
                out.addAll(clients());
            } else if (args[0].equalsIgnoreCase("webuser")) {
                out.addAll(users());
            }
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

    private List<String> users() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getCloudConfiguration()
            .getWebUsers()
            .forEach(user -> out.add(user.getUserName()));
        Collections.sort(out);
        return out;
    }

}
