/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server.defaults;

import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 09.12.2018
 */

public class DefaultGroup extends ServerGroup implements Serializable {
    private static final long serialVersionUID = -1234409573533112793L;

    public DefaultGroup(final String name, final String client) {
        super(
                name,
                "ReformCloud",
                null,
                Collections.singletonList(client),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                512,
                1,
                -1,
                50,
                41000,
                true,
                ServerModeType.GAME_SERVER,
                SpigotVersions.SPIGOT_1_8_8
        );
    }
}
