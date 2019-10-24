/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.process.ProxyStartedEvent;
import systems.reformcloud.event.events.process.ServerStartedEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutProcessAdd;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketInAddProcess implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration
                .getValue("serverInfo", TypeTokenAdaptor.getSERVER_INFO_TYPE());

            if (serverInfo == null) {
                return;
            }

            ReformCloudController.getInstance().getEventManager()
                .fire(new ServerStartedEvent(serverInfo));

            ReformCloudController.getInstance().getColouredConsoleProvider().info(
                ReformCloudController.getInstance().getLoadedLanguage().getController_process_add()
                    .replace("%name%", serverInfo.getCloudProcess().getName())
                    .replace("%uid%", serverInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", serverInfo.getCloudProcess().getClient()));

            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .registerServerProcess(
                    serverInfo.getCloudProcess().getProcessUID(),
                    serverInfo.getCloudProcess().getName(),
                    serverInfo
                );

            ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting()
                .remove(serverInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getCloudProcessOfferService().getWaitingPerClient()
                .remove(serverInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getChannelHandler()
                .sendToAllAsynchronous(new PacketOutProcessAdd(serverInfo),
                    new PacketOutUpdateAll(
                        ReformCloudController.getInstance().getInternalCloudNetwork()));
        } else {
            final ProxyInfo proxyInfo = configuration
                .getValue("proxyInfo", TypeTokenAdaptor.getPROXY_INFO_TYPE());

            if (proxyInfo == null) {
                return;
            }

            ReformCloudController.getInstance().getEventManager()
                .fire(new ProxyStartedEvent(proxyInfo));
            ReformCloudController.getInstance().getColouredConsoleProvider().info(
                ReformCloudController.getInstance().getLoadedLanguage().getController_process_add()
                    .replace("%name%", proxyInfo.getCloudProcess().getName())
                    .replace("%uid%", proxyInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", proxyInfo.getCloudProcess().getClient()));

            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .registerProxyProcess(
                    proxyInfo.getCloudProcess().getProcessUID(),
                    proxyInfo.getCloudProcess().getName(),
                    proxyInfo
                );

            ReformCloudController.getInstance().getCloudProcessOfferService().getWaiting()
                .remove(proxyInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getCloudProcessOfferService().getWaitingPerClient()
                .remove(proxyInfo.getCloudProcess().getName());
            ReformCloudController.getInstance().getChannelHandler()
                .sendToAllAsynchronous(new PacketOutProcessAdd(proxyInfo),
                    new PacketOutUpdateAll(
                        ReformCloudController.getInstance().getInternalCloudNetwork()));
        }
    }
}
