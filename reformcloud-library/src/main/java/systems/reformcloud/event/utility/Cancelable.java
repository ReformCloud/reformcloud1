/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.utility;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public abstract class Cancelable {
    public abstract void setCancelled(boolean cancelled);

    public abstract boolean isCancelled();
}
