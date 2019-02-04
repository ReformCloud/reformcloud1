/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.ProcessRegisterEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.netty.out.PacketOutProcessAdd;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public class PacketInAddProcess implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());

            if (serverInfo == null)
                return;

            ProcessRegisterEvent processRegisterEvent = new ProcessRegisterEvent(false, true, false, serverInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getEventManager().callEvent(EventTargetType.PROCESS_REGISTERED, processRegisterEvent);

            if (processRegisterEvent.isCancelled())
                return;

            ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_process_add()
                    .replace("%name%", serverInfo.getCloudProcess().getName())
                    .replace("%uid%", serverInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", serverInfo.getCloudProcess().getClient()));

            ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting().remove(serverInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutProcessAdd(serverInfo));
        } else {
            final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getProxyInfoType());

            ProcessRegisterEvent processRegisterEvent = new ProcessRegisterEvent(false, true, false, proxyInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getEventManager().callEvent(EventTargetType.PROCESS_REGISTERED, processRegisterEvent);

            if (processRegisterEvent.isCancelled())
                return;

            ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_process_add()
                    .replace("%name%", proxyInfo.getCloudProcess().getName())
                    .replace("%uid%", proxyInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", proxyInfo.getCloudProcess().getClient()));

            ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting().remove(proxyInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutProcessAdd(proxyInfo));
        }
    }
}
