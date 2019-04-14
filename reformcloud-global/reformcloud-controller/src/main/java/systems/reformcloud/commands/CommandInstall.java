/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.files.DownloadManager;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class CommandInstall extends Command implements Serializable {
    public CommandInstall() {
        super("install", "Installs an addon", "reformcloud.command.install", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("signs")) {
                DownloadManager.downloadSilentAndDisconnect("https://dl.reformcloud.systems/addons/ReformCloudSigns.jar", "reformcloud/addons/SignAddon.jar");
                commandSender.sendMessage("Download was completed successfully");
                ReformCloudController.getInstance().reloadAllSave();
                return;
            } else if (args[0].equalsIgnoreCase("discord")) {
                DownloadManager.downloadSilentAndDisconnect("https://dl.reformcloud.systems/addons/ReformCloudDiscord.jar", "reformcloud/addons/DiscordBot.jar");
                commandSender.sendMessage("Download was completed successfully");
                ReformCloudController.getInstance().reloadAllSave();
                return;
            } else if (args[0].equalsIgnoreCase("permissions")) {
                DownloadManager.downloadSilentAndDisconnect("https://dl.reformcloud.systems/addons/ReformCloudPermissions.jar", "reformcloud/addons/PermissionsAddon.jar");
                commandSender.sendMessage("Download was completed successfully");
                ReformCloudController.getInstance().reloadAllSave();
                return;
            } else if (args[0].equalsIgnoreCase("proxy")) {
                DownloadManager.downloadSilentAndDisconnect("https://dl.reformcloud.systems/addons/ReformCloudProxy.jar", "reformcloud/addons/ReformCloudProxy.jar");
                commandSender.sendMessage("Download was completed successfully");
                ReformCloudController.getInstance().reloadAllSave();
                return;
            } else if (args[0].equalsIgnoreCase("parameters")) {
                DownloadManager.downloadSilentAndDisconnect("https://dl.reformcloud.systems/addons/ReformCloudParameters.jar", "reformcloud/addons/ReformCloudParameters.jar");
                commandSender.sendMessage("Download was completed successfully");
                ReformCloudController.getInstance().reloadAllSave();
                return;
            }
        }

        commandSender.sendMessage("install <signs, discord, permissions, proxy, parameters>");
    }
}
