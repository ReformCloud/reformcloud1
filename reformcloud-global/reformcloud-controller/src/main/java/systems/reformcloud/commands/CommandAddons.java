/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.util.*;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.files.DownloadManager;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class CommandAddons extends Command implements Serializable {

    public CommandAddons() {
        super("addons", "List addons", "reformcloud.command.addons",
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
        } else if (args.length == 2 && args[0].equalsIgnoreCase("update")){
            if (args[1].equalsIgnoreCase("signs")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudSigns"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudSigns");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudSigns.jar",
                        "reformcloud/addons/ReformCloudSigns.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("discord")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudDiscord"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudDiscord");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudDiscord.jar",
                        "reformcloud/addons/ReformCloudDiscordBot.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("permissions")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudPermissions"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudPermissions");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudPermissions.jar",
                        "reformcloud/addons/ReformCloudPermissions.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("proxy")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudProxy"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudProxy");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudProxy.jar",
                        "reformcloud/addons/ReformCloudProxy.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("parameters")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudParameters"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudParameters");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudParameters.jar",
                        "reformcloud/addons/ReformCloudParameters.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("autoicon")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudAutoIcon"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudAutoIcon");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudAutoIcon.jar",
                        "reformcloud/addons/ReformCloudAutoIcon.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("properties")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudProperties"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudProperties");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudProperties.jar",
                        "reformcloud/addons/ReformCloudProperties.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("mobs")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudMob"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudMob");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudMobs.jar",
                        "reformcloud/addons/ReformCloudMobs.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("cloudflare")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudCloudFlare"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudCloudFlare");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudCloudFlare.jar",
                        "reformcloud/addons/ReformCloudCloudFlare.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("backup")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .filter(e -> e.getAddonName().equals("ReformCloudBackup"))
                    .findFirst().orElse(null) != null) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudBackup");
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudBackup.jar",
                        "reformcloud/addons/ReformCloudBackup.jar");
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            } else if (args[1].equalsIgnoreCase("--all")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader().getJavaAddons().size() == 0) {
                    commandSender.sendMessage(ReformCloudController.getInstance().getLoadedLanguage()
                        .getCommand_addons_no_addons_loaded());
                } else {
                    for (JavaAddon javaAddon : ReformCloudController.getInstance().getAddonParallelLoader().getJavaAddons()) {
                        ReformCloudController.getInstance().getAddonParallelLoader().disableAddon(javaAddon.getAddonName());
                        switch (javaAddon.getAddonName()) {

                            case "ReformCloudDiscord": {
                                DownloadManager.downloadSilentAndDisconnect(
                                    "https://dl.reformcloud.systems/addons/ReformCloudDiscord.jar",
                                    "reformcloud/addons/ReformCloudDiscordBot.jar");
                                break;
                            }

                            case "ReformCloudMob": {
                                DownloadManager.downloadSilentAndDisconnect(
                                    "https://dl.reformcloud.systems/addons/ReformCloudMobs.jar",
                                    "reformcloud/addons/ReformCloudMobs.jar");
                                break;
                            }

                            default: {
                                DownloadManager.downloadSilentAndDisconnect(
                                    "https://dl.reformcloud.systems/addons/" + javaAddon.getAddonName() + ".jar",
                                    "reformcloud/addons/" + javaAddon.getAddonName() + ".jar");
                                break;
                            }
                        }
                    }
                    ReformCloudController.getInstance().reloadAllSave();
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                }
            } else {
                commandSender.sendMessage("addons list");
                commandSender.sendMessage(
                    "addons update <autoicon, backup, cloudflare, discord," + " mobs, parameters, permissions, proxy, properties, signs, --all>");
            }
        } else {
            commandSender.sendMessage("addons list");
            commandSender.sendMessage(
                "addons update <autoicon, backup, cloudflare, discord," + " mobs, parameters, permissions, proxy, properties, signs, --all>");
        }
    }

    @Override
    public List<String> complete(String commandLine, String[] args) {
        final List<String> out = new LinkedList<>();
        if (args.length == 0) {
            out.addAll(asList("list", "update"));
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
            out.addAll(asList("AUTOICON", "BACKUP", "CLOUDFLARE", "DISCORD", "MOBS", "PARAMETERS", "PERMISSIONS",
                "PROXY", "PROPERTIES", "SIGNS", "--ALL"));
        }

        return out;
    }
}
