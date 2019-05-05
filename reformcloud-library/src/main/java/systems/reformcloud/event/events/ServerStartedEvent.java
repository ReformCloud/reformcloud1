/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;

/**
 * The event will be called when a server process was started successfully
 *
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

public final class ServerStartedEvent extends Event implements Serializable {
    /**
     * The server info of the started process
     */
    private ServerInfo serverInfo;

    @java.beans.ConstructorProperties({"serverInfo"})
    public ServerStartedEvent(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }
}
