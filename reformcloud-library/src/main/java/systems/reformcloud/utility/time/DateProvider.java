/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class DateProvider {
    /**
     * Get a defaultDateFormat
     *
     * @return a new {@link SimpleDateFormat}
     */
    private static DateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    }

    /**
     * Get a defaultDateFormat with custom pattern
     *
     * @param pattern
     * @return a new {@link SimpleDateFormat} with the given Pattern
     * @see SimpleDateFormat#applyPattern(String)
     */
    public static DateFormat getDateFormat(final String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * Gets a formatted Date by the default DateFormat
     *
     * @param current
     * @return {@link String} with formatted DateFormat
     * @see DateProvider#getDefaultDateFormat()
     */
    public static String formatByDefaultFormat(final long current) {
        return getDefaultDateFormat().format(current);
    }

    /**
     * Gets a formatted Date by custom DateFormat
     *
     * @param pattern
     * @param current
     * @return {@link String} with formatted DateFormat
     * @see DateProvider#getDateFormat(String)
     */
    public static String formatByDefaultFormat(final String pattern, final long current) {
        return getDateFormat(pattern).format(current);
    }

    /**
     * Throws a new RuntimeException
     */
    public static void throwDateException() {
        throw new RuntimeException("DateProvider Exception");
    }
}
