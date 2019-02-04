/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.authentication.interfaces;

import systems.reformcloud.netty.authentication.enums.AuthenticationType;
import systems.reformcloud.netty.channel.ChannelHandler;
import systems.reformcloud.netty.packet.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public interface AuthenticationManager {
    void handleAuth(AuthenticationType authenticationType, Packet packet, ChannelHandlerContext channelHandlerContext, ChannelHandler channelHandler);
}
