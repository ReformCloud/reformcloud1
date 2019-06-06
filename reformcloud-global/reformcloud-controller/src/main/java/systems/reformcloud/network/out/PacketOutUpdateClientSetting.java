/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.settings.ClientSettings;
import systems.reformcloud.network.packet.Packet;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.03.2019
 */

public final class PacketOutUpdateClientSetting extends Packet implements Serializable {

    public PacketOutUpdateClientSetting(ClientSettings clientSettings, String newValue) {
        super(
            "UpdateClientSetting",
            new Configuration()
                .addValue("setting", clientSettings)
                .addStringValue("value", newValue)
        );
    }
}
