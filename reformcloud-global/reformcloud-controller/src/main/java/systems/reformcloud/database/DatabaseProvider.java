/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.database;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 03.02.2019
 */

public abstract class DatabaseProvider implements Serializable {
    private static final long serialVersionUID = 4215067458872524204L;

    public abstract String getName();

    public abstract void load();

    public abstract void save();
}
