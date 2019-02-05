/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.screen;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public abstract class ScreenHandler implements Serializable {
    private static final long serialVersionUID = 937457820217096807L;

    @Getter
    private String client;

    protected ScreenHandler(final String client) {
        this.client = client;
    }

    public abstract void sendLine(String in);

    public abstract void executeCommand(String cmd);
}
