/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.addon;

import systems.reformcloud.addons.AddonLoader;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.event.utility.Cancellable;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.08.2019
 */

public final class AddonPreEnableEvent extends AddonEvent implements Serializable, Cancellable {

    public AddonPreEnableEvent(AddonLoader addonLoader, JavaAddon javaAddon) {
        super(addonLoader);
        this.javaAddon = javaAddon;
    }

    private final JavaAddon javaAddon;

    public JavaAddon getJavaAddon() {
        return javaAddon;
    }

    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
