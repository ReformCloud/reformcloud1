/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.event.Listener;

import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 27.12.2018
 */

public class ClientAddonImpl extends JavaAddon<ReformCloudClient> {
    @Override
    public ReformCloudClient getInternalReformCloudSystem() {
        return ReformCloudClient.getInstance();
    }

    @Override
    public ReformCloudLibraryServiceProvider getInternalCloudLibraryService() {
        return ReformCloudLibraryServiceProvider.getInstance();
    }

    public void registerCommand(final String name, final Command command) {
        ReformCloudClient.getInstance().getCommandManager().registerCommand(name, command);
    }

    public void registerCommand(final String name, final Command command, final String... aliases) {
        ReformCloudClient.getInstance().getCommandManager().registerCommand(name, command, aliases);
    }

    public void registerListener(final Listener listener) {
        ReformCloudClient.getInstance().getEventManager().registerListener(listener);
    }

    public void registerListener(final Listener... listeners) {
        Arrays.stream(listeners).forEach(e -> ReformCloudClient.getInstance().getEventManager().registerListener(e));
    }
}
