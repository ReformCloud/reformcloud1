/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.meta.info.ProxyInfo;

import java.io.Serializable;

/**
 * This class represents the ProxyStartup as event
 *
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 */

@AllArgsConstructor
@Getter
public final class CloudProxyAddEvent implements Serializable {
    private ProxyInfo proxyInfo;
}
