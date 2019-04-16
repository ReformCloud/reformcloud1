/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.authentication.interfaces;

import io.netty.channel.ChannelHandlerContext;
import systems.reformcloud.network.authentication.enums.AuthenticationType;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.packet.Packet;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public interface AuthenticationManager {
    /**
     * Handles the authentication of the a network participant
     *
     * @param authenticationType            The authentication type
     * @param packet                        The authentication packet send by the network participant
     * @param channelHandlerContext         The channel handler context of the participant's channel
     * @param channelHandler                The channel handler to register the process if the operation was successful
     */
    void handleAuth(AuthenticationType authenticationType, Packet packet, ChannelHandlerContext channelHandlerContext, ChannelHandler channelHandler);
}
