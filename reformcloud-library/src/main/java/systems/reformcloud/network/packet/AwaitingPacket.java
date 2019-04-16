/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.03.2019
 */

@AllArgsConstructor
@Getter
public final class AwaitingPacket implements Serializable {
    /**
     * The channel where the packet should be sent through
     */
    private ChannelHandlerContext channelHandlerContext;

    /**
     * The packet which should be sent
     */
    private Packet packet;
}
