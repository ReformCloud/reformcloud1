/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.commands.interfaces.CommandSender;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class CommandVersion extends Command implements Serializable {
    public CommandVersion() {
        super("version", "Get the cloud version", null, new String[]{"ver"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("ReformCloud V" + StringUtil.REFORM_VERSION + "@" + StringUtil.REFORM_SPECIFICATION);
    }
}
