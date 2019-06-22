/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.runtime;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public interface Shutdown extends Serializable {

    /**
     * Shutdowns something
     */
    void shutdownAll();
}
