/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import systems.reformcloud.network.packet.Packet;

import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public final class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
        List<Object> list) {
        try {
            final Packet packet = new Packet();
            packet.read(byteBuf);
            list.add(packet);
        } catch (final Exception ignored) {
        }
    }
}
