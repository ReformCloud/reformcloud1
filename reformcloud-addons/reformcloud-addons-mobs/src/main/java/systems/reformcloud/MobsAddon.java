/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.selector.MobSelector;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class MobsAddon extends ControllerAddonImpl implements Serializable {
    @Override
    public void onAddonClazzPrepare() {
    }

    @Override
    public void onAddonLoading() {
        new MobSelector();
    }

    @Override
    public void onAddonReadyToClose() {
        if (MobSelector.getInstance() == null)
            return;

        MobSelector.getInstance().close();
    }
}
