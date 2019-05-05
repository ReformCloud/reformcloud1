/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.event.events.ServerTempStatsUpdateEvent;
import systems.reformcloud.meta.server.stats.TempServerStats;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class PacketInUpdateServerTempStats implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        final TempServerStats tempServerStats = configuration.getValue("stats", new TypeToken<TempServerStats>() {
        }.getType());
        ReformCloudController.getInstance().getStatisticsProvider().updateServerStats(tempServerStats);
        ReformCloudController.getInstance().getEventManager().fire(new ServerTempStatsUpdateEvent(tempServerStats));
    }
}
