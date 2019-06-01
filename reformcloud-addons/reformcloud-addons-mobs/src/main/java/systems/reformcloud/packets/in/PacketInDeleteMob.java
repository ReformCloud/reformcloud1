/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.packets.in;

import java.io.Serializable;
import java.util.UUID;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.selector.MobSelector;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketInDeleteMob implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        UUID mob = configuration.getValue("mob", UUID.class);
        MobSelector.getInstance().deleteMob(mob);
    }
}
