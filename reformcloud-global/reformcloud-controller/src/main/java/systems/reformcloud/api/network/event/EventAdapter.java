/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.network.event;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.api.IEventHandler;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 23.02.2019
 */

public final class EventAdapter implements Serializable, IEventHandler {
    public EventAdapter() {
        instance.set(this);
    }

    @Override
    public void handleCustomPacket(String channel, String targetType, Configuration configuration) {
        if (configuration.contains("to")) {
            ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    configuration.getStringValue("to"), new Packet(targetType, configuration)
            );
        }
    }

    @Override
    public void handleReload() {
        try {
            ReformCloudController.getInstance().reloadAll();
        } catch (final Throwable throwable) {
            StringUtil.printError(ReformCloudController.getInstance().getLoggerProvider(), "Error while handling network reload", throwable);
        }
    }
}
