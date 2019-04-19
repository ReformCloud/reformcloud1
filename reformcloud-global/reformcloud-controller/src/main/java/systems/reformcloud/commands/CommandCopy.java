/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.out.PacketOutCopyServerIntoTemplate;
import systems.reformcloud.network.out.PacketOutExecuteCommand;

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
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(serverInfo.getCloudProcess().getClient(), new PacketOutCopyServerIntoTemplate(serverInfo.getCloudProcess().getName() + "-" + serverInfo.getCloudProcess().getProcessUID(),
                            serverInfo.getCloudProcess().getName(), "server", serverInfo.getServerGroup().getName()));
                    commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage().getCommand_copy_try()
                    );
                } else {
                    commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage().getCommand_copy_backend_not_client()
                    );
                }
            } else if (ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[0]) != null) {
                final ProxyInfo proxyInfo = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByName(args[0]);
                if (proxyInfo.getCloudProcess().getLoadedTemplate().getTemplateBackend().equals(TemplateBackend.CLIENT)) {
                    ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(proxyInfo.getCloudProcess().getClient(), new PacketOutCopyServerIntoTemplate(proxyInfo.getCloudProcess().getName() + "-" + proxyInfo.getCloudProcess().getProcessUID(), proxyInfo.getCloudProcess().getName(),
                            "proxy", proxyInfo.getProxyGroup().getName()));
                    commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage().getCommand_copy_try()
                    );
                } else {
                    commandSender.sendMessage(
                            ReformCloudController.getInstance().getLoadedLanguage().getCommand_copy_backend_not_client()
                    );
                }
            } else {
                commandSender.sendMessage(
                        ReformCloudController.getInstance().getLoadedLanguage().getProcess_not_connected()
                                .replace("%name%", args[0])
                );
            }
        } else {
            commandSender.sendMessage("copy <name>");
        }
    }
}
