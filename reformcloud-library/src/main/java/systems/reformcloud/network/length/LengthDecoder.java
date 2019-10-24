/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.length;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class LengthDecoder extends ByteToMessageDecoder implements Serializable {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf incoming,
                          List<Object> list) throws Exception {
        incoming.markReaderIndex();
        byte[] lengthByte = new byte[5];

        for (int i = 0; i < 5; i++) {
            if (!incoming.isReadable()) {
                incoming.resetReaderIndex();
                return;
            }

            lengthByte[i] = incoming.readByte();
            if (lengthByte[i] >= 0) {
                ByteBuf byteBuf = Unpooled.wrappedBuffer(lengthByte);

                try {
                    int len = readVarInt(byteBuf);
                    if (incoming.readableBytes() < len) {
                        incoming.resetReaderIndex();
                        return;
                    }

                    list.add(incoming.readBytes(len));
                } finally {
                    byteBuf.release();
                }

                return;
            }
        }
    }

    public static int readVarInt(ByteBuf buf) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new IllegalStateException("VarInt too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }
}
