/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.extendable;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public abstract class AddonExtendable {

    /**
     * This method will load the addons
     */
    public abstract void loadAddons();

    /**
     * This method will enable the addons
     */
    public abstract void enableAddons();

    /**
     * This method disables the addons
     */
    public abstract void disableAddons();
}
