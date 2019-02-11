/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server.stats;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class TempServerStats implements Serializable {
    public long blocksPlaced, distanceWalked, onlineTime;
    private double walkedDistanceTemp = 0D;

    public void addWalkedDistance(final double distance) {
        this.walkedDistanceTemp += distance;
        if (walkedDistanceTemp >= 1) {
            this.distanceWalked += (long) walkedDistanceTemp;
            this.walkedDistanceTemp = 0D;
        }
    }

    public void addPlacedBlock() {
        this.blocksPlaced++;
    }

    public void addOnlineTime(final long start) {
        this.onlineTime = onlineTime + (System.currentTimeMillis() - start);
    }

    public void reset() {
        this.blocksPlaced = 0L;
        this.distanceWalked = 0L;
    }

    public boolean hasChanges() {
        return this.blocksPlaced != 0 || this.distanceWalked != 0;
    }

    public TempServerStats clone() {
        TempServerStats tempServerStats = new TempServerStats();
        tempServerStats.distanceWalked = distanceWalked;
        tempServerStats.blocksPlaced = blocksPlaced;
        return tempServerStats;
    }
}
