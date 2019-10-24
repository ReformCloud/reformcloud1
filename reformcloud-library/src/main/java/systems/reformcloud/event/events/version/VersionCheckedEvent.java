/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.version;

import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class VersionCheckedEvent extends Event implements Serializable {

    public VersionCheckedEvent(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    private final String latestVersion;

    public String getLatestVersion() {
        return latestVersion;
    }
}
