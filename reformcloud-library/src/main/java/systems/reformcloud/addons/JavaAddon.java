/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons;

import lombok.Data;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.configuration.AddonClassConfig;
import systems.reformcloud.addons.loader.AddonMainClassLoader;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

@Data
public abstract class JavaAddon<E> {
    /**
     * The default instance of the implementer
     *
     * @return The class instance of the implementer set by himself
     */
    public abstract E getInternalReformCloudSystem();

    /**
     * Gets the internal library service
     *
     * @return The library service instance
     */
    public abstract ReformCloudLibraryServiceProvider getInternalCloudLibraryService();

    /**
     * The config of the addon containing the name, version and main class
     */
    private AddonClassConfig addonClassConfig;

    /**
     * The class loader of the addon
     */
    private AddonMainClassLoader addonMainClassLoader;

    /**
     * This method will be called when loading the addon
     */
    public void onAddonClazzPrepare() {
    }

    /**
     * This method will be called while enabling the addon
     */
    public void onAddonLoading() {
    }

    /**
     * This method will be called when disabling the module
     */
    public void onAddonReadyToClose() {
    }

    /**
     * Gets the name of the current addon
     *
     * @return The name of the addon, defined in the config or a random name starting with {@code Addon-}
     */
    public String getAddonName() {
        return addonClassConfig != null ? addonClassConfig.getName() : "Addon-" + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong();
    }
}
