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

    /**
     * The name of the process
     */
    private String name;

    /**
     * The uid of the process
     */
    private UUID processUID;

    /**
     * The client where the process was started
     */
    private String client;

    /**
     * The template which was loaded
     */
    private Template loadedTemplate;

    /**
     * The process id of the process
     */
    private int processID;
}
