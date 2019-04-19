/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.04.2019
 */

@AllArgsConstructor
@Getter
public final class ChannelConnectedEvent extends Event implements Serializable {
    private ChannelHandlerContext channelHandlerContext;
}
