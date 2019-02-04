/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

@AllArgsConstructor
@Getter
@Setter
public class InternalWebUser implements Serializable {
    private static final long serialVersionUID = -8467193156656410810L;

    private String name, password;
}
