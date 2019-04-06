/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.proxy.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import systems.reformcloud.utility.map.maps.Double;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

@AllArgsConstructor
@Data
public final class ProxySettings implements Serializable {
    private String targetProxyGroup, tabHeader, tabFooter, maintenanceProtocol, protocol;
    private String[] playerInfo;
    private boolean protocolEnabled, tabEnabled, motdEnabled, slotCounter;
    private int moreSlots;
    private List<Double<String, String>> normalMotd, maintenanceMotd;
}
