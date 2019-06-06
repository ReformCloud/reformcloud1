/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.web;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public class InternalWebUser implements Serializable {

    private static final long serialVersionUID = -8467193156656410810L;

    private String name;

    private String password;

    @java.beans.ConstructorProperties({"name", "password"})
    public InternalWebUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
