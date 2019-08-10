/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.addons.dependency.DependencyLoader;
import systems.reformcloud.addons.dependency.util.DynamicDependency;
import systems.reformcloud.commands.utility.Command;
import systems.reformcloud.event.utility.Listener;
import systems.reformcloud.utility.future.DefaultTask;
import systems.reformcloud.utility.future.Task;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

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

    @Override
    public Task<Void> loadDependencyAsync(DynamicDependency dynamicDependency) {
        Task<Void> task = new DefaultTask<>();
        CompletableFuture.runAsync(() -> {
            DependencyLoader.loadDependency(dynamicDependency);
            task.complete(null);
        });
        return task;
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
