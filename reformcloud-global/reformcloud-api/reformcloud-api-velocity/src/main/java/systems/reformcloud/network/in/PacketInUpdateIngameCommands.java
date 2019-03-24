/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 19.03.2019
 */

public final class PacketInUpdateIngameCommands implements Serializable, NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        List<IngameCommand> ingameCommands = configuration.getValue("commands", new TypeToken<List<IngameCommand>>() {
        }.getType());
        ReformCloudAPIVelocity.getInstance().updateIngameCommands(ingameCommands);
    }
}
