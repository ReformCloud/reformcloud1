/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import io.netty.buffer.ByteBuf;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.length.LengthDecoder;
import systems.reformcloud.network.length.LengthEncoder;
import systems.reformcloud.network.packet.constants.ChannelConstants;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

public class DefaultPacket implements Serializable, Packet {

    private static final long serialVersionUID = -30847898951064299L;

    /**
     * Returns an empty packet which doesn't contains any data
     */
    static final DefaultPacket EMPTY_PACKET = new DefaultPacket(StringUtil.NULL, new Configuration());

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

    public DefaultPacket() {
    }

    public DefaultPacket(String type, Configuration configuration) {
        this(type, configuration, null);
    }

    public DefaultPacket(final String type, Configuration configuration,
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

    public DefaultPacket(final String type, Configuration configuration,
                         long channel) {
        this(type, configuration, null, channel);
    }

    public DefaultPacket(final String type, Configuration configuration, UUID resultID) {
        this(type, configuration, resultID,
            ChannelConstants.REFORMCLOUD_INTERNAL_INFORMATION_DEFAULT_CHANNEL);
    }

    public DefaultPacket(final String type, Map<? extends String, ?> configuration) {
        this(type, Configuration.fromMap(configuration));
    }

    public DefaultPacket(final String type, Map<? extends String, ?> configuration, UUID resultID) {
        this(type, Configuration.fromMap(configuration), resultID);
    }

    @Override
    public void read(ByteBuf byteBuf) {
        if (byteBuf.readableBytes() != 0) {
            String content = readString(byteBuf);
            DefaultPacket packet = ReformCloudLibraryService.GSON.fromJson(content, TypeTokenAdaptor.getPACKET_TYPE());
            this.configuration = packet.configuration;
            this.type = packet.type;
            this.result = packet.result;
            this.channel = packet.channel;
        }
    }

    @Override
    public void write(ByteBuf byteBuf) {
        byte[] bytes = ReformCloudLibraryService.GSON.toJson(this).getBytes(StandardCharsets.UTF_8);
        LengthEncoder.write(byteBuf, bytes.length).writeBytes(bytes);
    }

    private String readString(ByteBuf byteBuf) {
        int i = LengthDecoder.readVarInt(byteBuf);
        byte[] buffer = new byte[i];
        byteBuf.readBytes(buffer, 0, i);
        return new String(buffer, StandardCharsets.UTF_8);
    }

    @Override
    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public UUID getResult() {
        return this.result;
    }

    @Override
    public long getChannel() {
        return channel;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setResult(UUID result) {
        this.result = result;
    }

    @Override
    public void setChannel(long channel) {
        this.channel = channel;
    }
}
