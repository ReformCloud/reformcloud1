/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.future;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public abstract class Task<T> extends CompletableFuture<T> implements Serializable {

    public final TaskListener<T> fireExceptionOnFailure = new TaskListener<T>() {
        @Override
        public void onOperationFailed(Throwable cause) {
            StringUtil.printError(
                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider(),
                "Error while executing task",
                cause
            );
        }
    };

    public abstract Task<T> addListener(TaskListener<T> taskListener);

    public abstract void getUninterruptedly();

    public abstract void getUninterruptedly(TimeUnit timeUnit, long timeout);
}
