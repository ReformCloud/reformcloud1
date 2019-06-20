/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.console.thread;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.05.2019
 */

public final class DefaultInfinitySleeper extends InfinitySleeper implements Serializable {

    private volatile boolean running = true;

    public DefaultInfinitySleeper() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::deleteInstant));
    }

    @Override
    public void sleep() {
        while (running) {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (final InterruptedException ignored) {
            }
        }
    }

    private void deleteInstant() {
        running = false;
    }
}