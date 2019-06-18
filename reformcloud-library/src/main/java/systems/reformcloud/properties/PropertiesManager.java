/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.properties;

import systems.reformcloud.utility.annotiations.ShouldNotBeNull;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public abstract class PropertiesManager implements Serializable {

    public abstract void fill(@ShouldNotBeNull String arg1,
                              @ShouldNotBeNull Properties arg2);

    public abstract PropertiesConfig getPropertiesConfig();

    public abstract void delete();

    public abstract boolean isPresent();
}
