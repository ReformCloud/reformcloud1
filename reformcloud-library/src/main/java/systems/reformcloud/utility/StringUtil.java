/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.logging.AbstractLoggerProvider;

/**
 * @author _Klaro | Pasqual K. / created on 31.10.2018
 */

public final class StringUtil {

    /**
     * Some strings which are used in the cloud system
     */
    public static final String JAVA = "java",
        JAVA_JAR = "-jar",
        EMPTY = "",
        SPACE = " ",
        SLASH = "/",
        BACK_SLASH = "\\",
    //API Names: Version Specification date time B(ungee) S(igot) V(elocity)
    BUNGEE_API_DOWNLOAD = "113PRESTABLE290719953B",
        SPIGOT_API_DOWNLOAD = "113PRESTABLE290719953S",
        VELOCITY_API_DOWNLOAD = "113PRESTABLE290719953V",
        NULL = "null",
        OS_NAME = System.getProperty("os.name"),
        OS_ARCH = System.getProperty("os.arch"),
        OS_VERSION = System.getProperty("os.version"),
        USER_NAME = System.getProperty("user.name"),
        JAVA_VERSION = System.getProperty("java.version"),
        REFORM_VERSION = System.getProperty("reformcloud.version"),
        LOGGER_INFO = "[§a%date%§r] ",
        LOGGER_WARN = "[§e%date%§r] ",
        LOGGER_ERR = "[§c%date%§r] ";

    /**
     * The error comment displayed when an exception occurs
     */
    private static final String[] unusedErrorComment = new String[]{
        "Who set up this error?", "Oh that was the idea", "This should happen?", "Yes",
        "I feel sad now :(", "It was my fault :(", "I\'m sorry _Klaro",
        "I will bring you a poster", "Thank you :)", "Surprise!, Haha just a tiny joke", "What?",
        "I\'m crashaholic", "Oof", "He. Stop!", "Ouch that hurts",
        "No problem my friend", "That was my present for you", "I\'m gonna fix this error",
        "Ok bro, do that"
    };

    /**
     * Prints an exception to the console
     *
     * @param loggerProvider The logger provider to log the exception
     * @param whoIAm Where the exception occur
     * @param cause The throwable why the error occur
     */
    public static void printError(final AbstractLoggerProvider loggerProvider, final String whoIAm,
        final Throwable cause) {
        try {
            for (String s : StringUtil.unusedErrorComment) {
                loggerProvider.serve(s);
            }
        } catch (final Throwable ignored) {
            loggerProvider.serve("No comment :(");
        }

        loggerProvider.emptyLine().serve("---------------------------------------------------");
        loggerProvider.emptyLine().serve(whoIAm);
        loggerProvider.serve(
            "If you have an addon which changes something and you see it in the Exception dump below, please report it to the author");
        loggerProvider.serve(
            "If you are unsure or still think that this is a ReformCloud Bug, please visit and report to https://discord.gg/uskXdVZ");
        loggerProvider.serve("Be sure to include ALL relevant console errors and Log files");
        loggerProvider.serve("Reform Version: " + REFORM_VERSION);
        loggerProvider.emptyLine();
        loggerProvider.exception(cause);
        loggerProvider.emptyLine().serve("---------------------------------------------------");
    }
}
