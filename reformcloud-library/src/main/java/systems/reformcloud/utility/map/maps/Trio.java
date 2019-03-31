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
    private F first;
    private S second;
    private T third;

    public <V> boolean contains(V toFind) {
        return MapUtility.contains(this, toFind);
    }

    public Trio<F, T, S> clone() {
        return new Trio(first, second, third);
    }
}
