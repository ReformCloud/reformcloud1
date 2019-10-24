/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.loader;

import systems.reformcloud.addons.JavaAddon;
import systems.reformcloud.addons.configuration.AddonClassConfig;
import systems.reformcloud.addons.extendable.AddonPreLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public class AddonMainClassLoader extends URLClassLoader implements AddonPreLoader {

    /**
     * The class config of the addon
     */
    private final AddonClassConfig addonClassConfig;

    /**
     * Loads the Addon Main Class
     *
     * @param addonClassConfig The class config of the addon
     * @throws MalformedURLException The exception will be thrown if the config cannot be found
     */
    public AddonMainClassLoader(final AddonClassConfig addonClassConfig)
        throws MalformedURLException {
        super(new URL[]{addonClassConfig.getFile().toURI().toURL()});
        this.addonClassConfig = addonClassConfig;
    }

    @Override
    public JavaAddon loadAddon() throws Throwable {
        JavaAddon javaAddon = (JavaAddon) loadClass(addonClassConfig.getMain())
            .getDeclaredConstructor().newInstance();

        javaAddon.setAddonClassConfig(this.addonClassConfig);
        return javaAddon;
    }
}
