/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.libraries;

import systems.reformcloud.libloader.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 20.04.2019
 */

public final class Guava extends Dependency implements Serializable {
    public Guava() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "com.google.guava";
    }

    @Override
    public String getName() {
        return "guava";
    }

    @Override
    public String getVersion() {
        return "27.1-jre";
    }
}
