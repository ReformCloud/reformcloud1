/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server.stats;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.02.2019
 */

public final class TempServerStats implements Serializable {

    /**
     * The placed blocks on the current server since the last update
     */
    public long blocksPlaced = 0L;

    /**
     * The walked distance on the server since the last update
     */
    public long distanceWalked = 0L;

    /**
     * The online time of the server since the last update
     */
    public long onlineTime = 0L;

    /**
     * The temp walked distance
     */
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

    /**
     * Resets the current stats
     */
    public void reset() {
        this.blocksPlaced = 0L;
        this.distanceWalked = 0L;
    }

    /**
     * Checks if the current stats have changed
     *
     * @return If the current stats have changed since the last update
     */
    public boolean hasChanges() {
        return this.blocksPlaced != 0 || this.distanceWalked != 0;
    }
}
