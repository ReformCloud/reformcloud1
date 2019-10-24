/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.screen.internal;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.logging.handlers.ConsoleInputHandler;
import systems.reformcloud.serverprocess.screen.ScreenHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class ClientScreenHandler extends ScreenHandler implements Serializable,
    ConsoleInputHandler {

    public ClientScreenHandler() {
        super(ReformCloudClient.getInstance().getCloudConfiguration().getClientName());
    }

    @Override
    public void handle(String message) {
        super.addScreenLine(message);
    }
}
