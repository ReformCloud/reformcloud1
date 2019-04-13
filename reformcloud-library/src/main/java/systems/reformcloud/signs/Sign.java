/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.meta.info.ServerInfo;

import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 11.12.2018
 */

@AllArgsConstructor
@Getter
public class Sign {
    private UUID uuid;
    private SignPosition signPosition;

    @Setter
    private ServerInfo serverInfo;
}
