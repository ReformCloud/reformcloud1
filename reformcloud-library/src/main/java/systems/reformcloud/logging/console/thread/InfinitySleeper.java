/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.console.thread;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 20.06.2019
 */

public abstract class InfinitySleeper implements Serializable {

    public abstract void sleep();
}
