/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.cloudsystem;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@AllArgsConstructor
@Getter
public final class EthernetAddress implements Serializable {
    /**
     * The host of the address
     */
    private String host;

    /**
     * The port of the address
     */
    private int port;
}
