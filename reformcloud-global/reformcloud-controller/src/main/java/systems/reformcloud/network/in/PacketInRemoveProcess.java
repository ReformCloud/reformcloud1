/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.enums.EventTargetType;
import systems.reformcloud.event.events.ProcessUnregistersEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutProcessRemove;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.screen.ScreenSessionProvider;

/**
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public class PacketInRemoveProcess implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (configuration.contains("serverInfo")) {
            final ServerInfo serverInfo = configuration.getValue("serverInfo", TypeTokenAdaptor.getServerInfoType());

            if (serverInfo == null)
                return;

            ReformCloudController.getInstance().getCloudProcessOfferService().unregisterID(serverInfo);

            ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_process_stopped()
                    .replace("%name%", serverInfo.getCloudProcess().getName())
                    .replace("%uid%", serverInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", serverInfo.getCloudProcess().getClient()));
            ReformCloudController.getInstance().getEventManager().callEvent(EventTargetType.PROCESS_UNREGISTERED, new ProcessUnregistersEvent(serverInfo.getCloudProcess().getName()));
            ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutProcessRemove(serverInfo));

            final ScreenSessionProvider screenSessionProvider = ReformCloudController.getInstance().getScreenSessionProvider();
            if (screenSessionProvider.isInScreen(serverInfo.getCloudProcess().getName())) {
                screenSessionProvider.leaveScreen();
                ReformCloudController.getInstance().getLoggerProvider().info(
                        ReformCloudController.getInstance().getLoadedLanguage().getScreen_kicked_process_disconnect()
                );
            }
        } else {
            final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getProxyInfoType());

            if (proxyInfo == null)
                return;

            ReformCloudController.getInstance().getCloudProcessOfferService().unregisterProxyID(proxyInfo);

            ReformCloudController.getInstance().getLoggerProvider().info(ReformCloudController.getInstance().getLoadedLanguage().getController_process_stopped()
                    .replace("%name%", proxyInfo.getCloudProcess().getName())
                    .replace("%uid%", proxyInfo.getCloudProcess().getProcessUID() + "")
                    .replace("%client%", proxyInfo.getCloudProcess().getClient()));
            ReformCloudController.getInstance().getEventManager().callEvent(EventTargetType.PROCESS_UNREGISTERED, new ProcessUnregistersEvent(proxyInfo.getCloudProcess().getName()));
            ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutProcessRemove(proxyInfo));

            final ScreenSessionProvider screenSessionProvider = ReformCloudController.getInstance().getScreenSessionProvider();
            if (screenSessionProvider.isInScreen(proxyInfo.getCloudProcess().getName())) {
                screenSessionProvider.leaveScreen();
                ReformCloudController.getInstance().getLoggerProvider().info(
                        ReformCloudController.getInstance().getLoadedLanguage().getScreen_kicked_process_disconnect()
                );
            }
        }
    }
}
