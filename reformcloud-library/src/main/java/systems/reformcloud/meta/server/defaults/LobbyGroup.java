/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.server.defaults;

import systems.reformcloud.meta.Template;
import systems.reformcloud.meta.auto.start.AutoStart;
import systems.reformcloud.meta.auto.stop.AutoStop;
import systems.reformcloud.meta.enums.ServerModeType;
import systems.reformcloud.meta.enums.TemplateBackend;
import systems.reformcloud.meta.server.ServerGroup;
import systems.reformcloud.meta.server.versions.SpigotVersions;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public class LobbyGroup extends ServerGroup implements Serializable {
    private static final long serialVersionUID = -6740582229649845556L;

    public LobbyGroup(SpigotVersions spigotVersions, int memory, final String client) {
        super(
                "Lobby",
                "ReformCloud",
                null,
                Collections.singletonList(client),
                Collections.singletonList(new Template("default", null, TemplateBackend.CLIENT)),
                memory,
                1,
                -1,
                50,
                41000,
                false,
                false,
                new AutoStart(true, 45, TimeUnit.MINUTES.toSeconds(20)),
                new AutoStop(true, TimeUnit.MINUTES.toSeconds(5)),
                ServerModeType.LOBBY,
                spigotVersions
        );
    }

}
