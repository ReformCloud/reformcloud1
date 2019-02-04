/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 29.10.2018
 */

@AllArgsConstructor
@Getter
@Setter
public class CloudProcess implements Serializable {
    private static final long serialVersionUID = -532002825303576279L;

    private String name;
    private UUID processUID;
    private String client;
    private Template loadedTemplate;
    private int processID;
}
