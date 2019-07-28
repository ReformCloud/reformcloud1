/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 28.01.2019
 */

public final class CommonsIO extends Dependency implements Serializable {

    private static final long serialVersionUID = 8266556935851895180L;

    public CommonsIO() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "commons-io";
    }

    @Override
    public String getName() {
        return "commons-io";
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Dependency setVersion(String version) {
        this.version = version;
        return this;
    }
}
