/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.02.2019
 */

@AllArgsConstructor
@Getter
public final class TemplateMap<F, S, T> implements Serializable {
    private F group;
    private S template;
    private T layout;
}
