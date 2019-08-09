/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.update;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.meta.server.stats.TempServerStats;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

public final class ServerTempStatsUpdateEvent extends Event implements Serializable {

    private TempServerStats tempServerStats;

    @java.beans.ConstructorProperties({"tempServerStats"})
    public ServerTempStatsUpdateEvent(TempServerStats tempServerStats) {
        this.tempServerStats = tempServerStats;
    }

    public TempServerStats getTempServerStats() {
        return this.tempServerStats;
    }
}
