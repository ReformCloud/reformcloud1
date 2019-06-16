/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import io.netty.buffer.ByteBuf;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.constants.ChannelConstants;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public class Packet implements Serializable {

    private static final long serialVersionUID = -30847898951064299L;

    /**
     * Returns an empty packet which doesn't contains any data
     */
    static final Packet EMPTY_PACKET = new Packet(StringUtil.NULL, new Configuration());

    /**
     * The configuration of the packet
     */
    private Configuration configuration;

    /**
     * The type of the packet
     */
    private String type;

    /**
     * The internal communication channel
     */
    private long channel;

    /**
     * The result uid of the packet
     */
    private UUID result;

    public Packet() {
    }

    public Packet(String type, Configuration configuration) {
        this(type, configuration, null);
    }

    public Packet(final String type, Configuration configuration,
        UUID resultID, long channel) {
        this.type = type;
        this.configuration = configuration;
        this.result = resultID;

        if (this.result != null
            && channel == ChannelConstants.REFORMCLOUD_INTERNAL_INFORMATION_DEFAULT_CHANNEL) {
            this.channel = ChannelConstants.REFORMCLOUD_INTERNAL_QUERY_INFORMATION_DEFAULT_CHANNEL;
        } else {
            this.channel = channel;
        }
    }

    public Packet(final String type, Configuration configuration,
        long channel) {
        this(type, configuration, null, channel);
    }

    public Packet(final String type, Configuration configuration, UUID resultID) {
        this(type, configuration, resultID,
            ChannelConstants.REFORMCLOUD_INTERNAL_INFORMATION_DEFAULT_CHANNEL);
    }

    public Packet(final String type, Map<? extends String, ?> configuration) {
        this(type, Configuration.fromMap(configuration));
    }

    public Packet(final String type, Map<? extends String, ?> configuration, UUID resultID) {
        this(type, Configuration.fromMap(configuration), resultID);
    }

    /**
     * Reads a packet from the given byte buf
     *
     * @param byteBuf The byte buf containing the packet information
     */
    public void read(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() != 0) {
            final Packet packet = ReformCloudLibraryService.GSON.fromJson(
                byteBuf.readBytes((int) readLong(byteBuf)).toString(StandardCharsets.UTF_8),
                TypeTokenAdaptor.getPACKET_TYPE());
            this.configuration = packet.configuration;
            this.type = packet.type;
            this.result = packet.result;
            this.channel = packet.channel;
        }
    }

    /**
     * Writes a packet to the byte buf
     *
     * @param byteBuf The byte buf where the packet should be written to
     */
    public void write(ByteBuf byteBuf) {
        byte[] bytes = ReformCloudLibraryService.GSON.toJson(this).getBytes(StandardCharsets.UTF_8);
        this.writeLongs(bytes.length, byteBuf).writeBytes(bytes);
    }

    private ByteBuf writeLongs(long value, ByteBuf byteBuf) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }

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

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public String getType() {
        return this.type;
    }

    public UUID getResult() {
        return this.result;
    }

    public long getChannel() {
        return channel;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setResult(UUID result) {
        this.result = result;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }
}
