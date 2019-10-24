/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 22.01.2019
 */

public final class Gson extends Dependency implements Serializable {

    private static final long serialVersionUID = -6226344132999747554L;

    public Gson() {
        super(null, "2.8.6");
    }

    @Override
    public String getGroupID() {
        return "com.google.code.gson";
    }

    @Override
    public String getName() {
        return "gson";
    }
}
