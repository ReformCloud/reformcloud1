/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.proxy.ProxyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

@AllArgsConstructor
@Getter
@Setter
public final class ProxyInfo implements Serializable {
    private static final long serialVersionUID = -3889580749596738985L;

    private CloudProcess cloudProcess;
    private ProxyGroup proxyGroup;

    private String group, host;
    private int port, online, maxMemory;

    private boolean full;
    private List<UUID> onlinePlayers;
}
