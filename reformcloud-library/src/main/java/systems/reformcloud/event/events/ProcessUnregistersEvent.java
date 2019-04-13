/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.event.utility.Event;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

@Getter
@AllArgsConstructor
public class ProcessUnregistersEvent extends Event implements Serializable {
    private static final long serialVersionUID = 6325756341292534826L;

    public String name;

    @Override
    @Deprecated
    public void setCancelled(boolean cancelled) {
    }

    @Override
    @Deprecated
    public boolean isCancelled() {
        return false;
    }
}
