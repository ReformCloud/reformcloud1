/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configuration.PropertiesConfig;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 22.04.2019
 */

public final class PacketOutSendProperties extends DefaultPacket implements Serializable {

    public PacketOutSendProperties(UUID result) {
        super(
            StringUtil.NULL,
            new Configuration()
                .addValue("config", PropertiesConfig.getInstance().getPropertiesConfig()),
            result
        );
    }
}
