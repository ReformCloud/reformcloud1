/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@Getter
@Setter
public class Packet implements Serializable {
    private static final long serialVersionUID = -30847898951064299L;

    private Configuration configuration;
    private String type;

    private UUID result;

    public Packet() {
    }

    public Packet(@NonNull final String type, @NonNull Configuration configuration) {
        this.type = type;
        this.configuration = configuration;
    }

    public void read(@NonNull ByteBuf byteBuf) {
        if (byteBuf.readableBytes() != 0) {
            final Packet packet = ReformCloudLibraryService.GSON.fromJson(byteBuf.readBytes((int) readLong(byteBuf)).toString(StandardCharsets.UTF_8), TypeTokenAdaptor.getPACKET_TYPE());
            this.configuration = packet.configuration;
            this.type = packet.type;
            this.result = packet.result;
        }
    }

    public void write(@NonNull ByteBuf byteBuf) {
        byte[] bytes = ReformCloudLibraryService.GSON.toJson(this).getBytes(StandardCharsets.UTF_8);
        this.writeLongs(bytes.length, byteBuf).writeBytes(bytes);
    }

    private ByteBuf writeLongs(long value, ByteBuf byteBuf) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0)
                temp |= 0b10000000;

            byteBuf.writeByte(temp);
        } while (value != 0);

        return byteBuf;
    }

    private long readLong(final ByteBuf byteBuf) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = byteBuf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static Packet emptyPacket() {
        return new Packet("undefined", new Configuration());
    }
}
