/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Getter
public final class SignPosition implements Serializable {
    /**
     * The general information about the sign
     */
    private String targetGroup, world;

    /**
     * The sign position
     */
    private int x, y, z;
}
