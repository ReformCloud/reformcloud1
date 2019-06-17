/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map.pool;

import systems.reformcloud.utility.annotiations.MayNotBePresent;

/**
 * @author _Klaro | Pasqual K. / created on 20.04.2019
 */

public interface FailureCallback<T> extends Callable<Throwable> {

    void onFailure(T t, @MayNotBePresent Throwable cause);

    @Override
    default void call(Throwable throwable) {
        onFailure(null, throwable);
    }
}
