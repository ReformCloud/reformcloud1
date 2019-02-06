/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.interfaces;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _Klaro | Pasqual K. / created on 18.10.2018
 */

@AllArgsConstructor
@Getter
public abstract class Command {
    private String name, description, permission;
    private String[] aliases;

    public abstract void executeCommand(CommandSender commandSender, String[] args);
}
