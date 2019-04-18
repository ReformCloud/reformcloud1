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
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@Getter
@Setter
public class Packet implements Serializable {
    private static final long serialVersionUID = -30847898951064299L;

    /**
     * Returns an empty packet which doesn't contains any data
     */
    public static final Packet EMPTY_PACKET = new Packet(StringUtil.NULL, new Configuration());

    /**
     * The configuration of the packet
     */
    private Configuration configuration;

    /**
     * The type of the packet
     */
    private String type;

    /**
     * The result uid of the packet
     */
    private UUID result;

    public Packet() {
    }

    public Packet(@NonNull final String type, @NonNull Configuration configuration) {
        this.type = type;
        this.configuration = configuration;
    }

    public Packet(@NonNull final String type, @NonNull Configuration configuration, @NonNull UUID resultID) {
        this.type = type;
        this.configuration = configuration;
        this.result = resultID;
    }

    public Packet(@NonNull final String type, @NonNull Map<? extends String, ?> configuration) {
        this.type = type;
        this.configuration = Configuration.fromMap(configuration);
    }

    public Packet(@NonNull final String type, @NonNull Map<? extends String, ?> configuration, @NonNull UUID resultID) {
        this.type = type;
        this.configuration = Configuration.fromMap(configuration);
        this.result = resultID;
    }

    /**
     * Reads a packet from the given byte buf
     *
     * @param byteBuf       The byte buf containing the packet information
     */
    public void read(@NonNull ByteBuf byteBuf) {
        if (byteBuf.readableBytes() != 0) {
            final Packet packet = ReformCloudLibraryService.GSON.fromJson(byteBuf.readBytes((int) readLong(byteBuf)).toString(StandardCharsets.UTF_8), TypeTokenAdaptor.getPACKET_TYPE());
            this.configuration = packet.configuration;
            this.type = packet.type;
            this.result = packet.result;
        }
    }

    /**
     * Writes a packet to the byte buf
     *
     * @param byteBuf   The byte buf where the packet should be written to
     */
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
}
