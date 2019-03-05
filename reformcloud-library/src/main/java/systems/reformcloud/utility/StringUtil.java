/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.logging.LoggerProvider;

/**
 * @author _Klaro | Pasqual K. / created on 31.10.2018
 */

public final class StringUtil {
    public static final String JAVA = "java",
            JAVA_JAR = "-jar",
            EMPTY = "",
            SPACE = " ",
            SLASH = "/",
            BACK_SLASH = "\\",
            BUNGEE_API_DOWNLOAD = "3435f392RXKaSgB",
            SPIGOT_API_DOWNLOAD = "F52LL34cECayQrJ",
            NULL = "null",
            OS_NAME = System.getProperty("os.name"),
            OS_ARCH = System.getProperty("os.arch"),
            OS_VERSION = System.getProperty("os.version"),
            USER_NAME = System.getProperty("user.name"),
            JAVA_VERSION = System.getProperty("java.version"),
            REFORM_SPECIFICATION = StringUtil.class.getPackage().getSpecificationVersion(),
            REFORM_VERSION = StringUtil.class.getPackage().getImplementationVersion(),
            LOGGER_INFO = "[§a%date%§r] ",
            LOGGER_WARN = "[§e%date%§r] ",
            LOGGER_ERR = "[§c%date%§r] ";

    private static final String[] unusedErrorComment = new String[] {
            "Who set up this error?", "Oh that was the idea", "This should happen?", "Yes", "I feel sad now :(", "It was my fault :(", "I\'m sorry _Klaro",
            "I will bring you a poster", "Thank you :)", "Surprise!, Haha just a tiny joke", "What?", "I\'m crashaholic", "Oof", "He. Stop!", "Ouch that hurts",
            "No problem my friend", "That was my present for you", "I\'m gonna fix this error", "Ok bro, do that"
    };

    public static void printError(final LoggerProvider loggerProvider, final String whoIAm, final Throwable cause) {
        try {
            for (String s : StringUtil.unusedErrorComment)
                loggerProvider.serve(s);
        } catch (final Throwable ignored) {
            loggerProvider.serve("No comment :(");
        }

        loggerProvider.emptyLine().serve("---------------------------------------------------");
        loggerProvider.emptyLine().serve(whoIAm);
        loggerProvider.serve("If you have an addon which changes something and you see it in the Exception dump below, please report it to the author");
        loggerProvider.serve("If you are unsure or still think that this is a ReformCloud Bug, please visit and report to https://discord.gg/uskXdVZ");
        loggerProvider.serve("Be sure to include ALL relevant console errors and Log files");
        loggerProvider.serve("Reform Version: " + REFORM_VERSION);
        loggerProvider.serve("Reform Specification: " + REFORM_SPECIFICATION);
        loggerProvider.emptyLine();
        loggerProvider.exception(cause);
        loggerProvider.emptyLine().serve("---------------------------------------------------");
    }
}
