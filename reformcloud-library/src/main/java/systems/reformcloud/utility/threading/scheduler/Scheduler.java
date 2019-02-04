/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading.scheduler;

import systems.reformcloud.ReformCloudLibraryService;
import lombok.Getter;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

public final class Scheduler implements Runnable {
    private ConcurrentHashMap<Long, ScheduledTask> tasks = ReformCloudLibraryService.concurrentHashMap();

    @Getter
    private final int ticks;
    @Getter
    private final Random random = new Random();

    public Scheduler(int ticks) {
        this.ticks = ticks;
    }

    public ScheduledTask runTaskSync(Runnable runnable) {
        return runTaskRepeatSync(runnable, 0, -1);
    }

    public ScheduledTask runTaskDelayedSync(Runnable runnable, int delayTicks) {
        return runTaskRepeatSync(runnable, delayTicks, -1);
    }

    public ScheduledTask runTaskAsync(Runnable runnable) {
        return runTaskRepeatAsync(runnable, 0, -1);
    }

    public ScheduledTask runTaskDelayedAsync(Runnable runnable, int delay) {
        return runTaskRepeatAsync(runnable, delay, -1);
    }

    public ScheduledTask runTaskRepeatSync(Runnable runnable, int delayTicks, int repeatDelay) {
        long id = random.nextLong();
        ScheduledTask task = new ScheduledTask(id, runnable, delayTicks, repeatDelay);
        this.tasks.put(id, task);
        return task;
    }

    public ScheduledTask runTaskRepeatAsync(Runnable runnable, int delay, int repeat) {
        long id = random.nextLong();
        ScheduledTask task = new ScheduledTaskAsync(id, runnable, delay, repeat, this);
        this.tasks.put(id, task);
        return task;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ReformCloudLibraryService.sleep(Thread.currentThread(), 1000 / ticks);

            if (tasks.isEmpty()) continue;

            ConcurrentHashMap<Long, ScheduledTask> tasks = this.tasks;

            for (ScheduledTask task : tasks.values()) {

                if (task.isInterrupted()) {
                    this.tasks.remove(task.getTaskId());
                    continue;
                }

                task.run();
            }
        }
    }
}
