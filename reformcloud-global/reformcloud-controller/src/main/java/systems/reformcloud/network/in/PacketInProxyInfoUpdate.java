/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.ProxyInfoUpdateEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.out.PacketOutProxyInfoUpdate;
import systems.reformcloud.network.out.PacketOutUpdateAll;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class PacketInProxyInfoUpdate implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final ProxyInfo proxyInfo = configuration
            .getValue("proxyInfo", TypeTokenAdaptor.getPROXY_INFO_TYPE());

        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
            .updateProxyInfo(proxyInfo);
        ReformCloudController.getInstance().getChannelHandler().sendToAllDirect(
            new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork())
        );
        ReformCloudController.getInstance().getChannelHandler().sendToAllDirect(
            new PacketOutProxyInfoUpdate(proxyInfo)
        );
        ReformCloudController.getInstance().getEventManager()
            .fire(new ProxyInfoUpdateEvent(proxyInfo));
    }
}
