/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.meta.web.WebUser;

import java.io.Serializable;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

public final class CommandWebPermissions extends Command implements Serializable {

    public CommandWebPermissions() {
        super("webpermissions", "Adds a user a specific permission",
            "reformcloud.command.webpermissions", new String[]{"wperms"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("list")) {
            WebUser webUser = this.getUser(args[1]);
            if (webUser == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "User not found"));
                return;
            }

            commandSender.sendMessage("Permissions of user " + webUser.getUser());
            for (Map.Entry<String, Boolean> perms : webUser.getPermissions().entrySet()) {
                commandSender.sendMessage(
                    "   - §e" + perms.getKey() + "§r | Activated: " + (perms.getValue() ? "§atrue"
                        : "§cfalse"));
            }

            return;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            WebUser webUser = this.getUser(args[1]);
            if (webUser == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "User not found"));
                return;
            }

            if (!webUser.getPermissions().containsKey(args[2])) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "User don't have permission \"" + args[2] + "\""));
                return;
            }

            webUser.getPermissions().remove(args[2]);
            ReformCloudController.getInstance().getCloudConfiguration().updateWebUser(webUser);
            commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                .getCommand_webpermission_remove_success());
            return;
        }

        if (args.length != 4) {
            commandSender.sendMessage("wperms add <name> <permission> <true/false>");
            commandSender.sendMessage("wperms remove <name> <permission>");
            commandSender.sendMessage("wperms list <name>");
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            WebUser webUser = this.getUser(args[1]);
            if (webUser == null) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_error_occurred()
                    .replace("%message%", "User not found"));
                return;
            }

            if (webUser.getPermissions().containsKey(args[2])) {
                if (webUser.getPermissions().get(args[2]) == Boolean.valueOf(args[3])) {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%", "Permission already set"));
                    return;
                } else {
                    webUser.getPermissions().remove(args[2]);
                }
            }

            webUser.getPermissions().put(args[2], Boolean.valueOf(args[3]));
            ReformCloudController.getInstance().getCloudConfiguration().updateWebUser(webUser);
            commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                .getCommand_webpermission_add_success()
                .replace("%perm%", args[2])
                .replace("%key%", Boolean.toString(Boolean.valueOf(args[3]))));
        }
    }

    private WebUser getUser(final String name) {
        return ReformCloudController.getInstance()
            .getCloudConfiguration()
            .getWebUsers()
            .stream()
            .filter(e -> e.getUser().equals(name))
            .findFirst()
            .orElse(null);
    }
}
