/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import systems.reformcloud.meta.info.ServerInfo;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author _Klaro | Pasqual K. / created on 14.03.2019
 */

public interface IDefaultPlayerProvider {
    /**
     * The atomic reference to get the default instance of the player provider
     */
    AtomicReference<IDefaultPlayerProvider> instance = new AtomicReference<>();

    /**
     * Sends a player to the given server
     *
     * @param uniqueID The uuid of the player which is online
     * @param name     The name of the server where the player should be sent to
     */
    void sendPlayer(UUID uniqueID, String name);

    /**
     * Sends a player to the given server
     *
     * @param uniqueID      The uuid of the player which is online
     * @param serverInfo    The serverInfo of the server where the player should be sent to
     */
    void sendPlayer(UUID uniqueID, ServerInfo serverInfo);

    /**
     * Sends the player a specific message
     *
     * @param uniqueID      The uuid of the player which is online
     * @param message       The message that should be sent
     */
    void sendMessage(UUID uniqueID, String message);

    /**
     * Kicks a player from the network
     *
     * @param uniqueID      The uuid of the player which is online
     * @param reason        The reason why the player got kicked
     */
    void kickPlayer(UUID uniqueID, String reason);
}
