/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.event.utility.Listener;

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

    public void registerCommand(final Command command) {
        ReformCloudClient.getInstance().getCommandManager().registerCommand(command);
    }

    public void registerListener(final Listener listener) {
        ReformCloudClient.getInstance().getEventManager().registerListener(listener);
    }

    public void registerListener(final Listener... listeners) {
        Arrays.stream(listeners)
            .forEach(e -> ReformCloudClient.getInstance().getEventManager().registerListener(e));
    }
}
