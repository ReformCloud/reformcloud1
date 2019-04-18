/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.utility.screen.ScreenSessionProvider;
import systems.reformcloud.utility.screen.defaults.DefaultScreenHandler;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class CommandScreen extends Command implements Serializable {
    private static final long serialVersionUID = -1318658988288270472L;

    public CommandScreen() {
        super("screen", "Opens the screen of a server, client or proxy", "reformcloud.command.screen", new String[]{"srn"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        final ScreenSessionProvider screenSessionProvider = ReformCloudController.getInstance().getScreenSessionProvider();

        if (args.length == 1 && args[0].toLowerCase().equalsIgnoreCase("leave")) {
            if (screenSessionProvider.leaveScreen()) {
                commandSender.sendMessage("You §asuccessfully§r left the §escreen§r session");
            } else {
                commandSender.sendMessage("You're not in a screen session");
            }

            return;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("switch")) {
            if (!screenSessionProvider.isInScreen()) {
                commandSender.sendMessage("You have to be in a screen session, before you can switch between them");
                return;
            }

            screenSessionProvider.leaveScreen();

            switch (args[1].toLowerCase()) {
                case "server": {
                    final ServerInfo serverInfo = ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getRegisteredServerByName(args[2]);
                    if (serverInfo == null) {
                        commandSender.sendMessage("The server §e[\"" + args[2] + "\"]§r is not §cconnected§r to the controller");
                        return;
                    }

                    screenSessionProvider.joinScreen(serverInfo.getCloudProcess().getName(),
                            new DefaultScreenHandler(
                                    serverInfo.getCloudProcess().getClient(), "server", serverInfo.getCloudProcess().getName()
                            )
                    );
                    break;
                }
                case "proxy": {
                    final ProxyInfo proxyInfo = ReformCloudController.getInstance()
                            .getInternalCloudNetwork()
                            .getServerProcessManager()
                            .getRegisteredProxyByName(args[2]);
                    if (proxyInfo == null) {
                        commandSender.sendMessage("The proxy §e[\"" + args[2] + "\"]§r is not §cconnected§r to the controller");
                        return;
                    }

                    screenSessionProvider.joinScreen(proxyInfo.getCloudProcess().getName(),
                            new DefaultScreenHandler(
                                    proxyInfo.getCloudProcess().getClient(), "proxy", proxyInfo.getCloudProcess().getName()
                            )
                    );
                    break;
                }
                case "client": {
                    final Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(args[2]);
                    if (client == null || client.getClientInfo() == null) {
                        commandSender.sendMessage("Client is not registered");
                        return;
                    }

                    screenSessionProvider.joinScreen(
                            client.getName(),
                            new DefaultScreenHandler(
                                    client.getName(), "client", client.getName()
                            )
                    );
                    break;
                }
                default: {
                    commandSender.sendMessage("screen <server, proxy, client> <name>");
                    commandSender.sendMessage("screen execute <command>");
                    commandSender.sendMessage("screen switch <server, proxy, client> <name>");
                    commandSender.sendMessage("screen leave");
                    break;
                }
            }
        }

        if (args.length >= 2 && (args[0].equalsIgnoreCase("execute") || args[0].equalsIgnoreCase("exec"))) {
            if (!screenSessionProvider.isInScreen()) {
                commandSender.sendMessage("You're not in a screen session. Type \"screen\"" +
                        " to get a command overview");
                return;
            }

            StringBuilder stringBuilder = new StringBuilder();
            String[] commandSplit = Arrays.copyOfRange(args, 1, args.length);
            for (String cmd : commandSplit)
                stringBuilder.append(cmd).append(" ");

            screenSessionProvider.executeCommand(stringBuilder.substring(0, stringBuilder.length() - 1));
            return;
        }

        if (args.length != 2) {
            commandSender.sendMessage("screen <server, proxy, client> <name>");
            commandSender.sendMessage("screen execute <command>");
            commandSender.sendMessage("screen switch <server, proxy, client> <name>");
            commandSender.sendMessage("screen leave");
            return;
        }

        if (screenSessionProvider.isInScreen()) {
            commandSender.sendMessage("You're already in a screen session. Type \"screen leave\"" +
                    " or \"screen switch <server/proxy> <name>\" to leave the current screen session or switch to another");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "server": {
                final ServerInfo serverInfo = ReformCloudController.getInstance()
                        .getInternalCloudNetwork()
                        .getServerProcessManager()
                        .getRegisteredServerByName(args[1]);
                if (serverInfo == null) {
                    commandSender.sendMessage("The server §e[\"" + args[1] + "\"]§r is not §cconnected§r to the controller");
                    return;
                }

                screenSessionProvider.joinScreen(serverInfo.getCloudProcess().getName(),
                        new DefaultScreenHandler(
                                serverInfo.getCloudProcess().getClient(), "server", serverInfo.getCloudProcess().getName()
                        )
                );
                break;
            }
            case "proxy": {
                final ProxyInfo proxyInfo = ReformCloudController.getInstance()
                        .getInternalCloudNetwork()
                        .getServerProcessManager()
                        .getRegisteredProxyByName(args[1]);
                if (proxyInfo == null) {
                    commandSender.sendMessage("The proxy §e[\"" + args[1] + "\"]§r is not §cconnected§r to the controller");
                    return;
                }

                screenSessionProvider.joinScreen(proxyInfo.getCloudProcess().getName(),
                        new DefaultScreenHandler(
                                proxyInfo.getCloudProcess().getClient(), "proxy", proxyInfo.getCloudProcess().getName()
                        )
                );
                break;
            }
            case "client": {
                final Client client = ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(args[1]);
                if (client == null || client.getClientInfo() == null) {
                    commandSender.sendMessage("Client is not registered");
                    return;
                }

                screenSessionProvider.joinScreen(client.getName(),
                        new DefaultScreenHandler(
                                client.getName(), "client", client.getName()
                        )
                );
                break;
            }
            default: {
                commandSender.sendMessage("screen <server, proxy, client> <name>");
                commandSender.sendMessage("screen execute <command>");
                commandSender.sendMessage("screen switch <server, proxy, client> <name>");
                commandSender.sendMessage("screen leave");
                break;
            }
        }
    }
}
