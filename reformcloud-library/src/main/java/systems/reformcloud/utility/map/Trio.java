/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public Trio<F, T, S> clone() {
        return new Trio(first, second, third);
    }
}
