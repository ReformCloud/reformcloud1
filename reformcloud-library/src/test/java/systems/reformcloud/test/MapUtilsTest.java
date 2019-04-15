/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.test;

import org.junit.Test;
import systems.reformcloud.utility.map.MapUtility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author _Klaro | Pasqual K. / created on 15.04.2019
 */

public final class MapUtilsTest implements Serializable {
    @Test
    public void mapUtilsTest() {
        Map<String, String> override = new HashMap<>();
        override.put("Hallo1", "Hallo2");
        override.put("Hallo3", "Hallo4");

        override.forEach((key, value) -> System.out.println("Key: " + key + " Value: " + value));

        Map<String, String> copy = MapUtility.copyOf(override);
        copy.forEach((key, value) -> System.out.println("Key: " + key + " Value: " + value));
    }
}
