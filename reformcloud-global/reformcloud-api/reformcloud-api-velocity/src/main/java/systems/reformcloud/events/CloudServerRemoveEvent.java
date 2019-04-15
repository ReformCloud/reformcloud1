/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.meta.info.ServerInfo;

/**
 * This class represents the cloud server remove event
 * in the whole cloud
 *
 * @author _Klaro | Pasqual K. / created on 11.11.2018
 * @since RCS1.0
 */

@AllArgsConstructor
@Getter
public final class CloudServerRemoveEvent {
    private ServerInfo serverInfo;
}
