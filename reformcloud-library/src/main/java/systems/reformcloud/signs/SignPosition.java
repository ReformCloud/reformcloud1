/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Getter
public class SignPosition {
    private String targetGroup, world;
    private int x, y, z;
}
