/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;
import systems.reformcloud.meta.info.ProxyInfo;

/**
 * This class represents the ProxyStartup as event
 *
 * @since RCS1.0
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

@AllArgsConstructor
@Getter
public class CloudProxyAddEvent extends Event {
    private ProxyInfo proxyInfo;
}
