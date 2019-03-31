/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 31.03.2019
 */

public final class Require implements Serializable {
    public static <T> T requireNotNull(T t) {
        return Objects.requireNonNull(t);
    }

    public static <T> T requireNotNullOrThrow(T t, Throwable exception) {
        if (t == null)
            throw new IllegalStateException(exception);

        return t;
    }
}
