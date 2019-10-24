/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.length;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class LengthEncoder extends MessageToByteEncoder<ByteBuf> implements Serializable {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf in, ByteBuf out)
        throws Exception {
        int readAble = in.readableBytes();
        int space = getVarIntSize(readAble);
        if (space > 5) {
            throw new IllegalStateException("Length space is bigger than 5");
        }

        out.ensureWritable(space + readAble);
        write(out, readAble);
        out.writeBytes(in, in.readerIndex(), readAble);
    }

    public static ByteBuf write(ByteBuf out, int value) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }

            out.writeByte(temp);
        } while (value != 0);

        return out;
    }

    private int getVarIntSize(int value) {
        if ((value & -128) == 0) {
            return 1;
        } else if ((value & -16384) == 0) {
            return 2;
        } else if ((value & -2097152) == 0) {
            return 3;
        } else if ((value & -268435456) == 0) {
            return 4;
        }

        return 5;
    }
}
