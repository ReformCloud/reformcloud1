/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.logging.enums;

import lombok.Getter;
import org.fusesource.jansi.Ansi;

/**
 * @author _Klaro | Pasqual K. / created on 19.10.2018
 */

@Getter
public enum AnsiColourHandler {
    RESET("reset", 'r', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT).boldOff().toString()),
    WHITE("white", 'f', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString()),
    BLACK("black", '0', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString()),
    RED("red", 'c', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString()),
    YELLOW("yellow", 'e', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString()),
    BLUE("blue", '9', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString()),
    GREEN("green", 'a', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString()),
    PURPLE("purple", '5', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString()),
    ORANGE("orange", '6', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString()),
    GRAY("gray", '7', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString()),
    DARK_RED("dark_red", '4', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString()),
    DARK_GRAY("dark_gray", '8', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString()),
    DARK_BLUE("dark_blue", '1', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString()),
    DARK_GREEN("dark_green", '2', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString()),
    LIGHT_BLUE("light_blue", 'b', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString()),
    CYAN("cyan", '3', Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());

    private String ansiCode;
    private final String name;
    private final char index;

    AnsiColourHandler(String name, char index, String ansiCode) {
        this.name = name;
        this.index = index;
        this.ansiCode = ansiCode;
    }

    public static String stripColor(String input) {
        return input.replaceAll("\u001B\\[[;\\d]*m", "").replace("§", "");
    }

    public static String stripColourCodes(String input) {
        if (input == null)
            throw new IllegalStateException("text");

        for (AnsiColourHandler consoleColour : values())
            input = input.replace('§' + "" + consoleColour.index, "");

        return input;
    }

    public static String toColouredString(String text) {
        if (text == null)
            throw new IllegalStateException("text");

        for (AnsiColourHandler consoleColour : values())
            text = text.replace('§' + "" + consoleColour.index, consoleColour.ansiCode);

        return text;
    }
}
