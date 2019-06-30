/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import net.md_5.bungee.api.plugin.Event;
import systems.reformcloud.meta.info.ServerInfo;

/**
 * @author _Klaro | Pasqual K. / created on 29.06.2019
 */

public final class CloudServerInfoPreUpdateEvent extends Event implements Serializable {

    private ServerInfo oldInfo;

    private ServerInfo newInfo;

    @ConstructorProperties({"oldInfo", "newInfo"})
    public CloudServerInfoPreUpdateEvent(ServerInfo oldInfo,
        ServerInfo newInfo) {
        this.oldInfo = oldInfo;
        this.newInfo = newInfo;
    }

    public ServerInfo getOldInfo() {
        return oldInfo;
    }

    public ServerInfo getNewInfo() {
        return newInfo;
    }
}
