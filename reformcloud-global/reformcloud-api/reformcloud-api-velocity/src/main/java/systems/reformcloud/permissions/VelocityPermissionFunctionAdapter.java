/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.permissions;

import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.permission.PermissionSubject;
import com.velocitypowered.api.permission.Tristate;
import com.velocitypowered.api.proxy.Player;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionHolder;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 05.05.2019
 */

public final class VelocityPermissionFunctionAdapter implements Serializable, PermissionFunction {
    @java.beans.ConstructorProperties({"permissionSubject"})
    public VelocityPermissionFunctionAdapter(PermissionSubject permissionSubject) {
        this(((Player) permissionSubject).getUniqueId());
    }

    @java.beans.ConstructorProperties({"uniqueID"})
    public VelocityPermissionFunctionAdapter(UUID uniqueID) {
        this.permissionSubject = uniqueID;
    }

    private UUID permissionSubject;
    private PermissionHolder permissionHolder;

    @Override
    public Tristate getPermissionValue(String s) {
        PermissionHolder permissionHolder = this.getHolder();
        if (permissionHolder == null)
            return Tristate.FALSE;

        return permissionHolder.hasPermission(
                s,
                this.getGroups(permissionHolder)
        ) ? Tristate.TRUE : Tristate.FALSE;
    }

    private PermissionHolder getHolder() {
        if (permissionHolder != null)
            return permissionHolder;

        this.permissionHolder = ReformCloudAPIVelocity.getInstance().createPacketFuture(
                new PacketOutQueryGetPermissionHolder(
                        new PermissionHolder(permissionSubject, Collections.singletonMap(ReformCloudAPIVelocity
                                .getInstance().getPermissionCache().getDefaultGroup().getName(), -1L), new HashMap<>())
                ), "ReformCloudController"
        ).sendOnCurrentThread().syncUninterruptedly(2, TimeUnit.SECONDS).getConfiguration().getValue("holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE());
        ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders().put(permissionHolder.getUniqueID(), permissionHolder);
        return this.permissionHolder;
    }

    private List<PermissionGroup> getGroups(PermissionHolder permissionHolder) {
        return permissionHolder.getAllPermissionGroups(ReformCloudAPIVelocity.getInstance().getPermissionCache());
    }
}
