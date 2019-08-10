/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.runtime.execution;

import systems.reformcloud.event.utility.Event;
import systems.reformcloud.utility.threading.TaskQueueEntry;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class ExecutionTaskEvent extends Event implements Serializable {

    public ExecutionTaskEvent(TaskQueueEntry taskQueueEntry, long currentRepeats) {
        this.taskQueueEntry = taskQueueEntry;
        this.currentRepeats = currentRepeats;
    }

    private final TaskQueueEntry taskQueueEntry;

    private final long currentRepeats;

    public TaskQueueEntry getTaskQueueEntry() {
        return taskQueueEntry;
    }

    public long getCurrentRepeats() {
        return currentRepeats;
    }
}
