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
                loggerProvider.err(s);
        } catch (final Throwable ignored) {
            loggerProvider.err("No comment :(");
        }

        loggerProvider.emptyLine().err("---------------------------------------------------");
        loggerProvider.emptyLine().err(whoIAm);
        loggerProvider.err("If you have an addon which changes something and you see it in the Exception dump below, please report it to the author");
        loggerProvider.err("If you are unsure or still think that this is a ReformCloud Bug, please visit and report to https://discord.gg/uskXdVZ");
        loggerProvider.err("Be sure to include ALL relevant console errors and Log files");
        loggerProvider.err("Reform Version: " + REFORM_VERSION);
        loggerProvider.err("Reform Specification: " + REFORM_SPECIFICATION);
        loggerProvider.emptyLine();
        loggerProvider.exception(cause);
        loggerProvider.emptyLine().err("---------------------------------------------------");
    }
}
