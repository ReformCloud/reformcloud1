/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signal;

import sun.misc.Signal;
import sun.misc.SignalHandler;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.ExitUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("If you really want to exit the application print 'y'");
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().serve("Please note that only the stop command is supported");

            try {
                if (bufferedReader.readLine().equalsIgnoreCase("y"))
                    System.exit(ExitUtil.TERMINATED);
                else
                    ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info("You cancelled the termination");
            } catch (final IOException ignored) {
            }
        }
    }
}
