/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signal;

import sun.misc.Signal;
import sun.misc.SignalHandler;
import systems.reformcloud.ReformCloudLibraryServiceProvider;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class SignalBlocker implements Serializable {
    public SignalBlocker() {
        SignalHandler handler = new Handler();

        try {
            Signal.handle(new Signal("TERM"), handler);
            Signal.handle(new Signal("HUP"), handler);
            Signal.handle(new Signal("INT"), handler);
            Signal.handle(new Signal("SIGTERM"), handler);
        } catch (final Exception ignored) {
        }
    }

    private static final class Handler implements SignalHandler, Serializable {
        @Override
        public void handle(Signal signal) {
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("Please note that only the stop command is supported");
        }
    }
}
