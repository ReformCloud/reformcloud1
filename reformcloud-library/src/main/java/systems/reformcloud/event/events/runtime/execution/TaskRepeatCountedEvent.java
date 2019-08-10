/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.event.events.runtime.execution;

import systems.reformcloud.utility.threading.TaskQueueEntry;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class TaskRepeatCountedEvent extends ExecutionTaskEvent implements Serializable {

    public TaskRepeatCountedEvent(TaskQueueEntry taskQueueEntry, long currentRepeats) {
        super(taskQueueEntry, currentRepeats);
    }
}
