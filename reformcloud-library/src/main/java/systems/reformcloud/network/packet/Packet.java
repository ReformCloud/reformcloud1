/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.packet;

import io.netty.buffer.ByteBuf;
import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public interface Packet extends Serializable {

    void read(ByteBuf byteBuf);

    void write(ByteBuf byteBuf);

    Configuration getConfiguration();

    String getType();

    UUID getResult();

    long getChannel();

    void setConfiguration(Configuration configuration);

    void setType(String type);

    void setResult(UUID result);

    void setChannel(long channel);
}
