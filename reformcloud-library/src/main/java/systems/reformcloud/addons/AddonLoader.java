/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons;

import systems.reformcloud.addons.extendable.AddonExtendable;

import java.io.Serializable;
import java.util.Queue;

/**
 * @author _Klaro | Pasqual K. / created on 17.06.2019
 */

public abstract class AddonLoader extends AddonExtendable implements Serializable {

    public abstract boolean disableAddon(final String name);

    public abstract boolean enableAddon(final String name);

    public abstract boolean isAddonEnabled(final String name);

    public abstract Queue<JavaAddon> getJavaAddons();
}
