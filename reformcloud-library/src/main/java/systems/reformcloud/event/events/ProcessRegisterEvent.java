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

@AllArgsConstructor
@Getter
public class ProcessRegisterEvent extends Event implements Serializable {
    private static final long serialVersionUID = -8275832105157826283L;

    private boolean cancelled, serverGroup, proxyGroup;
    private String name;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
}
