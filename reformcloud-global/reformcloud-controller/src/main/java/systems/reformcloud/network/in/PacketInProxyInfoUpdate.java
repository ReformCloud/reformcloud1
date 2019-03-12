/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
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
        final ProxyInfo proxyInfo = configuration.getValue("proxyInfo", TypeTokenAdaptor.getPROXY_INFO_TYPE());

        ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().updateProxyInfo(proxyInfo);

        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(ReformCloudController.getInstance().getInternalCloudNetwork());
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
                new PacketOutUpdateAll(ReformCloudController.getInstance().getInternalCloudNetwork()),
                new PacketOutProxyInfoUpdate(proxyInfo)
        );
    }
}
