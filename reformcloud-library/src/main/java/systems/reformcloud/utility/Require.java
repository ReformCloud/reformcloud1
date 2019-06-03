/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 31.03.2019
 */

public final class Require implements Serializable {

    /**
     * Checks if the given parameter is non null
     *
     * @param t The given parameter which should be checked
     * @param <T> The type of the given parameter
     */
    public static <T> void requireNotNull(T t) {
        requireNotNullOrThrow(t, new IllegalStateException("Object requires to be not null"));
    }

    /**
     * Checks if the given parameter is non null
     *
     * @param t The given parameter which should be checked
     * @param exception The exception which should be thrown when the parameter is null
     * @param <T> The type of the given parameter
     * @return The parameter or throws an exception when the parameter is null
     */
    private static <T> T requireNotNullOrThrow(T t, Throwable exception) {
        if (t == null) {
            throw new IllegalStateException(exception);
        }

        return t;
    }

    /**
     * Checks if the given parameter is non null
     *
     * @param t The given parameter which should be checked
     * @param message The message which should be thrown in the exception when the parameter is null
     * @param <T> The type of the given parameter
     * @return The parameter or throws an exception when the parameter is null
     */
    public static <T> T requireNotNull(T t, String message) {
        return requireNotNullOrThrow(t, new IllegalStateException(message));
    }

    /**
     * Checks if the given parameters are non null
     *
     * @param t The given parameters which should be checked
     * @param <T> The type of the given parameters
     */
    @SafeVarargs
    public static <T> void requiresNotNull(T... t) {
        for (T check : t) {
            if (check == null) {
                throw new IllegalStateException();
            }
        }
    }

    /**
     * Checks if the argument is true
     *
     * @param check The argument which should be checked
     * @param message The message which should be display in the exception when the argument is non
     * null
     * @return If the argument is true
     */
    public static boolean isTrue(boolean check, String message) {
        if (!check) {
            throw new IllegalStateException(message);
        }

        return true;
    }

    /**
     * Checks if the argument is false
     *
     * @param check The argument which should be checked
     * @param message The message which should be display in the exception when the argument is non
     * null
     * @return If the argument is false
     */
    public static boolean isFalse(boolean check, String message) {
        if (check) {
            throw new IllegalStateException(message);
        }

        return true;
    }
}
