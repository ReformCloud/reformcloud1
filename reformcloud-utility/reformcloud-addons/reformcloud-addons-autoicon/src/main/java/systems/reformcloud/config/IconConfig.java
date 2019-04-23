/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

@AllArgsConstructor
@Getter
public final class IconConfig implements Serializable {
    private String targetGroup;
    private int updateTimeInSeconds;
    private List<String> iconPaths;
}
