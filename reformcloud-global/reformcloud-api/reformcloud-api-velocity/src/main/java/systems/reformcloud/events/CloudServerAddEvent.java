/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import systems.reformcloud.meta.info.ServerInfo;

/**
 * This class represents the cloud server info update
 * event
 *
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

public final class CloudServerAddEvent {
    private ServerInfo serverInfo;

    @java.beans.ConstructorProperties({"serverInfo"})
    public CloudServerAddEvent(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }
}
