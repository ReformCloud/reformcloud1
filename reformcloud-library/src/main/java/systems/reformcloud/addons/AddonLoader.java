/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons;

import java.io.Serializable;
import java.util.Queue;
import systems.reformcloud.addons.extendable.AddonExtendable;

/**
 * @author _Klaro | Pasqual K. / created on 17.06.2019
 */

public abstract class AddonLoader extends AddonExtendable implements Serializable {

    public abstract boolean disableAddon(final String arg1);

    public abstract boolean enableAddon(final String arg1);

    public abstract boolean isAddonEnabled(final String arg1);

    public abstract Queue<JavaAddon> getJavaAddons();
}
