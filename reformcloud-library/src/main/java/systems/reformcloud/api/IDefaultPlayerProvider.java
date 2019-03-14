/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.meta.info.ServerInfo;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public interface IDefaultPlayerProvider {
    void sendPlayer(UUID uniqueID, String name);

    void sendPlayer(UUID uniqueID, ServerInfo serverInfo);

    void sendMessage(UUID uniqueID, String message);

    void kickPlayer(UUID uniqueID, String reason);
}
