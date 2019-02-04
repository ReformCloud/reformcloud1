/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta;

import systems.reformcloud.meta.enums.TemplateBackend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 21.10.2018
 */

@AllArgsConstructor
@Getter
@Setter
public class Template implements Serializable {
    private static final long serialVersionUID = -7937892033580579125L;

    private String name, template_url;
    private TemplateBackend templateBackend;
}
