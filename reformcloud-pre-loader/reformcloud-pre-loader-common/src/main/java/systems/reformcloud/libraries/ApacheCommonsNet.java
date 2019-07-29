/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class ApacheCommonsNet extends Dependency implements Serializable {

    public ApacheCommonsNet() {
        super(null, "3.6");
    }

    @Override
    public String getGroupID() {
        return "commons-net";
    }

    @Override
    public String getName() {
        return "commons-net";
    }
}
