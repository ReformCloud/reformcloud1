/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.runtime;

import systems.reformcloud.utility.threading.AbstractTaskScheduler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class TaskPreparedEvent extends TaskEvent implements Serializable {

    public TaskPreparedEvent(AbstractTaskScheduler taskScheduler, Runnable execute) {
        super(taskScheduler, execute);
    }
}
