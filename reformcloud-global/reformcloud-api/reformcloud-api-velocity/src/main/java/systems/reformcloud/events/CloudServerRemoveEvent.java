/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import systems.reformcloud.meta.info.ServerInfo;

/**
 * This class represents the cloud server remove event in the whole cloud
 *
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

public final class CloudServerRemoveEvent {

    private ServerInfo serverInfo;

    @java.beans.ConstructorProperties({"serverInfo"})
    public CloudServerRemoveEvent(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }
}
