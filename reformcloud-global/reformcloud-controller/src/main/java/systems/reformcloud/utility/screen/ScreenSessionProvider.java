/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.screen;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.network.sync.out.PacketOutSyncScreenDisable;
import systems.reformcloud.network.sync.out.PacketOutSyncScreenJoin;

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
        if (!this.isInScreen()) {
            this.screens.put(name, screenHandler);
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(screenHandler.getClient(), new PacketOutSyncScreenJoin(name));
            return true;
        }

        return false;
    }

    public boolean leaveScreen() {
        if (this.isInScreen()) {
            ScreenHandler screenHandler = this.screens.values().stream().findFirst().orElse(null);
            if (screenHandler == null)
                return false;

            final String name = this.getNameByScreen(screenHandler);
            this.screens.clear();
            ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(screenHandler.getClient(), new PacketOutSyncScreenDisable(name));
            return true;
        }

        return false;
    }

    public boolean isInScreen() {
        return !this.screens.isEmpty();
    }

    public boolean isInScreen(final String name) {
        return this.isInScreen() && this.screens.containsKey(name);
    }

    public void sendScreenMessage(final String message, final String name) {
        if (this.screens.containsKey(name)) {
            this.screens.get(name).sendLine(message);
        }
    }

    public void executeCommand(final String cmd) {
        if (this.isInScreen()) {
            ScreenHandler screenHandler = this.screens.values().stream().findFirst().orElse(null);
            if (screenHandler == null)
                return;

            screenHandler.executeCommand(cmd);
        }
    }

    private String getNameByScreen(final ScreenHandler screenHandler) {
        for (Map.Entry<String, ScreenHandler> map : this.screens.entrySet()) {
            if (map.getValue().equals(screenHandler))
                return map.getKey();
        }

        return null;
    }
}
