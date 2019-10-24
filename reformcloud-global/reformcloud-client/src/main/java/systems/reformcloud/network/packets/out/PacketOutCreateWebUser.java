/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packets.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.web.WebUser;
import systems.reformcloud.network.packet.DefaultPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketOutCreateWebUser extends DefaultPacket implements Serializable {

    public PacketOutCreateWebUser(WebUser webUser) {
        super("CreateWebUser", new Configuration().addValue("user", webUser));
    }
}
