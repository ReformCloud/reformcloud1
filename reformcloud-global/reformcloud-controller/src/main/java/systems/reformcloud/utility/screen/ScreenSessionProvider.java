/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.screen;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.netty.sync.out.PacketOutSyncScreenDisable;
import systems.reformcloud.netty.sync.out.PacketOutSyncScreenJoin;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public final class ScreenSessionProvider implements Serializable {
    private static final long serialVersionUID = -7287732124453310814L;

    private final Map<String, ScreenHandler> screens = new ConcurrentHashMap<>();

    public boolean joinScreen(final String name, final ScreenHandler screenHandler) {
        if (!this.screens.containsKey(name)) {
            this.screens.put(name, screenHandler);
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(screenHandler.getClient(), new PacketOutSyncScreenJoin(name));
            return true;
        }

        return false;
    }

    public boolean leaveScreen(final String name) {
        if (this.screens.containsKey(name)) {
            ScreenHandler screenHandler = screens.get(name);
            this.screens.remove(name);
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(screenHandler.getClient(), new PacketOutSyncScreenDisable(name));
            return true;
        }

        return false;
    }

    public void sendScreenMessage(final String message, final String name) {
        if (this.screens.containsKey(name)) {
            this.screens.get(name).sendLine(message);
        }
    }

    public void executeCommand(final String name, final String cmd) {
        if (screens.containsKey(name)) {
            screens.get(name).executeCommand(cmd);
        }
    }
}
