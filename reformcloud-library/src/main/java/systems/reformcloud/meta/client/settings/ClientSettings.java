/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.client.settings;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.03.2019
 */

public enum ClientSettings implements Serializable {
    /**
     * The start host setting
     */
    START_HOST("general.start-host"),

    /**
     * The max memory of the client setting
     */
    MEMORY("general.memory"),

    /**
     * The max cpu usage while starting up processes
     */
    MAX_CPU_USAGE("general.maxcpuusage"),

    /**
     * The max log size of the client
     */
    MAX_LOG_SIZE("general.max-log-size");

    /**
     * The config string of the client setting
     */
    private String configString;

    /**
     * Creates a new client setting
     *
     * @param configString The config string of the setting
     */
    ClientSettings(String configString) {
        this.configString = configString;
    }

    /**
     * Get a setting by the name
     *
     * @param in The name of the setting
     * @return The setting found by the name
     */
    public static ClientSettings getSettingByName(String in) {
        ClientSettings clientSettings;
        try {
            clientSettings = valueOf(in.toUpperCase());
        } catch (final Throwable throwable) {
            return null;
        }

        return clientSettings;
    }

    /**
     * @return the config string of the client setting
     */
    public String getConfigString() {
        return this.configString;
    }
}
