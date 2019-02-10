/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.out.PacketOutCopyServerIntoTemplate;
import systems.reformcloud.netty.out.PacketOutExecuteCommand;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public final class CommandCopy extends Command implements Serializable {
    public CommandCopy() {
        super("copy", "Copies a server or proxy into the template", "reformcloud.command.copy", new String[0]);
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[0]) != null) {
                final ServerInfo serverInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByName(args[0]);
                if (serverInfo.getCloudProcess().getLoadedTemplate().getTemplateBackend().equals(TemplateBackend.CLIENT)) {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(
                            serverInfo.getCloudProcess().getClient(),
                            new PacketOutExecuteCommand("save-all", "server", serverInfo.getCloudProcess().getName())
                    );
                    ReformCloudLibraryService.sleep(100);
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(), new PacketOutCopyServerIntoTemplate(serverInfo.getCloudProcess().getName() + "-" + serverInfo.getCloudProcess().getProcessUID(), "server", serverInfo.getServerGroup().getName()));
                    commandSender.sendMessage("The Client tries to copy the template.");
                } else {
                    commandSender.sendMessage("You can't copy a server if the template backend is not the client.");
                }
            } else if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[0]) != null) {
                final ProxyInfo proxyInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[0]);
                if (proxyInfo.getCloudProcess().getLoadedTemplate().getTemplateBackend().equals(TemplateBackend.CLIENT)) {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(), new PacketOutCopyServerIntoTemplate(proxyInfo.getCloudProcess().getName() + "-" + proxyInfo.getCloudProcess().getProcessUID(), "proxy", proxyInfo.getProxyGroup().getName()));
                    commandSender.sendMessage("The Client tries to copy the template.");
                } else {
                    commandSender.sendMessage("You can't copy a server if the template backend is not the client.");
                }
            } else {
                commandSender.sendMessage("The server or proxy isn't connected to Controller.");
            }
        } else {
            commandSender.sendMessage("copy <name>");
        }
    }
}
