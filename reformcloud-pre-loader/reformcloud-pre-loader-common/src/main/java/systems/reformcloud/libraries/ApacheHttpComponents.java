/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.01.2019
 */

public final class ApacheHttpComponents extends Dependency implements Serializable {
    private static final long serialVersionUID = -2104235898343838977L;

    public ApacheHttpComponents() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "org.apache.httpcomponents";
    }

    @Override
    public String getName() {
        return "httpclient";
    }

    @Override
    public String getVersion() {
        return "4.5.8";
    }
}
