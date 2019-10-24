/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.mysql.dependency;

import systems.reformcloud.addons.dependency.util.DynamicDependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.08.2019
 */

public final class MySQLConnectorJava extends DynamicDependency implements Serializable {

    @Override
    public String getGroupID() {
        return "mysql";
    }

    @Override
    public String getName() {
        return "mysql-connector-java";
    }

    @Override
    public String getVersion() {
        return "8.0.17";
    }
}
