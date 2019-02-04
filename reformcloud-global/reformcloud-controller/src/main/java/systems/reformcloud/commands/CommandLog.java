/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.out.PacketOutUploadLog;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class CommandLog implements Command, Serializable {
    private static final long serialVersionUID = 8869467720046339074L;

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("controller")) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                Files.readAllLines(Paths.get("reformcloud/logs/CloudLog.0")).forEach(s -> stringBuilder.append(s).append("\n"));
            } catch (final IOException ex) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not read log", ex);
            }

            final String url = ReformCloudController.getInstance().getLoggerProvider().uploadLog(stringBuilder.substring(0));
            commandSender.sendMessage("The log for the Controller was uploaded: " + url);
            return;
        }

        if (args.length != 2) {
            commandSender.sendMessage("log <CONTROLLER>");
            commandSender.sendMessage("log <CLIENT, SPIGOT, PROXY> <name>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "client": {
                Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(args[1]);
                if (client != null) {
                    if (!ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                            new PacketOutUploadLog(client.getName(), "client"))) {
                        commandSender.sendMessage("This client isn't registered");
                    }
                } else {
                    commandSender.sendMessage("This client doesn't exists");
                }
                break;
            }

            case "spigot": {
                ServerInfo serverInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[1]);
                if (serverInfo != null) {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(),
                            new PacketOutUploadLog(serverInfo.getCloudProcess().getName(), "spigot"));
                } else {
                    commandSender.sendMessage("This server isn't registered");
                }
                break;
            }

            case "proxy": {
                ProxyInfo proxyInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[1]);
                if (proxyInfo != null) {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(),
                            new PacketOutUploadLog(proxyInfo.getCloudProcess().getName(), "bungee"));
                } else {
                    commandSender.sendMessage("This proxy isn't registered");
                }
                break;
            }

            default: {
                commandSender.sendMessage("The serverType is invalid");
            }
        }
    }

    @Override
    public String getPermission() {
        return "reformcloud.command.log";
    }
}
