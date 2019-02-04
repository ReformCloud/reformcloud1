/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.startup;

import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.server.ServerGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

@AllArgsConstructor
@Getter
public class ServerStartupInfo implements Serializable {
    private static final long serialVersionUID = 3276684735275715610L;

    private UUID uid;
    private String name, template;
    private ServerGroup serverGroup;
    private Configuration configuration;
    private int id;
}
