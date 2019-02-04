/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import lombok.Getter;
import systems.reformcloud.signs.SignSelector;
import systems.reformcloud.utility.ControllerAddonImpl;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public final class SignAddon extends ControllerAddonImpl implements Serializable {
    private static final long serialVersionUID = -3291689192807455021L;

    @Getter
    private static SignSelector signSelector;

    @Override
    public void onAddonClazzPrepare() {
        try {
            signSelector = new SignSelector();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onAddonReadyToClose() {
        if (signSelector != null) {
            signSelector.shutdownAll();
        }
    }
}
