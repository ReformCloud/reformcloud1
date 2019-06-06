/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands;

import java.io.Serializable;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.utility.StringUtil;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class CommandVersion extends Command implements Serializable {

    public CommandVersion() {
        super("version", "Get the cloud version", null, new String[]{"ver"});
    }

    @Override
    public void executeCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMessage("ReformCloud V" + StringUtil.REFORM_VERSION);
    }
}
