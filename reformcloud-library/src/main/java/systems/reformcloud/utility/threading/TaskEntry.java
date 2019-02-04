/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
public class TaskEntry<T> {
    protected volatile Callable<T> task;
    protected volatile T value = null;
    protected Consumer<T> consumer;
    protected long delayTimeOut, repeat, delay;
    protected boolean completed = false;
    private final TaskEntryFuture<T> future;

    public TaskEntry(Callable<T> task, Consumer<T> consumer, long delay, long repeat) {
        this.task = task;
        this.consumer = consumer;
        this.delay = delay;
        this.delayTimeOut = System.currentTimeMillis() + delay;
        this.repeat = repeat;
        this.future = new TaskEntryFuture<>(this, false);
    }

    protected void invoke() throws Throwable {
        if (task == null)
            return;

        T val = task.call();

        value = val;

        if (consumer != null)
            consumer.accept(val);

        if (repeat != -1 && repeat != 0) repeat--;

        if (repeat != 0)
            this.delayTimeOut = System.currentTimeMillis() + delay;
        else {
            completed = true;

            if (future.waits) {
                synchronized (future) {
                    future.notifyAll();
                }
            }
        }
    }
}
