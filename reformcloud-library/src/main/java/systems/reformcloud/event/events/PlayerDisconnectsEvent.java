/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 17.04.2019
 */

@AllArgsConstructor
@Getter
public final class PlayerDisconnectsEvent extends Event implements Serializable {
    private UUID onlinePlayer;
}
