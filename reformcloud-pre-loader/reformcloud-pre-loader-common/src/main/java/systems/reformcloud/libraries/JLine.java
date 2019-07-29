/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.01.2019
 */

public final class JLine extends Dependency implements Serializable {

    private static final long serialVersionUID = -5341268101711897840L;

    public JLine() {
        super(null, "2.14.6");
    }

    @Override
    public String getGroupID() {
        return "jline";
    }

    @Override
    public String getName() {
        return "jline";
    }
}
