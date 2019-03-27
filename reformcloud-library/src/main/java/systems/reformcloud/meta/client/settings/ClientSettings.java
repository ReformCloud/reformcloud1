/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.client.settings;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 27.03.2019
 */

@Getter
public enum ClientSettings implements Serializable {
    START_HOST("general.start-host"),
    MEMORY("general.memory"),
    MAX_CPU_USAGE("general.maxcpuusage"),
    MAX_LOG_SIZE("general.max-log-size");

    private String configString;

    ClientSettings(String configString) {
        this.configString = configString;
    }

    public static ClientSettings getSettingByName(String in) {
        ClientSettings clientSettings;
        try {
            clientSettings = valueOf(in.toUpperCase());
        } catch (final Throwable throwable) {
            return null;
        }

        return clientSettings;
    }
}
