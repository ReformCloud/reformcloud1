/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map.maps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.reformcloud.utility.map.MapUtility;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

@Getter
@AllArgsConstructor
public final class Trio<F, S, T> implements Serializable {
    /**
     * The first value of the map
     */
    private F first;

    /**
     * The second value of the map
     */
    private S second;

    /**
     * The third value of the map
     */
    private T third;

    /**
     * Checks if a specific parameter is in the trio
     *
     * @param toFind The key which should be checked for
     * @param <V>    The type of the key
     * @return If the trio contains the key
     */
    public <V> boolean contains(V toFind) {
        return MapUtility.contains(this, toFind);
    }
}
