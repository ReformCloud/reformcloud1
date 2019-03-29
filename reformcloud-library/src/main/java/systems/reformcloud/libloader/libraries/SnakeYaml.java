/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.libloader.libraries;

import systems.reformcloud.libloader.utility.Dependency;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 18.03.2019
 */

public final class SnakeYaml extends Dependency implements Serializable {
    public SnakeYaml() {
        super(null);
    }

    @Override
    public String getGroupID() {
        return "org.yaml";
    }

    @Override
    public String getName() {
        return "snakeyaml";
    }

    @Override
    public String getVersion() {
        return "1.24";
    }
}
