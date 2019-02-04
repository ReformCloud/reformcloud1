/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading.scheduler;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;
import lombok.Getter;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
public class ScheduledTask implements Runnable {
    protected long taskId;
    protected Runnable runnable;
    protected int delay;
    protected int repeatDelay;
    protected boolean interrupted;

    protected int delayTime;
    protected int repeatTime;

    public ScheduledTask(long taskId, Runnable runnable, int delay, int repeatDelay) {
        this.taskId = taskId;
        this.runnable = runnable;
        this.delay = delay != -1 && delay != 0 ? delay : 0;
        this.repeatDelay = repeatDelay != -1 ? repeatDelay : 0;
        this.interrupted = false;

        this.delayTime = this.delay;
        this.repeatTime = repeatDelay == 0 ? -1 : repeatDelay;
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
            try {
                runnable.run();
            } catch (final Throwable throwable) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error in ScheduledTask", throwable);
            }

            if (repeatTime == -1) {
                cancel();
                return;
            }
            repeatTime = repeatDelay;
        }
    }

    protected boolean isAsync() {
        return false;
    }

    public void cancel() {
        this.interrupted = true;
    }
}
