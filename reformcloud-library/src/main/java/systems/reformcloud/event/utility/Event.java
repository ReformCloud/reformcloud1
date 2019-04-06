/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.utility;

import lombok.Getter;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

@Getter
public abstract class Event extends Cancelable {
    public Event() {
        this.callable = true;
    }

    public Event(boolean callable) {
        this.callable = callable;
    }

    private boolean callable;
}
