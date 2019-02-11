/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import systems.reformcloud.ReformCloudAPISpigot;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 25.11.2018
 */

public class PacketInUpdateAll implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        ReformCloudAPISpigot.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));
        ReformCloudLibraryServiceProvider.getInstance().setInternalCloudNetwork(configuration.getValue("networkProperties", TypeTokenAdaptor.getInternalCloudNetworkType()));

        if (ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByUID(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getProcessUID()) != null)
            ReformCloudAPISpigot.getInstance().setServerInfo(ReformCloudAPISpigot.getInstance().getInternalCloudNetwork().getServerProcessManager().getRegisteredServerByUID(ReformCloudAPISpigot.getInstance().getServerInfo().getCloudProcess().getProcessUID()));

        if (SignSelector.getInstance() != null)
            SignSelector.getInstance().updateAll();
    }
}
