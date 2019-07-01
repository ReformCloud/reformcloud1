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

    /**
     * The user name of the web user
     */
    private String userName;

    /**
     * The hashed password of the web user
     */
    private String password;

    /**
     * The permissions of the web user
     */
    private Map<String, Boolean> permissions;

    @java.beans.ConstructorProperties({"userName", "password", "permissions"})
    public WebUser(String userName, String password, Map<String, Boolean> permissions) {
        this.userName = userName;
        this.password = password;
        this.permissions = permissions;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public Map<String, Boolean> getPermissions() {
        return this.permissions;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        final Object this$user = this.getUserName();
        final Object other$user = other.getUserName();
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
        final Object $user = this.getUserName();
        result = result * PRIME + ($user == null ? 43 : $user.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $permissions = this.getPermissions();
        result = result * PRIME + ($permissions == null ? 43 : $permissions.hashCode());
        return result;
    }

    public String toString() {
        return "WebUser(userName=" + this.getUserName() + ", password=" + this.getPassword()
            + ", permissions=" + this.getPermissions() + ")";
    }
}
