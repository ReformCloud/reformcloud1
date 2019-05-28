/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.packets.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.selector.MobSelector;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketInQueryGetAll implements Serializable, NetworkQueryInboundHandler {
    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                configuration.getStringValue("from"),
                new Packet(
                        StringUtil.NULL,
                        new Configuration()
                                .addValue("mobs", MobSelector.getInstance().getMobs())
                                .addValue("config", MobSelector.getInstance().getSelectorMobConfig()),
                        resultID
                )
        );
    }
}
