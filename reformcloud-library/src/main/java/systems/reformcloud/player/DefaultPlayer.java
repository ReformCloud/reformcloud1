/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.player.version.SpigotVersion;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 22.02.2019
 */

@AllArgsConstructor
@Getter
public class DefaultPlayer implements Serializable {
    private static final long serialVersionUID = -6988920422303669100L;

    @Setter
    private String name;

    private UUID uniqueID;
    private Map<String, Object> playerMeta;

    @Setter
    private long lastLogin;

    @Setter
    private SpigotVersion spigotVersion;
}
