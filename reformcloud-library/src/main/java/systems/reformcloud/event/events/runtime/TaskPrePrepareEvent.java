/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.runtime;

import systems.reformcloud.event.utility.Cancellable;
import systems.reformcloud.utility.threading.AbstractTaskScheduler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class TaskPrePrepareEvent extends TaskEvent implements Serializable, Cancellable {

    public TaskPrePrepareEvent(AbstractTaskScheduler taskScheduler, Runnable execute) {
        super(taskScheduler, execute);
    }

    private boolean cancelled;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
