/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.autoicon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

@AllArgsConstructor
@Getter
public final class IconData implements Serializable {
    private List<byte[]> icons;
    private int updateTimeInSeconds;
}
