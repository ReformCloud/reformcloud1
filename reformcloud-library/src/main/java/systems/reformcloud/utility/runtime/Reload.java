/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.runtime;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public interface Reload {

    /**
     * This method get called to reload something
     *
     * @throws Throwable If any exception occurs will be catch here
     */
    void reloadAll() throws Throwable;
}
