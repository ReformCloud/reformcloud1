/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.completer;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public interface TabCompleter extends Serializable {

    /**
     * This method allows a command to be tab completed if the command
     * returns a non-empty list of candidates
     *
     * @param arg1 The typed command line
     * @param arg2 The given parameters
     * @return A list containing all candidates for the tab complete
     */
    List<String> complete(String arg1, String[] arg2);
}
