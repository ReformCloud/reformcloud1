/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.completer;

import systems.reformcloud.commands.abstracts.AbstractCommandCompleter;
import systems.reformcloud.commands.abstracts.CommandMap;
import systems.reformcloud.commands.utility.Command;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public final class DefaultCommandCompleter extends AbstractCommandCompleter implements Serializable {

    @ConstructorProperties({"commandMap"})
    public DefaultCommandCompleter(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    private CommandMap commandMap;

    @Override
    public int calculateCursorPosition(String buffer, int cursor) {
        int lastSpace = buffer.lastIndexOf(' ');
        return (lastSpace == -1) ? cursor - buffer.length() :
            cursor - (buffer.length() - lastSpace - 1);
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        String[] args = calculateArgs(buffer);
        List<String> out = new ArrayList<>();
        if (buffer.isEmpty() || buffer.indexOf(' ') == -1) {
            out.addAll(commandMap.findAll(buffer));
        } else {
            Command command = commandMap.fromFirstArgument(buffer);
            if (command != null) {
                List<String> returned = ((TabCompleter) command)
                    .complete(buffer, args);
                if (returned != null) {
                    String test = args[args.length - 1];
                    for (String suggestion : returned) {
                        if (test == null || test.isEmpty() || suggestion.toLowerCase().startsWith(test.toLowerCase())) {
                            out.add(suggestion);
                        }
                    }

                    Collections.sort(out);
                    candidates.addAll(out);
                }
            }

            int lastSpace = buffer.lastIndexOf(32);
            return lastSpace == -1 ? cursor - buffer.length() :
                cursor - (buffer.length() - lastSpace - 1);
        }

        Collections.sort(out);
        candidates.addAll(out);
        return calculateCursorPosition(buffer, cursor);
    }

    private String[] calculateArgs(String commandLine) {
        String[] out = commandLine.split(" ");
        if (out.length != 0) {
            out = Arrays.copyOfRange(out, 1, out.length);
        }

        if (commandLine.endsWith(" ")) {
            out = out.length == 0 ? new String[]{""} : Arrays.copyOf(out,
                out.length + 1);
            if (out.length != 0) {
                out[out.length - 1] = "";
            }
        }

        return out;
    }
}
