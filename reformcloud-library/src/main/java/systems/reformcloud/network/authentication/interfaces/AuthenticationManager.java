/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.authentication.interfaces;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.network.abstracts.AbstractChannelHandler;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public interface AuthenticationManager {

    /**
     * Handles the authentication of the a network participant
     *
     * @param arg1 The authentication type
     * @param arg2 The authentication packet send by the network participant
     * @param arg3 The channel handler context of the participant's channel
     * @param arg4 The channel handler to register the process if the operation was
     * successful
     */
    void handleAuth(AuthenticationType arg1, Packet arg2,
                    ChannelHandlerContext arg3, AbstractChannelHandler arg4);
}
