/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 08.12.2018
 */

public final class CommandHelp extends Command implements Serializable {
    private final DecimalFormat decimalFormat = new DecimalFormat("##.###");

    public CommandHelp() {
        super("help", "Get help", null, new String[]{"?"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("The following Commands are registered:");
        ReformCloudController.getInstance().getCommandManager().getCommands().forEach(command -> commandSender.sendMessage("   - " + command.getName() + " | Aliases: §e" + Arrays.asList(command.getAliases()) + "§r | Description: §3" + command.getDescription() + "§r | Permission: §e" + (command.getPermission() == null ? "none" : command.getPermission())));

        ReformCloudController.getInstance().getLoggerProvider().emptyLine();

        commandSender.sendMessage("Ram: " + decimalFormat.format(ReformCloudLibraryService.bytesToMB(ReformCloudLibraryService.usedMemorySystem())) + "MB/" + decimalFormat.format(ReformCloudLibraryService.bytesToMB(ReformCloudLibraryService.maxMemorySystem())) + "MB");
        commandSender.sendMessage("CPU (System/Internal): " + decimalFormat.format(ReformCloudLibraryService.cpuUsage()) + "/" + decimalFormat.format(ReformCloudLibraryService.internalCpuUsage()));
        commandSender.sendMessage("Threads: " + Thread.getAllStackTraces().size());
        commandSender.sendMessage("For further information please contact us on our Discord (\"https://discord.gg/fwe2CHD\")");
    }
}
