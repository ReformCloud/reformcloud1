/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.console.thread;

import systems.reformcloud.utility.annotiations.InternalClass;
import systems.reformcloud.utility.annotiations.NotDocumented;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 20.06.2019
 */

@InternalClass
public abstract class InfinitySleeper implements Serializable {

    @NotDocumented
    public abstract void sleep();
}
