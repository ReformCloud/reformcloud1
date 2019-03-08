/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.files.DownloadManager;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author _Klaro | Pasqual K. / created on 08.03.2019
 */

public final class CommandUpload extends Command implements Serializable {
    public CommandUpload() {
        super("upload", "Uploads the given file to the specific position", "reformcloud.command.upload", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length <= 1) {
            commandSender.sendMessage("upload <CONTROLLER, CLIENTS> URL");
            commandSender.sendMessage("upload PLUGIN <GLOBAL, GROUPNAME> NAME URL");
            commandSender.sendMessage("upload PLUGIN GROUPNAME TEMPLATE NAME URL");
            commandSender.sendMessage("upload <CONTROLLERADDON, CLIENTADDON> NAME URL");
            return;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("controller")) {
                if (!this.isURLValid(args[1])) {
                    commandSender.sendMessage("The given url is not valid");
                    return;
                }

                DownloadManager.download("the new ControllerFile", args[1], FileUtils.getInternalFileName());
                commandSender.sendMessage("Please restart the controller load the new file");
                return;
            }
        }
    }

    private boolean isURLValid(String url) {
        if (!url.endsWith(".jar"))
            return false;

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
}
