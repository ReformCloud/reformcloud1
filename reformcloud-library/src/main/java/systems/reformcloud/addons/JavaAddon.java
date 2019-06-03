/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.configuration.AddonClassConfig;
import systems.reformcloud.addons.loader.AddonMainClassLoader;

import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public abstract class JavaAddon<E> {

    protected JavaAddon() {
    }

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
     * @return The name of the addon, defined in the config or a random name starting with {@code
     * Addon-}
     */
    public String getAddonName() {
        return addonClassConfig != null ? addonClassConfig.getName()
            : "Addon-" + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong();
    }

    public AddonClassConfig getAddonClassConfig() {
        return this.addonClassConfig;
    }

    private AddonMainClassLoader getAddonMainClassLoader() {
        return this.addonMainClassLoader;
    }

    public void setAddonClassConfig(AddonClassConfig addonClassConfig) {
        this.addonClassConfig = addonClassConfig;
    }

    public void setAddonMainClassLoader(AddonMainClassLoader addonMainClassLoader) {
        this.addonMainClassLoader = addonMainClassLoader;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JavaAddon)) {
            return false;
        }
        final JavaAddon<?> other = (JavaAddon<?>) o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$addonClassConfig = this.getAddonClassConfig();
        final Object other$addonClassConfig = other.getAddonClassConfig();
        if (!Objects.equals(this$addonClassConfig, other$addonClassConfig)) {
            return false;
        }
        final Object this$addonMainClassLoader = this.getAddonMainClassLoader();
        final Object other$addonMainClassLoader = other.getAddonMainClassLoader();
        if (!Objects.equals(this$addonMainClassLoader, other$addonMainClassLoader)) {
            return false;
        }
        return true;
    }

    private boolean canEqual(final Object other) {
        return other instanceof JavaAddon;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $addonClassConfig = this.getAddonClassConfig();
        result = result * PRIME + ($addonClassConfig == null ? 43 : $addonClassConfig.hashCode());
        final Object $addonMainClassLoader = this.getAddonMainClassLoader();
        result = result * PRIME + ($addonMainClassLoader == null ? 43
            : $addonMainClassLoader.hashCode());
        return result;
    }

    public String toString() {
        return "JavaAddon(addonClassConfig=" + this.getAddonClassConfig()
            + ", addonMainClassLoader=" + this.getAddonMainClassLoader() + ")";
    }
}
