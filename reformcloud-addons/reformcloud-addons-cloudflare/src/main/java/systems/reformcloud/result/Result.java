/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.result;

import java.beans.ConstructorProperties;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.05.2019
 */

public final class Result implements Serializable {
    private String id, email, token, name;

    @ConstructorProperties({"id", "email", "token", "name"})
    public Result(String id, String email, String token, String name) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
