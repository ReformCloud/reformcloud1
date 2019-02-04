/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@Getter
@AllArgsConstructor
public class Double<F, S> {
    private F first;
    private S second;
}
