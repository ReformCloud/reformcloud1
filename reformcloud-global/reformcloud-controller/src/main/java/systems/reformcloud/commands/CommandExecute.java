/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.netty.out.PacketOutExecuteCommand;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 08.12.2018
 */

public final class CommandExecute implements Command {
    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length < 3) {
            commandSender.sendMessage("execute <server/proxy> <name> <command>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "server": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[1]) != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (short i = 2; i < args.length; i++)
                        stringBuilder.append(args[i]).append(StringUtil.SPACE);

                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[1]).getCloudProcess().getClient(), new PacketOutExecuteCommand(stringBuilder.substring(0, stringBuilder.length() - 1), args[0].toLowerCase(), args[1]));
                    commandSender.sendMessage("The command has been executed.");
                } else
                    commandSender.sendMessage("This Server is not connected to controller");
                break;
            }
            case "proxy": {
                if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[1]) != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (short i = 2; i < args.length; i++)
                        stringBuilder.append(args[i]).append(StringUtil.SPACE);

                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[1]).getCloudProcess().getClient(), new PacketOutExecuteCommand(stringBuilder.substring(0, stringBuilder.length() - 1), args[0].toLowerCase(), args[1]));
                    commandSender.sendMessage("The command has been executed.");
                } else
                    commandSender.sendMessage("This Proxy is not connected to controller");
                break;
            }
            default: {
                commandSender.sendMessage("execute <server/proxy> <name> <command>");
            }
        }
    }

    @Override
    public String getPermission() {
        return "reformcloud.command.execute";
    }
}
