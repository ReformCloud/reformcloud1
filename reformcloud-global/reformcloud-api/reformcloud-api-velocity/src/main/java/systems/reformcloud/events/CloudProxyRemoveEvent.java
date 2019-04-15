/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.meta.info.ProxyInfo;

/**
 * This class represents the remove of a cloud proxy
 *
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 * @since RCS1.0
 */

@AllArgsConstructor
@Getter
public final class CloudProxyRemoveEvent {
    private ProxyInfo proxyInfo;
}
