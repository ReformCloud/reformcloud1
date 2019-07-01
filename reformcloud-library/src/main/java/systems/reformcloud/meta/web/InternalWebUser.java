/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.web;

import java.io.Serializable;
import java.util.Collections;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public class InternalWebUser extends WebUser implements Serializable {

    private static final long serialVersionUID = -8467193156656410810L;

    @java.beans.ConstructorProperties({"name", "password"})
    public InternalWebUser(String userName, String password) {
        super(userName, password, Collections.singletonMap("*", true));
    }
}
