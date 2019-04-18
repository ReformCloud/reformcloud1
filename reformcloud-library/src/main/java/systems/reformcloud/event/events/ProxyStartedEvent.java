/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * The event will be called when a proxy process starts up
 *
 * @author _Klaro | Pasqual K. / created on 16.04.2019
 */

@AllArgsConstructor
@Getter
public final class ProxyStartedEvent extends Event implements Serializable {
    /**
     * The proxy info of the started process
     */
    private ProxyInfo proxyInfo;
}
