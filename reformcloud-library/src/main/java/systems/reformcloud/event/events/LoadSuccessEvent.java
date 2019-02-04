/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events;

import systems.reformcloud.event.utility.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

@Getter
@AllArgsConstructor
public class LoadSuccessEvent extends Event implements Serializable {
    private static final long serialVersionUID = -2086515156432669832L;

    private boolean done;

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
