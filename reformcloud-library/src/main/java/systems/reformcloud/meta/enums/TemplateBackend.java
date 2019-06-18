/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.enums;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

public enum TemplateBackend implements Serializable {
    /**
     * The template will be saved in the client
     */
    CLIENT,

    /**
     * The template is saved externally as zip
     */
    URL,

    /**
     * The template is saved in the controller
     */
    CONTROLLER
}
