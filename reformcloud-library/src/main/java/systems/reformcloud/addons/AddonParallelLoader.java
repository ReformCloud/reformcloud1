/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons;

import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.addons.configuration.AddonClassConfig;
import systems.reformcloud.addons.extendable.AddonExtendable;
import systems.reformcloud.addons.loader.AddonMainClassLoader;
import systems.reformcloud.utility.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

public class AddonParallelLoader extends AddonExtendable {
    private Queue<JavaAddon> javaAddons = new ConcurrentLinkedDeque<>();

    /**
     * Loads all addons
     */
    @Override
    public void loadAddons() {
        Collection<AddonClassConfig> addonClassConfigs = this.checkForAddons();

        addonClassConfigs.forEach(addonClassConfig -> {
            try {
                final AddonMainClassLoader addonMainClassLoader = new AddonMainClassLoader(addonClassConfig);
                JavaAddon javaAddon = addonMainClassLoader.loadAddon();
                javaAddon.setAddonMainClassLoader(addonMainClassLoader);

                javaAddons.add(javaAddon);

                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getAddon_prepared()
                        .replace("%name%", addonClassConfig.getName())
                        .replace("%version%", addonClassConfig.getVersion()));
            } catch (final Throwable throwable) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while loading addon", throwable);
            }
        });
    }

    /**
     * Enables the Addons
     */
    @Override
    public void enableAddons() {
        this.javaAddons.forEach(consumer -> {
            consumer.onAddonLoading();
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getAddon_enabled()
                    .replace("%name%", consumer.getAddonName())
                    .replace("%version%", consumer.getAddonClassConfig().getVersion()));
        });
    }

    /**
     * Disables all addons
     */
    @Override
    public void disableAddons() {
        if (javaAddons.isEmpty())
            return;

        do {
            JavaAddon consumer = javaAddons.poll();
            consumer.onAddonReadyToClose();
            ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getAddon_closed()
                    .replace("%name%", consumer.getAddonName())
                    .replace("%version%", consumer.getAddonClassConfig().getVersion()));
        } while (!javaAddons.isEmpty());
    }

    public boolean disableAddon(final String name) {
        JavaAddon javaAddon = this.javaAddons
                .stream()
                .filter(addon -> addon.getAddonName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        if (javaAddon == null)
            return false;

        javaAddon.onAddonReadyToClose();
        ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getAddon_closed()
                .replace("%name%", javaAddon.getAddonName())
                .replace("%version%", javaAddon.getAddonClassConfig().getVersion()));

        this.javaAddons.remove(javaAddon);
        return true;
    }

    /**
     * This methods searches for a specific addon and enables it
     *
     * @param name The name of the addon that should be found
     * @return If the addon file exists and if the addon could be loaded
     */
    public boolean enableAddon(final String name) {
        Set<AddonClassConfig> moduleConfigs = new HashSet<>();

        File[] files = new File("reformcloud/addons").listFiles(pathname ->
                pathname.isFile()
                        && pathname.exists()
                        && pathname.getName().endsWith(".jar")
                        && pathname.getName().replace(".jar", StringUtil.EMPTY).equalsIgnoreCase(name));
        if (files == null || files.length == 0)
            return false;

        for (File file : files) {
            if (!file.getName().replace(".jar", StringUtil.EMPTY).equalsIgnoreCase(name))
                continue;

            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("addon.properties");
                if (jarEntry == null)
                    throw new IllegalStateException(new FileNotFoundException("Could't find properties file"));

                try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                    Properties properties = new Properties();
                    properties.load(reader);
                    AddonClassConfig moduleConfig = new AddonClassConfig(file,
                            properties.getProperty("name"),
                            properties.getProperty("version"),
                            properties.getProperty("mainClazz"));
                    moduleConfigs.add(moduleConfig);
                }
            } catch (final Throwable throwable) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load addon configuration", throwable);
            }
        }

        moduleConfigs.forEach(addonClassConfig -> {
            try {
                final AddonMainClassLoader addonMainClassLoader = new AddonMainClassLoader(addonClassConfig);
                JavaAddon javaAddon = addonMainClassLoader.loadAddon();
                javaAddon.setAddonMainClassLoader(addonMainClassLoader);

                javaAddons.add(javaAddon);

                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getAddon_prepared()
                        .replace("%name%", addonClassConfig.getName())
                        .replace("%version%", addonClassConfig.getVersion()));

                javaAddon.onAddonLoading();
                ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider().info(ReformCloudLibraryServiceProvider.getInstance().getLoaded().getAddon_enabled()
                        .replace("%name%", javaAddon.getAddonName())
                        .replace("%version%", javaAddon.getAddonClassConfig().getVersion()));
            } catch (final Throwable throwable) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Error while loading addon", throwable);
            }
        });

        return true;
    }

    /**
     * Checks if a specific addon is enabled or not
     *
     * @param name      The name of the addon
     * @return If the addon is enabled or not
     */
    public boolean isAddonEnabled(final String name) {
       return this.javaAddons
               .stream()
               .anyMatch(addon -> addon.getAddonName().equalsIgnoreCase(name));
    }

    /**
     * Returns all Addon Class Configs of all jar files
     *
     * @return a set of all Addons
     */
    private Set<AddonClassConfig> checkForAddons() {
        Set<AddonClassConfig> moduleConfigs = new HashSet<>();

        File[] files = new File("reformcloud/addons").listFiles(pathname ->
                pathname.isFile()
                        && pathname.exists()
                        && pathname.getName().endsWith(".jar"));
        if (files == null)
            return moduleConfigs;

        for (File file : files) {
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("addon.properties");
                if (jarEntry == null)
                    throw new IllegalStateException(new FileNotFoundException("Could't find properties file"));

                try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                    Properties properties = new Properties();
                    properties.load(reader);
                    AddonClassConfig moduleConfig = new AddonClassConfig(file,
                            properties.getProperty("name"),
                            properties.getProperty("version"),
                            properties.getProperty("mainClazz"));
                    moduleConfigs.add(moduleConfig);
                }
            } catch (final Throwable throwable) {
                StringUtil.printError(ReformCloudLibraryServiceProvider.getInstance().getLoggerProvider(), "Could not load addon configuration", throwable);
            }
        }

        return moduleConfigs;
    }

    public Queue<JavaAddon> getJavaAddons() {
        return this.javaAddons;
    }
}
