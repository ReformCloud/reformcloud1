/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map.pool;

import jline.internal.Nullable;

/**
 * @author _Klaro | Pasqual K. / created on 20.04.2019
 */

public interface FailureCallback<T> {
    void onFailure(T t, @Nullable Throwable cause);
}
