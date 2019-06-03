/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobsaddon.packet.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.mobsaddon.MobSelector;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketInDeleteMob implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        SelectorMob selectorMob = configuration.getValue("mob", new TypeToken<SelectorMob>() {
        });
        MobSelector.getInstance().handleDeleteMob(selectorMob);
    }
}
