/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.netty.packets;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.signs.SignLayoutConfiguration;

import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class PacketOutSendSigns extends DefaultPacket {

    public PacketOutSendSigns(final SignLayoutConfiguration signLayoutConfiguration,
        final Map<UUID, Sign> signs, UUID result) {
        super("undefined", new Configuration().addValue("signConfig", signLayoutConfiguration)
            .addValue("signMap", signs));
        super.setResult(result);
    }
}
