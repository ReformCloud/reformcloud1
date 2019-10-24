/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.runtime;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.utility.threading.AbstractTaskScheduler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class TaskEvent extends Event implements Serializable {

    public TaskEvent(AbstractTaskScheduler taskScheduler, Runnable execute) {
        this.taskScheduler = taskScheduler;
        this.runnable = execute;
    }

    private final AbstractTaskScheduler taskScheduler;

    private final Runnable runnable;

    public Runnable getRunnable() {
        return runnable;
    }

    public AbstractTaskScheduler getTaskScheduler() {
        return taskScheduler;
    }
}
