/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.mobsaddon.packet.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.mobs.config.SelectorMobConfig;
import systems.reformcloud.mobsaddon.MobSelector;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PacketInUpdateMobs implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        Map<UUID, SelectorMob> mobs = configuration
            .getValue("mobs", new TypeToken<Map<UUID, SelectorMob>>() {
            });
        SelectorMobConfig selectorMobConfig = configuration
            .getValue("config", new TypeToken<SelectorMobConfig>() {
            });

        MobSelector.getInstance().setMobs(mobs);
        MobSelector.getInstance().setSelectorMobConfig(selectorMobConfig);
    }
}
