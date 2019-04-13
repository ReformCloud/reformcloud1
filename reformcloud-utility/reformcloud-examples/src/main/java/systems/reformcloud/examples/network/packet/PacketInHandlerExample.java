/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.network.packet;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class PacketInHandlerExample implements NetworkInboundHandler, NetworkQueryInboundHandler {
    /**
     * Method get called, when the handler gets triggered
     * Contains sent configuration, with all added stuff like String and Integers
     * <p>
     * !! Dont forget to register the packet handler !!
     */
    @Override
    public void handle(Configuration configuration) {
    }

    /**
     * Method gets called when a query packet was send by another process
     *
     * @param configuration The main configuration with all the stuff you send
     *                      Note: in the configuration is a string called {@code from}
     *                      this is the process which sends the packet
     * @param resultID      The result UniqueID of the packet which is needed to handle
     *                      the packet in the other instance again
     */
    @Override
    public void handle(Configuration configuration, UUID resultID) {

    }
}
