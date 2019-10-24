/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.handlers;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public interface ConsoleInputHandler extends Serializable {

    /**
     * Get called when a line is printed into the console
     *
     * @param arg1 The string which is printed into the console
     */
    void handle(String arg1);
}
