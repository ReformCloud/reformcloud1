/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.query.out;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.dev.DevProcess;
import systems.reformcloud.network.packet.Packet;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 24.04.2019
 */

public final class PacketOutStartDevProcessResult extends Packet implements Serializable {

    public PacketOutStartDevProcessResult(UUID requester, DevProcess result) {
        super(
            StringUtil.NULL,
            new Configuration().addValue("result", result),
            requester
        );
    }
}
