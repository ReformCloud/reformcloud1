/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api.ingame.command;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.ingame.IngameCommandManger;
import systems.reformcloud.commands.ingame.command.IngameCommand;
import systems.reformcloud.network.out.PacketOutUpdateIngameCommands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 19.03.2019
 */

public final class IngameCommandMangerImpl extends IngameCommandManger implements Serializable {

    private Map<String, IngameCommand> registeredIngameCommands = new HashMap<>();

    public IngameCommandMangerImpl() {
        IngameCommandManger.instance.set(this);
    }

    public List<IngameCommand> getAllRegisteredCommands() {
        return new ArrayList<>(this.registeredIngameCommands.values());
    }

    public void registerCommand(IngameCommand ingameCommand) {
        this.registeredIngameCommands.replace(ingameCommand.getName(), ingameCommand);

        ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .forEach(
                e -> ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    e.getCloudProcess().getName(),
                    new PacketOutUpdateIngameCommands()
                ));
    }

    public void unregisterCommand(IngameCommand ingameCommand) {
        this.registeredIngameCommands.remove(ingameCommand.getName());

        ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .forEach(
                e -> ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    e.getCloudProcess().getName(),
                    new PacketOutUpdateIngameCommands()
                ));
    }

    public void unregisterCommand(String name) {
        this.registeredIngameCommands.remove(name);

        ReformCloudController.getInstance()
            .getInternalCloudNetwork()
            .getServerProcessManager()
            .getAllRegisteredProxyProcesses()
            .forEach(
                e -> ReformCloudController.getInstance().getChannelHandler().sendPacketSynchronized(
                    e.getCloudProcess().getName(),
                    new PacketOutUpdateIngameCommands()
                ));
    }

    public IngameCommand getCommand(String name) {
        return this.registeredIngameCommands.getOrDefault(name, null);
    }
}
