/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.01.2019
 */

public final class Netty extends Dependency implements Serializable {

    private static final long serialVersionUID = 5273979933426169372L;

    public Netty() {
        super(null, "4.1.38.Final");
    }

    @Override
    public String getGroupID() {
        return "io.netty";
    }

    @Override
    public String getName() {
        return "netty-all";
    }
}
