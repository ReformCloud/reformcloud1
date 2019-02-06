/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.commands.interfaces.Command;
import systems.reformcloud.event.Listener;

import java.util.Arrays;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

/**
 * Class for Controller Addons
 */
public class ControllerAddonImpl extends JavaAddon<ReformCloudController> {
    @Override
    public ReformCloudController getInternalReformCloudSystem() {
        return ReformCloudController.getInstance();
    }

    @Override
    public ReformCloudLibraryServiceProvider getInternalCloudLibraryService() {
        return ReformCloudLibraryServiceProvider.getInstance();
    }

    public void registerCommand(final Command command) {
        ReformCloudController.getInstance().getCommandManager().registerCommand(command);
    }

    public void registerListener(final Listener listener) {
        ReformCloudController.getInstance().getEventManager().registerListener(listener);
    }

    public void registerListener(final Listener... listeners) {
        Arrays.stream(listeners).forEach(e -> ReformCloudController.getInstance().getEventManager().registerListener(e));
    }
}
