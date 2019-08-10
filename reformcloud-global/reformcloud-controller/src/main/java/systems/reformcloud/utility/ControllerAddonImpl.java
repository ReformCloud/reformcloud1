/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.addons.dependency.DependencyLoader;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.event.utility.Listener;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

public class ControllerAddonImpl extends JavaAddon<ReformCloudController> implements Serializable {

    @Override
    public ReformCloudController getInternalReformCloudSystem() {
        return ReformCloudController.getInstance();
    }

    @Override
    public ReformCloudLibraryServiceProvider getInternalCloudLibraryService() {
        return ReformCloudLibraryServiceProvider.getInstance();
    }

    @Override
    public Task<Void> loadDependencyAsync(DynamicDependency dynamicDependency) {
        DefaultTask<Void> defaultTask = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            DependencyLoader.loadDependency(dynamicDependency);
            defaultTask.complete(null);
        });
        return defaultTask;
    }

    public void registerCommand(final Command command) {
        ReformCloudController.getInstance().getCommandManager().registerCommand(command);
    }

    public void registerListener(final Listener listener) {
        ReformCloudController.getInstance().getEventManager().registerListener(listener);
    }

    public void registerListener(final Listener... listeners) {
        Arrays.stream(listeners).forEach(
            e -> ReformCloudController.getInstance().getEventManager().registerListener(e));
    }
}
