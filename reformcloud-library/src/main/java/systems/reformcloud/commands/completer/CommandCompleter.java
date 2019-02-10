/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.completer;

import jline.console.completer.Completer;
import lombok.AllArgsConstructor;
import systems.reformcloud.commands.CommandManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 28.01.2019
 */

@AllArgsConstructor
public final class CommandCompleter implements Serializable, Completer {
    private static final long serialVersionUID = 8517535904277077116L;
    private CommandManager commandManager;

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        if (buffer.isEmpty() || buffer.indexOf(' ') == -1) {
            String s = buffer.toLowerCase();
            List<String> completer = new ArrayList<>();
            if (!buffer.trim().isEmpty()) {
                commandManager.getCommands().forEach(command -> {
                    Arrays.asList(command.getAliases()).forEach(alias -> {
                        if (alias.toLowerCase().startsWith(s.toLowerCase()))
                            completer.add(alias.toLowerCase());
                    });

                    if (command.getName().toLowerCase().startsWith(s.toLowerCase()))
                        completer.add(s.toLowerCase());
                });
            } else {
                commandManager.getCommands().forEach(command -> {
                    Arrays.asList(command.getAliases()).forEach(alias -> {
                        completer.add(alias.toLowerCase());
                    });

                    completer.add(s.toLowerCase());
                });
            }
            candidates.addAll(completer);
        }

        int lastSpace = buffer.lastIndexOf(' ');
        return (lastSpace == -1) ? cursor - buffer.length() : cursor - (buffer.length() - lastSpace - 1);
    }
}
