/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.extendable;

import systems.reformcloud.addons.JavaAddon;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public interface ModulePreLoader {
    JavaAddon loadAddon() throws Throwable;
}
