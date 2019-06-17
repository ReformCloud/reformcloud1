/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.ProxyStoppedEvent;
import systems.reformcloud.event.events.ServerStoppedEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutProcessRemove;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.screen.ScreenSessionProvider;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class PacketInRemoveProcess implements NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration
                .getValue("serverInfo", TypeTokenAdaptor.getSERVER_INFO_TYPE());

            if (serverInfo == null) {
                return;
            }

            ReformCloudController.getInstance().getCloudProcessOfferService()
                .unregisterID(serverInfo);
            ReformCloudController.getInstance().getEventManager()
                .fire(new ServerStoppedEvent(serverInfo));

            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .unregisterServerProcess(
                    serverInfo.getCloudProcess().getProcessUID(),
                    serverInfo.getCloudProcess().getName(),
                    serverInfo.getPort()
                );

            ReformCloudController.getInstance().getColouredConsoleProvider().info(
                ReformCloudController.getInstance().getLoadedLanguage()
                    .getController_process_stopped()
                    .replace("%name%", serverInfo.getCloudProcess().getName())
                    .replace("%uid%", serverInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", serverInfo.getCloudProcess().getClient()));
            ReformCloudController.getInstance().getChannelHandler()
                .sendToAllAsynchronous(new PacketOutProcessRemove(serverInfo),
                    new PacketOutUpdateAll(
                        ReformCloudController.getInstance().getInternalCloudNetwork()));

            final ScreenSessionProvider screenSessionProvider = ReformCloudController.getInstance()
                .getScreenSessionProvider();
            if (screenSessionProvider.isInScreen(serverInfo.getCloudProcess().getName())) {
                screenSessionProvider.leaveScreen();
                ReformCloudController.getInstance().getColouredConsoleProvider().info(
                    ReformCloudController.getInstance().getLoadedLanguage()
                        .getScreen_kicked_process_disconnect()
                );
            }
        } else {
            final ProxyInfo proxyInfo = configuration
                .getValue("proxyInfo", TypeTokenAdaptor.getPROXY_INFO_TYPE());

            if (proxyInfo == null) {
                return;
            }

            ReformCloudController.getInstance().getCloudProcessOfferService()
                .unregisterProxyID(proxyInfo);
            ReformCloudController.getInstance().getEventManager()
                .fire(new ProxyStoppedEvent(proxyInfo));

            ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
                .unregisterProxyProcess(
                    proxyInfo.getCloudProcess().getProcessUID(),
                    proxyInfo.getCloudProcess().getName(),
                    proxyInfo.getPort()
                );

            ReformCloudController.getInstance().getColouredConsoleProvider().info(
                ReformCloudController.getInstance().getLoadedLanguage()
                    .getController_process_stopped()
                    .replace("%name%", proxyInfo.getCloudProcess().getName())
                    .replace("%uid%", proxyInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", proxyInfo.getCloudProcess().getClient()));
            ReformCloudController.getInstance().getChannelHandler()
                .sendToAllAsynchronous(new PacketOutProcessRemove(proxyInfo),
                    new PacketOutUpdateAll(
                        ReformCloudController.getInstance().getInternalCloudNetwork()));

            final ScreenSessionProvider screenSessionProvider = ReformCloudController.getInstance()
                .getScreenSessionProvider();
            if (screenSessionProvider.isInScreen(proxyInfo.getCloudProcess().getName())) {
                screenSessionProvider.leaveScreen();
                ReformCloudController.getInstance().getColouredConsoleProvider().info(
                    ReformCloudController.getInstance().getLoadedLanguage()
                        .getScreen_kicked_process_disconnect()
                );
            }
        }
    }
}
