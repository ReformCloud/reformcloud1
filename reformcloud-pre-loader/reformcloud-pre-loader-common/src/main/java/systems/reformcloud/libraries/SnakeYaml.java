/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libraries;

import systems.reformcloud.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 18.03.2019
 */

public final class SnakeYaml extends Dependency implements Serializable {

    public SnakeYaml() {
        super(null, "1.24");
    }

    @Override
    public String getGroupID() {
        return "org.yaml";
    }

    @Override
    public String getName() {
        return "snakeyaml";
    }
}
