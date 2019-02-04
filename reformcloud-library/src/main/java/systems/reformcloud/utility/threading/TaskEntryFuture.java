/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.threading;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
@AllArgsConstructor
public class TaskEntryFuture<T> implements Future<T> {
    private TaskEntry<T> entry;
    protected volatile boolean waits;

    @Override
    public boolean cancel(boolean pMayInterruptIfRunning) {
        if (pMayInterruptIfRunning) {
            entry.task = null;
            entry.repeat = 0;
        }
        return true;
    }

    @Override
    public boolean isCancelled() {
        return entry.task == null;
    }

    @Override
    public boolean isDone() {
        return entry.completed;
    }

    @Override
    public synchronized T get() throws InterruptedException {
        waits = true;
        while (!isDone()) this.wait();

        return entry.value;
    }

    @Override
    public synchronized T get(long pTimeout, @NonNull TimeUnit pUnit) throws InterruptedException {
        waits = true;
        while (!isDone()) this.wait(pUnit.toMillis(pTimeout));

        return entry.value;
    }
}
