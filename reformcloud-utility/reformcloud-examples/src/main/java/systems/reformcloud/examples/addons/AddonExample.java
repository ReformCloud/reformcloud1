/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.examples.addons;

import systems.reformcloud.utility.ControllerAddonImpl;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public final class AddonExample extends ControllerAddonImpl implements Serializable {
    /**
     * This method will be called when the controller is currently loading the addon main class
     */
    @Override
    public void onAddonClazzPrepare() {
    }

    /**
     * This method will be called when the controller is enabling all addons
     */
    @Override
    public void onAddonLoading() {
    }

    /**
     * This method will be called when the controller is stopping and disabling all addons
     */
    @Override
    public void onAddonReadyToClose() {
    }
}
