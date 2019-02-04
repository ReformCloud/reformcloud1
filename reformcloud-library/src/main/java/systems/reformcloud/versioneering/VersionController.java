/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.versioneering;

import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 08.01.2019
 */

public final class VersionController {
    public static boolean isVersionAvailable() {
        return !VersionLoader.getNewestVersion().equalsIgnoreCase(StringUtil.REFORM_VERSION);
    }
}
