/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandDelete extends Command implements Serializable {
    public CommandDelete() {
        super("delete", "Deletes a ServerGroup, ProxyGroup or Client", "reformcloud.command.delete", new String[]{"delet", "del"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length != 2) {
            commandSender.sendMessage("delete SERVERGROUP <name>");
            commandSender.sendMessage("delete PROXYGROUP <name>");
            commandSender.sendMessage("delete CLIENT <name>");
            commandSender.sendMessage("delete WEBUSER <name>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "servergroup": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().containsKey(args[1])) {
                    try {
                        ReformCloudController.getInstance().getCloudConfiguration().deleteServerGroup(ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().get(args[1]));
                    } catch (final IOException ex) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not delete servergroup", ex);
                    }
                } else {
                    commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage().getCommand_error_occurred()
                            .replace("%message%", "The server group doesn't exists"));
                }
                break;
            }

            case "proxygroup": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().containsKey(args[1])) {
                    try {
                        ReformCloudController.getInstance().getCloudConfiguration().deleteProxyGroup(ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().get(args[1]));
                    } catch (final IOException ex) {
                        StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not delete proxygroup", ex);
                    }
                } else {
                    commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage().getCommand_error_occurred()
                            .replace("%message%", "The proxy group doesn't exists"));
                }
                break;
            }

            case "client": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getClients().containsKey(args[1])) {
                    ReformCloudController.getInstance().getCloudConfiguration().deleteClient(ReformCloudController.getInstance().getInternalCloudNetwork().getClients().get(args[1]));
                } else {
                    commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage().getCommand_error_occurred()
                            .replace("%message%", "The client doesn't exists"));
                }
                break;
            }

            case "webuser": {
                WebUser webUser = ReformCloudController.getInstance()
                        .getCloudConfiguration()
                        .getWebUsers()
                        .stream()
                        .filter(e -> e.getUser().equals(args[1]))
                        .findFirst()
                        .orElse(null);
                if (webUser == null) {
                    commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage().getCommand_error_occurred()
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
            }
        }
    }
}
