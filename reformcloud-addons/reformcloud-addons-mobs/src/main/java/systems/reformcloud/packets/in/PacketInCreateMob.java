/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.packets.in;

import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.selector.MobSelector;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketInCreateMob implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        SelectorMob selectorMob = configuration.getValue("mob", new TypeToken<SelectorMob>() {
        });
        MobSelector.getInstance().createMob(selectorMob);
    }
}
