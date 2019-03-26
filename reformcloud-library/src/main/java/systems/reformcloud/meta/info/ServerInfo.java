/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.meta.CloudProcess;
import systems.reformcloud.meta.enums.ServerState;
import systems.reformcloud.meta.server.ServerGroup;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

@AllArgsConstructor
@Getter
@Setter
public final class ServerInfo implements Serializable {
    private static final long serialVersionUID = 8057730391607929124L;

    private CloudProcess cloudProcess;

    private ServerGroup serverGroup;

    private ServerState serverState;

    private String group, host, motd;
    private int port, online, maxMemory;

    private boolean full;

    private List<UUID> onlinePlayers;
}
