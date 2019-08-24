package systems.reformcloud.commands;

import java.io.Serializable;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.files.FileUtils;

public final class CommandUninstall extends Command implements Serializable {

    public CommandUninstall() {
        super("uninstall", "Uninstall an addon", "reformcloud.command.uninstall", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("signs")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equals("ReformCloudSigns"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudSigns");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudSigns.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/signs");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if (args[0].equalsIgnoreCase("discord")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equals("ReformCloudDiscordBot"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudDiscordBot");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudSigns.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/discord");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if (args[0].equalsIgnoreCase("permissions")) {
                if (ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equals("ReformCloudPermissions"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudPermissions");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudPermissions.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/discord");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if(args[0].equalsIgnoreCase("proxy")) {
                if(ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equalsIgnoreCase("ReformCloudProxy"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudProxy");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudProxy.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/proxy");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if(args[0].equalsIgnoreCase("parameters")) {
                if(ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equalsIgnoreCase("ReformCloudParameters"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudParameters");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudParameters.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/parameters");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if(args[0].equalsIgnoreCase("autoicon")) {
                if(ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equalsIgnoreCase("ReformCloudAutoIcon"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudAutoIcon");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudAutoIcon.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/autoicon");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if(args[0].equalsIgnoreCase("properties")) {
                if(ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equalsIgnoreCase("ReformCloudProperties"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudProperties");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudProperties.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/properties");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if(args[0].equalsIgnoreCase("mobs")) {
                if(ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equalsIgnoreCase("ReformCloudMob"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudMob");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudMobs.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/mobs");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if(args[0].equalsIgnoreCase("cloudflare")) {
                if(ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equalsIgnoreCase("ReformCloudCloudFlare"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudCloudFlare");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudCloudFlare.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/cloudflare");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }
            }
            else if(args[0].equalsIgnoreCase("backup")) {
                if(ReformCloudController.getInstance().getAddonParallelLoader()
                    .getJavaAddons()
                    .stream()
                    .anyMatch(e -> e.getAddonName().equalsIgnoreCase("ReformCloudBackup"))) {
                    ReformCloudController.getInstance().getAddonParallelLoader().disableAddon("ReformCloudBackup");

                    FileUtils.deleteFileIfExists("reformcloud/addons/ReformCloudBackup.jar");
                    FileUtils.deleteFullDirectory("reformcloud/addons/backup");
                    ReformCloudController.getInstance().reloadAllSave();
                } else {
                    commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage()
                            .getCommand_error_occurred()
                            .replace("%message%",
                                "This addon is not installed!"));
                }

            } else {
                commandSender.sendMessage("addons list");
                commandSender.sendMessage(
                    "addons update <autoicon, backup, cloudflare, discord," + " mobs, parameters, permissions, proxy, properties, signs>");
            }
        } else {
            commandSender.sendMessage("addons list");
            commandSender.sendMessage(
                "addons update <autoicon, backup, cloudflare, discord," + " mobs, parameters, permissions, proxy, properties, signs>");
        }
    }
}
