/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import systems.reformcloud.meta.info.ClientInfo;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@AllArgsConstructor
@Getter
@Setter
public class Client implements Serializable {
    private static final long serialVersionUID = 7702400116714803106L;

    private String name, ip;
    private ClientInfo clientInfo;
}
