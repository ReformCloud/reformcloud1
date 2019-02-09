/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

@AllArgsConstructor
@Getter
public final class Trio<F, S, T> implements Serializable {
    private F first;
    private S second;
    private T third;
}
