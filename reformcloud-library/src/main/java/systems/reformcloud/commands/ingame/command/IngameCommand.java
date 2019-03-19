/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.ingame.command;

import lombok.Getter;
import systems.reformcloud.commands.ingame.sender.IngameCommandSender;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 19.03.2019
 */

@Getter
public abstract class IngameCommand implements Serializable {
    private String name;
    private String permission;
    private String[] aliases;

    public IngameCommand(String name) {
        this.name = name;
        this.aliases = new String[0];
    }

    public IngameCommand(String name, String permission, String[] aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public abstract void handle(IngameCommandSender commandSender, String[] args);
}
