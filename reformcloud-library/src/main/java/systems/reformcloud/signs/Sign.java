/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

public final class Sign implements Serializable {

    /**
     * The uuid of the sign
     */
    private UUID uuid;

    /**
     * The position of the sign
     */
    private SignPosition signPosition;

    /**
     * The server info of the sign
     */
    private ServerInfo serverInfo;

    @java.beans.ConstructorProperties({"uuid", "signPosition", "serverInfo"})
    public Sign(UUID uuid, SignPosition signPosition, ServerInfo serverInfo) {
        this.uuid = uuid;
        this.signPosition = signPosition;
        this.serverInfo = serverInfo;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public SignPosition getSignPosition() {
        return this.signPosition;
    }

    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }
}
