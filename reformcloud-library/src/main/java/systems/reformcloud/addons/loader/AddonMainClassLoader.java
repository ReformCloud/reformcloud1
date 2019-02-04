/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.loader;

import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.addons.configuration.AddonClassConfig;
import systems.reformcloud.addons.extendable.ModulePreLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public class AddonMainClassLoader extends URLClassLoader implements ModulePreLoader {
    private final AddonClassConfig addonClassConfig;

    /**
     * Loads the Addon Main Class
     *
     * @param addonClassConfig
     * @throws MalformedURLException
     */
    public AddonMainClassLoader(final AddonClassConfig addonClassConfig) throws MalformedURLException {
        super(new URL[]{addonClassConfig.getFile().toURI().toURL()});
        this.addonClassConfig = addonClassConfig;
    }

    /**
     * Loads the Addon by the defined {@link AddonClassConfig}
     *
     * @return
     * @throws Throwable
     * @see AddonMainClassLoader#AddonMainClassLoader(AddonClassConfig)
     */
    @Override
    public JavaAddon loadAddon() throws Throwable {
        JavaAddon javaAddon = (JavaAddon) loadClass(addonClassConfig.getMain()).getDeclaredConstructor().newInstance();

        javaAddon.setAddonClassConfig(this.addonClassConfig);
        javaAddon.onAddonClazzPrepare();

        return javaAddon;
    }
}
