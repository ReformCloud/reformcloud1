/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.meta.info.ServerInfo;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Getter
public final class Sign implements Serializable {
    /**
     * The uuid of the sign
     */
    private UUID uuid;

    /**
     * The position of the sign
     */
    private SignPosition signPosition;

    /**
     * The server info of the sign
     */
    @Setter
    private ServerInfo serverInfo;
}
