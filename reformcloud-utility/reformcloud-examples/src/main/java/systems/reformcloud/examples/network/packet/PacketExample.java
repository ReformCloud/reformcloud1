/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.network.packet;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public final class PacketExample extends Packet implements Serializable { //Class can be final
    public PacketExample() {
        super(
                "Example", //Type of the packet, packet will be handled by this name
                new Configuration().addStringProperty("example", StringUtil.NULL) //What is in the packet, main message, information
        );
    }
}
