/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.libraries;

import systems.reformcloud.libloader.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 29.01.2019
 */

public final class CommonsLogging extends Dependency implements Serializable {
    public CommonsLogging() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "commons-logging";
    }

    @Override
    public String getName() {
        return "commons-logging";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }
}
