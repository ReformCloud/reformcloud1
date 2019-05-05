/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.11.2018
 */

public final class PacketInUpdateAll implements NetworkInboundHandler, Serializable {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPIVelocity.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));

        if (ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByUID(ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getProcessUID()) != null)
            ReformCloudAPIVelocity.getInstance().setProxyInfo(ReformCloudAPIVelocity.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredProxyByUID(ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getProcessUID()));
    }
}
