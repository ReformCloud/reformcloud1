/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.addons.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * @author _Klaro | Pasqual K. / created on 10.12.2018
 */

@AllArgsConstructor
@Getter
public class AddonClassConfig {
    private File file;
    private String name, version, main;
}
