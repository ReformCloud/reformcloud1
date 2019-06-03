/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.meta.web;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @author _Klaro | Pasqual K. / created on 16.12.2018
 */

public class WebUser implements Serializable {

    private static final long serialVersionUID = 6104918827767931388L;

    private String user;

    private String password;

    private Map<String, Boolean> permissions;

    @java.beans.ConstructorProperties({"user", "password", "permissions"})
    public WebUser(String user, String password, Map<String, Boolean> permissions) {
        this.user = user;
        this.password = password;
        this.permissions = permissions;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public Map<String, Boolean> getPermissions() {
        return this.permissions;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WebUser)) {
            return false;
        }
        final WebUser other = (WebUser) o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$user = this.getUser();
        final Object other$user = other.getUser();
        if (!Objects.equals(this$user, other$user)) {
            return false;
        }
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (!Objects.equals(this$password, other$password)) {
            return false;
        }
        final Object this$permissions = this.getPermissions();
        final Object other$permissions = other.getPermissions();
        if (!Objects.equals(this$permissions, other$permissions)) {
            return false;
        }
        return true;
    }

    private boolean canEqual(final Object other) {
        return other instanceof WebUser;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $user = this.getUser();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $permissions = this.getPermissions();
        result = result * PRIME + ($permissions == null ? 43 : $permissions.hashCode());
        return result;
    }

    public String toString() {
        return "WebUser(user=" + this.getUser() + ", password=" + this.getPassword()
            + ", permissions=" + this.getPermissions() + ")";
    }
}
