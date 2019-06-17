/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.handlers;

import systems.reformcloud.logging.ColouredConsoleProvider;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 *
 * Register a Handler in {@link ColouredConsoleProvider} to get the console input
 */

public interface IConsoleInputHandler {

    void handle(String message);
}
