/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.11.2018
 */

public final class PacketInUpdateAll implements NetworkInboundHandler, Serializable {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPISpigot.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getINTERNAL_CLOUD_NETWORK_TYPE()));

        if (ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByUID(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getProcessUID()) != null)
            ReformCloudAPISpigot.getInstance().setServerInfo(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByUID(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getProcessUID()));

        if (SignSelector.getInstance() != null)
            SignSelector.getInstance().updateAll();
    }
}
