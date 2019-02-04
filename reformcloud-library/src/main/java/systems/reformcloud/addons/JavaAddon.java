/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.configuration.AddonClassConfig;
import systems.reformcloud.addons.loader.AddonMainClassLoader;
import lombok.Data;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

@Data
public abstract class JavaAddon<E> {
    public abstract E getInternalReformCloudSystem();

    public abstract ReformCloudLibraryServiceProvider getInternalCloudLibraryService();

    private AddonClassConfig addonClassConfig;
    private AddonMainClassLoader addonMainClassLoader;

    public void onAddonClazzPrepare() {
    }

    public void onAddonLoading() {
    }

    public void onAddonReadyToClose() {
    }

    public String getAddonName() {
        return addonClassConfig != null ? addonClassConfig.getName() : "Addon-" + ReformCloudLibraryService.THREAD_LOCAL_RANDOM.nextLong();
    }
}
