/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.proxy.ProxyGroup;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.network.out.*;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class CommandUpload extends Command implements Serializable {

    public CommandUpload() {
        super("upload", "Uploads the given file to the specific position",
            "reformcloud.command.upload", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length <= 1) {
            commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
            commandSender.sendMessage("upload PLUGIN <GROUPNAME> <NAME> <URL>");
            commandSender.sendMessage("upload PLUGIN <GROUPNAME> <TEMPLATE> <NAME> <URL>");
            commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
            return;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("controller")) {
                if (!this.isURLValid(args[1])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The given url is not valid"));
                    return;
                }

                DownloadManager
                    .download("the new ControllerFile", args[1], FileUtils.getInternalFileName());
                commandSender.sendMessage("Please restart the controller load the new file");
                return;
            } else if (args[0].equalsIgnoreCase("clients")) {
                if (!this.isURLValid(args[1])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The given url is not valid"));
                    return;
                }

                ReformCloudController.getInstance().getInternalCloudNetwork().getClients().values()
                    .forEach(client -> {
                        if (client.getClientInfo() == null) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_error_occurred()
                                    .replace("%message%", "Client isn't connected"));
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketAsynchronous(client.getName(),
                                new PacketOutUpdateClientFromURL(args[1]));
                        commandSender
                            .sendMessage("Trying to update client " + client.getName() + "...");
                    });
            } else {
                commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <NAME> <URL>");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <TEMPLATE> <NAME> <URL>");
                commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
            }

            return;
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("controlleraddon")) {
                if (!this.isURLValid(args[2])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The given url is not valid"));
                    return;
                }

                FileUtils.deleteFileIfExists(Paths.get("addons/" + args[1] + ".jar"));
                DownloadManager.downloadAndDisconnect("addon " + args[1], args[2],
                    "addons/" + args[1] + ".jar");
                commandSender.sendMessage("Updated controllerAddon " + args[1]);
                return;
            } else if (args[0].equalsIgnoreCase("clientaddon")) {
                if (!this.isURLValid(args[2])) {
                    commandSender.sendMessage("The given url is not valid");
                    return;
                }

                ReformCloudController.getInstance().getInternalCloudNetwork().getClients().values()
                    .forEach(client -> {
                        if (client.getClientInfo() == null) {
                            commandSender.sendMessage(
                                ReformCloudController.getInstance().getLoadedLanguage()
                                    .getCommand_error_occurred()
                                    .replace("%message%", "Client isn't connected"));
                            return;
                        }

                        ReformCloudController.getInstance().getChannelHandler()
                            .sendPacketAsynchronous(client.getName(),
                                new PacketOutUpdateClientAddon(args[1], args[2]));
                        commandSender
                            .sendMessage("Trying to update clientAddon " + args[1] + "...");
                    });
                return;
            } else {
                commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <NAME> <URL>");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <TEMPLATE> <NAME> <URL>");
                commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
            }

            return;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("plugin")) {
            if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
                .get(args[1]) != null) {
                if (!this.isURLValid(args[3])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The given url is not valid"));
                    return;
                }

                ServerGroup serverGroup = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getServerGroups().get(args[1]);
                serverGroup.getClients().forEach(client -> {
                    Client client1 = ReformCloudController.getInstance().getClient(client);
                    if (client1 == null || client1.getClientInfo() == null) {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%", "Client isn't connected"));
                        return;
                    }

                    ReformCloudController.getInstance().getChannelHandler()
                        .sendPacketAsynchronous(client1.getName(),
                            new PacketOutUpdateServerGroupPlugin(
                                serverGroup.getName(), args[2], args[3]
                            ));
                    commandSender.sendMessage(
                        "Trying to update plugin on serverGroup " + serverGroup.getName() + "...");
                });
            } else if (
                ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups()
                    .get(args[1]) != null) {
                if (!this.isURLValid(args[3])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The given url is not valid"));
                    return;
                }

                ProxyGroup proxyGroup = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getProxyGroups().get(args[1]);
                proxyGroup.getClients().forEach(client -> {
                    Client client1 = ReformCloudController.getInstance().getClient(client);
                    if (client1 == null || client1.getClientInfo() == null) {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%", "Client isn't connected"));
                        return;
                    }

                    ReformCloudController.getInstance().getChannelHandler()
                        .sendPacketAsynchronous(client1.getName(),
                            new PacketOutUpdateProxyGroupPlugin(
                                proxyGroup.getName(), args[2], args[3]
                            ));
                    commandSender.sendMessage(
                        "Trying to update plugin on proxyGroup " + proxyGroup.getName() + "...");
                });
            } else {
                commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <NAME> <URL>");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <TEMPLATE> <NAME> <URL>");
                commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
            }

            return;
        }

        if (args.length == 5 && args[0].equalsIgnoreCase("plugin")) {
            if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups()
                .get(args[1]) != null) {
                if (!this.isURLValid(args[4])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The given url is not valid"));
                    return;
                }

                ServerGroup serverGroup = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getServerGroups().get(args[1]);

                if (serverGroup.getTemplate(args[2]) == null) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "Template doesn't exists"));
                    return;
                }

                serverGroup.getClients().forEach(client -> {
                    Client client1 = ReformCloudController.getInstance().getClient(client);
                    if (client1 == null || client1.getClientInfo() == null) {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%", "Client isn't connected"));
                        return;
                    }

                    ReformCloudController.getInstance().getChannelHandler()
                        .sendPacketAsynchronous(client1.getName(),
                            new PacketOutUpdateServerGroupPluginTemplate(
                                serverGroup.getName(), args[2], args[3], args[4]
                            ));
                    commandSender.sendMessage(
                        "Trying to update plugin on serverGroup " + serverGroup.getName() + "...");
                });
            } else if (
                ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups()
                    .get(args[1]) != null) {
                if (!this.isURLValid(args[4])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "The given url is not valid"));
                    return;
                }

                ProxyGroup proxyGroup = ReformCloudController.getInstance()
                    .getInternalCloudNetwork().getProxyGroups().get(args[1]);
                if (proxyGroup.getTemplate(args[2]) == null) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "Template doesn't exists"));
                    return;
                }

                proxyGroup.getClients().forEach(client -> {
                    Client client1 = ReformCloudController.getInstance().getClient(client);
                    if (client1 == null || client1.getClientInfo() == null) {
                        commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage()
                                .getCommand_error_occurred()
                                .replace("%message%", "Client isn't connected"));
                        return;
                    }

                    ReformCloudController.getInstance().getChannelHandler()
                        .sendPacketAsynchronous(client1.getName(),
                            new PacketOutUpdateProxyGroupPluginTemplate(
                                proxyGroup.getName(), args[2], args[3], args[4]
                            ));
                    commandSender.sendMessage(
                        "Trying to update plugin on proxyGroup " + proxyGroup.getName() + "...");
                });
            } else {
                commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <NAME> <URL>");
                commandSender.sendMessage("upload PLUGIN <GROUPNAME> <TEMPLATE> <NAME> <URL>");
                commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
            }

            return;
        }

        commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
        commandSender.sendMessage("upload PLUGIN <GROUPNAME> <NAME> <URL>");
        commandSender.sendMessage("upload PLUGIN <GROUPNAME> <TEMPLATE> <NAME> <URL>");
        commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
    }

    @Override
    public List<String> complete(String commandLine, String[] args) {
        List<String> out = new LinkedList<>();

        if(args.length == 0) {
            out.addAll(asList("PLUGIN", "CONTROLLER", "CLIENTS",
                "CONTROLLERADDON", "CLIENTADDON"));
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("PLUGIN")) {
                out.addAll(groups());
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("PLUGIN")) {
                out.addAll(templates(args[1]));
            }
        }

        return out;
    }

    private boolean isURLValid(String url) {
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setConnectTimeout(1000);
            urlConnection.setUseCaches(false);
            urlConnection.connect();

            return true;
        } catch (Throwable throwable) {
            return false;
        }
    }

    private List<String> groups() {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getAllProxyGroups()
            .forEach(group -> out.add(group.getName()));
        ReformCloudController.getInstance().getAllServerGroups()
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

    private List<String> templates(String name) {
        List<String> out = new LinkedList<>();
        ReformCloudController.getInstance().getServerGroup(name)
            .getTemplates()
            .forEach(template -> out.add(template.getName()));
        ReformCloudController.getInstance().getProxyGroup(name)
            .getTemplates()
            .forEach(template -> out.add(template.getName()));
        Collections.sort(out);
        return out;
    }

}
