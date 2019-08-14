/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database.player.mongo.dependency;

import systems.reformcloud.addons.dependency.util.DynamicDependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.08.2019
 */

public final class MongoDatabaseJavaDriver extends DynamicDependency implements Serializable {

    @Override
    public String getGroupID() {
        return "org.mongodb";
    }

    @Override
    public String getName() {
        return "mongo-java-driver";
    }

    @Override
    public String getVersion() {
        return "3.11.0";
    }
}
