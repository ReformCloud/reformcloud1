/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import systems.reformcloud.meta.info.ServerInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;

/**
 * @author _Klaro | Pasqual K. / created on 07.11.2018
 */

@AllArgsConstructor
@Getter
public class CloudServerAddEvent extends Event {
    private ServerInfo serverInfo;
}
