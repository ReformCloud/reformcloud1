/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.rethinkdb.dependency;

import systems.reformcloud.addons.dependency.util.DynamicDependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 12.08.2019
 */

public final class RethinkDBJavaDriver extends DynamicDependency implements Serializable {

    @Override
    public String getGroupID() {
        return "com.rethinkdb";
    }

    @Override
    public String getName() {
        return "rethinkdb-driver";
    }

    @Override
    public String getVersion() {
        return "2.3.3";
    }
}
