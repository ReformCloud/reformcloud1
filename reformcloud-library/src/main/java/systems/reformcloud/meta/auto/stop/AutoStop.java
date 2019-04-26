/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.auto.stop;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 26.04.2019
 */

public final class AutoStop implements Serializable {
    private boolean enabled;
    private long checkEverySeconds;

    @java.beans.ConstructorProperties({"enabled", "checkEverySeconds"})
    public AutoStop(boolean enabled, long checkEverySeconds) {
        this.enabled = enabled;
        this.checkEverySeconds = checkEverySeconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getCheckEverySeconds() {
        return checkEverySeconds;
    }

    public void setCheckEverySeconds(long checkEverySeconds) {
        this.checkEverySeconds = checkEverySeconds;
    }
}
