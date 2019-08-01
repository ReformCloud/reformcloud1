/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.files.DownloadManager;

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
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudSigns"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudSigns.jar",
                        "reformcloud/addons/ReformCloudSigns.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("discord")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudDiscord"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudDiscord.jar",
                        "reformcloud/addons/ReformCloudDiscordBot.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("permissions")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudPermissions"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudPermissions.jar",
                        "reformcloud/addons/ReformCloudPermissions.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("proxy")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudProxy"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudProxy.jar",
                        "reformcloud/addons/ReformCloudProxy.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("parameters")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudParamaeters"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudParameters.jar",
                        "reformcloud/addons/ReformCloudParameters.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("autoicon")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudAutoIcon"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudAutoIcon.jar",
                        "reformcloud/addons/ReformCloudAutoIcon.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("properties")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudProperties"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudProperties.jar",
                        "reformcloud/addons/ReformCloudProperties.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("mobs")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudMob"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudMobs.jar",
                        "reformcloud/addons/ReformCloudMobs.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("cloudflare")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudCloudFlare"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudCloudFlare.jar",
                        "reformcloud/addons/ReformCloudCloudFlare.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            } else if (args[0].equalsIgnoreCase("backup")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .noneMatch(e -> e.getAddonName().equals("ReformCloudBackup"))) {
                    DownloadManager.downloadSilentAndDisconnect(
                        "https://dl.reformcloud.systems/addons/ReformCloudBackup.jar",
                        "reformcloud/addons/ReformCloudBackup.jar");
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getDownload_success()
                    );
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is already installed!"));
                }
                return;
            }
        }

        commandSender.sendMessage(
            "install <autoicon, backup, cloudflare, discord, mobs, parameters, permissions, proxy, properties, signs>");
    }

    @Override
    public List<String> complete(String commandLine, String[] args) {
        List<String> out = new LinkedList<>();

        if (args.length == 1) {
            out.addAll(asList("AUTOICON", "BACKUP", "CLOUDFLARE", "DISCORD", "MOBS", "PARAMETERS", "PERMISSIONS",
                "PROXY", "PROPERTIES", "SIGNS"));
        }

        return out;
    }

}
