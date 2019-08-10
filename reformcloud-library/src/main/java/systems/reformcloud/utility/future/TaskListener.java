/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.future;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.08.2019
 */

public interface TaskListener<T> extends Serializable {

    TaskListener FIRE_EXCEPTION_ON_FAILURE = new TaskListener() {
        @Override
        public void onOperationFailed(Throwable cause) {
            cause.printStackTrace();
        }
    };

    default void onOperationComplete(T result) {

    }

    default void onTimeOut() {

    }

    default void onOperationFailed(Throwable cause) {

    }
}
