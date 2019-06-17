/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.sync.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.packet.constants.ChannelConstants;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketInSyncExceptionThrown implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        final Throwable cause = configuration.getValue("cause", new TypeToken<Throwable>() {
        }.getType());
        if (cause != null) {
            StringUtil.printError(ReformCloudController.getInstance().getColouredConsoleProvider(),
                "Exception caught on Client " + configuration.getStringValue("name"), cause);
        }
    }

    @Override
    public long handlingChannel() {
        return ChannelConstants.REFORMCLOUD_SYNC_CLIENT_COMMUNICATION_CHANNEL;
    }
}
