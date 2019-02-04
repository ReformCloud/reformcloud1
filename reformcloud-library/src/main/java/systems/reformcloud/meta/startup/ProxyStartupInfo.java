/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.startup;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.ProxyGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

@AllArgsConstructor
@Data
public class ProxyStartupInfo implements Serializable {
    private static final long serialVersionUID = - 295123813122792999L;

    private UUID uid;
    private String name, template;
    private ProxyGroup proxyGroup;
    private Configuration configuration;
    private int id;
}
