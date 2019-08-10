/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.future;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public final class DefaultTask<T> extends Task<T> implements Serializable {

    private final List<TaskListener<T>> taskListeners = new LinkedList<>();

    @Override
    public Task<T> addListener(TaskListener<T> taskListener) {
        taskListeners.add(taskListener);
        return this;
    }

    @Override
    public void getUninterruptedly() {
        try {
            T result = get();
            onSuccess(result);
        } catch (final InterruptedException | ExecutionException ex) {
            onFailure(ex);
        }
    }

    @Override
    public void getUninterruptedly(TimeUnit timeUnit, long timeout) {
        try {
            T t = get(timeout, timeUnit);
            onSuccess(t);
        } catch (final InterruptedException | ExecutionException ex) {
            onFailure(ex);
        } catch (final TimeoutException ex) {
            onTimeOut();
        }
    }

    private void onSuccess(T result) {
        taskListeners.forEach(e -> e.onOperationComplete(result));
    }

    private void onTimeOut() {
        taskListeners.forEach(TaskListener::onTimeOut);
    }

    private void onFailure(Throwable cause) {
        taskListeners.forEach(e -> e.onOperationFailed(cause));
    }
}
