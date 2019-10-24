/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.addon;

import systems.reformcloud.addons.AddonLoader;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public abstract class AddonEvent extends Event implements Serializable {

    public AddonEvent(AddonLoader addonLoader) {
        this.provider = addonLoader;
    }

    private final AddonLoader provider;

    public AddonLoader getProvider() {
        return provider;
    }
}
