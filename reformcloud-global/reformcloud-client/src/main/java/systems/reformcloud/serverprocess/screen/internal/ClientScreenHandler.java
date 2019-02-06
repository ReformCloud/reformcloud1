/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.screen.internal;

import lombok.Getter;
import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.logging.handlers.IConsoleInputHandler;
import systems.reformcloud.serverprocess.screen.ScreenHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class ClientScreenHandler implements Serializable, IConsoleInputHandler {
    @Getter
    public final ScreenHandler screenHandler = new ScreenHandler(ReformCloudClient.getInstance().getCloudConfiguration().getClientName());

    @Override
    public void handle(String message) {
        screenHandler.addScreenLine(message);
    }
}
