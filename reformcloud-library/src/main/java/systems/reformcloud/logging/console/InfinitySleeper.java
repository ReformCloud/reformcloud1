/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.console;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.05.2019
 */

public final class InfinitySleeper implements Serializable {

    public InfinitySleeper() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::deleteInstant));
    }

    public void sleep() {
        while (running) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (final InterruptedException ignored) {
            }
        }
    }

    private volatile boolean running = true;

    private void deleteInstant() {
        running = false;
    }
}
