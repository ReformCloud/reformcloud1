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

    /**
     * The name of the database
     *
     * @return The given name of the database
     */
    public abstract String getName();

    /**
     * Loads the database
     */
    public abstract void load();

    /**
     * Saves the database
     */
    public abstract void save();
}
