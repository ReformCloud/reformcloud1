/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;

/**
 * The event will be called if a server was stopped successfully
 *
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

@AllArgsConstructor
@Getter
public final class ServerStoppedEvent extends Event implements Serializable {
    /**
     * The server info of the stopped process
     */
    private ServerInfo serverInfo;
}
