/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.network.packet;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class PacketInHandlerExample implements NetworkInboundHandler {
    /**
     * Method get called, when the handler gets triggered
     * Contains sent configuration, with all added stuff like String and Integers
     *
     * !! Dont forget to register the packet handler !!
     */
    @Override
    public void handle(Configuration configuration) {
    }
}
