/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading.scheduler;

import systems.reformcloud.utility.threading.TaskScheduler;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public class ScheduledTaskAsync extends ScheduledTask {
    protected Scheduler scheduler;

    public ScheduledTaskAsync(long taskId, Runnable runnable, int delay, int repeatDelay, Scheduler scheduler) {
        super(taskId, runnable, delay, repeatDelay);
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        if (interrupted) return;

        if (delay != 0 && delayTime != 0) {
            delayTime--;
            return;
        }

        if (repeatTime > 0) {
            repeatTime--;
        } else {
            TaskScheduler.runtimeScheduler().schedule(runnable);
            if (repeatTime == -1) {
                cancel();
                return;
            }
            repeatTime = repeatDelay;
        }
    }

    @Override
    protected boolean isAsync() {
        return true;
    }
}
