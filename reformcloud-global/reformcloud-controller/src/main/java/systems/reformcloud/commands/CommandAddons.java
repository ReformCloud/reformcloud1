/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class CommandAddons extends Command implements Serializable {

    public CommandAddons() {
        super("addons", "List, enable and disable addons", "reformcloud.command.addons",
            new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (ReformCloudController.getInstance().getAddonParallelLoader().getJavaAddons().size()
                == 0) {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_addons_no_addons_loaded());
            } else {
                commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                    .getCommand_addons_following_loaded());
                ReformCloudController.getInstance().getColouredConsoleProvider().emptyLine();
                ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .forEach(e -> commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_addons_addon_description()
                            .replace("%name%", e.getAddonName())
                            .replace("%version%", e.getAddonClassConfig().getVersion())
                            .replace("%main%", e.getAddonClassConfig().getMain())
                    ));
            }
        } else {
            commandSender.sendMessage("addons list");
        }
    }

    @Override
    public List<String> complete(String commandLine, String[] args) {
        if (args.length == 0) {
            return Collections.singletonList("list");
        }

        return new ArrayList<>();
    }
}
