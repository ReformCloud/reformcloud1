/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.event.events.runtime.execution.*;
import systems.reformcloud.event.utility.Event;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.annotiations.Internal;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class TaskQueueEntry implements Serializable {

    private Runnable prepared;

    private Runnable run;

    private long repeats;

    private long sleepPerRepeat;

    private long currentRepeats = 0;

    private boolean countRepeats = true;

    private boolean deleted = false;

    public TaskQueueEntry(Runnable prepared, Runnable run, long repeats, long sleepPerRepeat) {
        Require.isTrue(repeats > -2, "Cannot use repeat lower than -1");
        Require.isTrue(sleepPerRepeat > 0, "Cannot sleep lower than 1 millisecond");
        this.prepared = prepared;
        this.run = run;
        this.repeats = repeats;
        this.sleepPerRepeat = sleepPerRepeat;
    }

    public TaskQueueEntry(Runnable prepared, Runnable run, long sleepPerRepeat) {
        Require.isTrue(sleepPerRepeat > 0, "Cannot sleep lower than 1 millisecond");
        this.prepared = prepared;
        this.run = run;
        this.repeats = -1;
        this.sleepPerRepeat = sleepPerRepeat;
    }

    public void execute() {
        if (this.repeats == -1) {
            this.currentRepeats = -1;
            this.countRepeats = false;
        }

        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
            while (!Thread.currentThread().isInterrupted() && (currentRepeats < repeats
                || repeats == -1) && !deleted) {
                callEvent(new TaskPreTickEvent(this, currentRepeats));

                prepared.run();
                if (countRepeats) {
                    callEvent(new TaskRepeatCountEvent(this, currentRepeats));
                    currentRepeats++;
                    callEvent(new TaskRepeatCountedEvent(this, currentRepeats));
                }

                callEvent(new TaskTickedEvent(this, currentRepeats));
            }

            deleted = true;
        });
    }

    public Runnable getPrepared() {
        return prepared;
    }

    public Runnable getRun() {
        return run;
    }

    public long getRepeats() {
        return repeats;
    }

    public long getSleepPerRepeat() {
        return sleepPerRepeat;
    }

    public void delete() {
        Require.isFalse(deleted, "Cannot delete task again");
        callEvent(new TaskDeletedEvent(this, currentRepeats));
        this.deleted = true;
    }

    @Internal
    private void callEvent(Event event) {
        ReformCloudLibraryServiceProvider.getInstance().getEventManager().fire(event);
    }
}
