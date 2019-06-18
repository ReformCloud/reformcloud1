/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.abstracts;

import jline.console.completer.Completer;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public abstract class AbstractCommandCompleter implements Completer,
    Serializable {

    /**
     * This method should calculate the new cursor position after giving the
     * tab complete candidates
     *
     * @param arg1 The buffer of the tab complete
     * @param arg2 The current cursor position
     * @return The new cursor position
     */
    public abstract int calculateCursorPosition(String arg1, int arg2);
}
