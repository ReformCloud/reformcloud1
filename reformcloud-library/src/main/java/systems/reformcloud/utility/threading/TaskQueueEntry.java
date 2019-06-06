/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.utility.Require;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

final class TaskQueueEntry implements Serializable {

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
                prepared.run();
                if (countRepeats) {
                    currentRepeats++;
                }
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
        if (deleted) {
            return;
        }

        this.deleted = true;
    }
}
