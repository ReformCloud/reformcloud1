/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.auto.start;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 26.04.2019
 */

public final class AutoStart implements Serializable {

    /**
     * The current state of the auto start
     */
    private boolean enabled;

    /**
     * The max players when a server should be started
     */
    private int playerMax;

    /**
     * The allowed
     */
    private long allowAutoStartEverySeconds;

    @java.beans.ConstructorProperties({"enabled", "playerMax", "allowAutoStartEverySeconds"})
    public AutoStart(boolean enabled, int playerMax, long allowAutoStartEverySeconds) {
        this.enabled = enabled;
        this.playerMax = playerMax;
        this.allowAutoStartEverySeconds = allowAutoStartEverySeconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPlayerMax() {
        return playerMax;
    }

    public void setPlayerMax(int playerMax) {
        this.playerMax = playerMax;
    }

    public long getAllowAutoStartEverySeconds() {
        return allowAutoStartEverySeconds;
    }

    public void setAllowAutoStartEverySeconds(long allowAutoStartEverySeconds) {
        this.allowAutoStartEverySeconds = allowAutoStartEverySeconds;
    }
}
