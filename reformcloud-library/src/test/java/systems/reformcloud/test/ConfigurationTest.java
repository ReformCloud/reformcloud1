/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.test;

import org.junit.Test;
import systems.reformcloud.configurations.Configuration;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class ConfigurationTest implements Serializable {
    @Test
    public void configurationTest() {
        Configuration configuration = new Configuration();
        configuration.addStringValue("test", "test").addValue("uuid", UUID.randomUUID());
        System.out.println(configuration.getJsonString());
        configuration.clear();
        System.out.println(configuration.getJsonString());
    }
}
