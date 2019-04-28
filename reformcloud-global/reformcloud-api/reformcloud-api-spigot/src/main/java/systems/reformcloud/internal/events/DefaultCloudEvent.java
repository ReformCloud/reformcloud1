/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.internal.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public abstract class DefaultCloudEvent extends Event implements Serializable {
    public DefaultCloudEvent() {
        super(!Bukkit.getServer().isPrimaryThread());
    }
}
