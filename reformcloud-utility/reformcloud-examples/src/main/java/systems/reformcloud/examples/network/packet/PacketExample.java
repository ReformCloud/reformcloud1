/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.network.packet;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.packet.Packet;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class PacketExample extends Packet { //Class can be final
    public PacketExample() {
        super(
                "Example", //Type of the packet, packet will be handled by this name
                new Configuration().addStringProperty("URLClassPath", StringUtil.NULL) //What is in the packet, main message, information
                //Query type(s) of packet !!HAS TO CONTAIN COMPLETE !!
                //PacketSender of packet
        );
    }
}
