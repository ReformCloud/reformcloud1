/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.libraries;

import systems.reformcloud.libloader.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 24.01.2019
 */

public final class ApacheHttpCore extends Dependency implements Serializable {
    private static final long serialVersionUID = 727304906145669453L;

    public ApacheHttpCore() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "org.apache.httpcomponents";
    }

    @Override
    public String getName() {
        return "httpcore";
    }

    @Override
    public String getVersion() {
        return "4.4.11";
    }
}
