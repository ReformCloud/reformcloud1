/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.extendable;

import systems.reformcloud.addons.JavaAddon;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public interface AddonPreLoader {

    /**
     * Loads the Addon main class and creates a new instance
     *
     * @return The loaded java addon
     * @throws Throwable The exception will be thrown if any error occurs while loading the addon
     */
    JavaAddon loadAddon() throws Throwable;
}
