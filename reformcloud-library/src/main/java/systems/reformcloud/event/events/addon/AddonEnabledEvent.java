/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.addon;

import systems.reformcloud.addons.AddonLoader;
import systems.reformcloud.addons.JavaAddon;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public final class AddonEnabledEvent extends AddonEvent implements Serializable {

    public AddonEnabledEvent(AddonLoader addonLoader, JavaAddon javaAddon) {
        super(addonLoader);
        this.javaAddon = javaAddon;
    }

    private final JavaAddon javaAddon;

    public JavaAddon getJavaAddon() {
        return javaAddon;
    }
}
