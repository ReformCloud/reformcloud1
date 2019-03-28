/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.libraries;

import systems.reformcloud.libloader.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

public final class Quartz extends Dependency implements Serializable {
    public Quartz() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "org.quartz-scheduler";
    }

    @Override
    public String getName() {
        return "quartz";
    }

    @Override
    public String getVersion() {
        return "2.2.1";
    }
}
