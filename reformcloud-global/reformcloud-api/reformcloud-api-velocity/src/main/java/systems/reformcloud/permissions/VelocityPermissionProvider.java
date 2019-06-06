/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.permissions;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionProvider;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.proxy.Player;
import org.checkerframework.checker.optional.qual.MaybePresent;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class VelocityPermissionProvider implements Serializable, PermissionProvider {

    @Override
    @MaybePresent
    public PermissionFunction createFunction(PermissionSubject permissionSubject) {
        return permissionSubject instanceof Player ? new VelocityPermissionFunctionAdapter(
            permissionSubject) : null;
    }
}
