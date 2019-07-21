/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import systems.reformcloud.ReformCloudLibraryService;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

public final class TaskScheduler extends AbstractTaskScheduler implements Serializable {

    /**
     * The default scheduler initialized by the cloud system
     */
    public static final TaskScheduler TASK_SCHEDULER = new TaskScheduler();

    private final Queue<TaskQueueEntry> TASKS = new ConcurrentLinkedDeque<>();

    public TaskScheduler() {
        Runtime.getRuntime()
            .addShutdownHook(new Thread(() -> TASKS.forEach(TaskQueueEntry::delete)));
    }

    @Override
    public AbstractTaskScheduler schedule(Runnable runnable, long repeats, long timePerRepeat) {
        Runnable prepared = () -> {
            runnable.run();
            ReformCloudLibraryService.sleep(timePerRepeat);
        };
        TaskQueueEntry taskQueueEntry = new TaskQueueEntry(prepared, runnable, repeats,
            timePerRepeat);
        TASKS.offer(taskQueueEntry);
        taskQueueEntry.execute();
        return this;
    }

    @Override
    public void close() {
        this.TASKS.forEach(TaskQueueEntry::delete);
    }
}
