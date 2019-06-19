/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.completer;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.commands.abstracts.AbstractCommandCompleter;
import systems.reformcloud.commands.abstracts.CommandMap;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.utility.map.MapUtility;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.*;

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
        List<String> out = new ArrayList<>();
        if (buffer.isEmpty() || buffer.indexOf(' ') == -1) {
            List<String> commands = new ArrayList<>();
            commandMap.findAll(buffer).forEach(e -> commands.add(e.getName()));
            out.addAll(commands);
        } else {
            Command command = commandMap.fromFirstArgument(buffer);
            if (command != null) {
                String[] args = replace(buffer);
                List<String> returned = ((TabCompleter) command)
                    .complete(buffer, args);
                Collection<String> completed = MapUtility.filterAll(returned,
                    e -> e.toLowerCase().startsWith(buffer.toLowerCase()));

                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider()
                    .serve().accept(returned.toString());

                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider()
                    .serve().accept("----");

                ReformCloudLibraryServiceProvider.getInstance().getColouredConsoleProvider()
                    .serve().accept(completed.toString());

                out.addAll(completed);
            }
        }

        Collections.sort(out);
        candidates.addAll(out);
        return calculateCursorPosition(buffer, cursor);
    }

    private String[] replace(String buffer) {
        List<String> args = new LinkedList<>(Arrays.asList(buffer.split(" ")));
        args.remove(buffer.split(" ")[0]);
        return args.toArray(new String[0]);
    }
}
