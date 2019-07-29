/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.07.2019
 */

public abstract class AbstractTaskScheduler implements Serializable {

    /**
     * Schedules a specific runnable in the given time
     *
     * @param arg1 The runnable which should be executed
     * @param arg2 The repeats or {@code -1} to repeat forever
     * @param arg3 The delay time in milliseconds
     * @return The current class instance
     */
    public abstract AbstractTaskScheduler schedule(Runnable arg1,
                                                   long arg2,
                                                   long arg3);

    /**
     * Closes all running tasks
     */
    public abstract void close();
}
