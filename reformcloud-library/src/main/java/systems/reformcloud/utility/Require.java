/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 31.03.2019
 */

public final class Require implements Serializable {
    public static <T> T requireNotNull(T t) {
        return requireNotNullOrThrow(t, new IllegalStateException("Object requires to be not null"));
    }

    public static <T> T requireNotNullOrThrow(T t, Throwable exception) {
        if (t == null)
            throw new IllegalStateException(exception);

        return t;
    }

    public static <T> T requireNotNull(T t, String message) {
        return requireNotNullOrThrow(t, new IllegalStateException(message));
    }

    public static boolean isTrue(boolean check, String message) {
        if (!check)
            throw new IllegalStateException(message);

        return true;
    }

    public static boolean isFalse(boolean check, String message) {
        if (check)
            throw new IllegalStateException(message);

        return true;
    }
}
