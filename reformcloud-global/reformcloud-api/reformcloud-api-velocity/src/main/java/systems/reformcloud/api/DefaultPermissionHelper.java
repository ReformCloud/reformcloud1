/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;
import systems.reformcloud.ReformCloudAPIVelocity;
import systems.reformcloud.api.permissions.PermissionHelper;
import systems.reformcloud.network.query.out.PacketOutQueryGetPermissionHolder;
import systems.reformcloud.player.implementations.OfflinePlayer;
import systems.reformcloud.player.permissions.PermissionCache;
import systems.reformcloud.player.permissions.group.PermissionGroup;
import systems.reformcloud.player.permissions.player.PermissionHolder;
import systems.reformcloud.utility.TypeTokenAdaptor;

/**
 * @author _Klaro | Pasqual K. / created on 28.06.2019
 */

public final class DefaultPermissionHelper implements Serializable, PermissionHelper {

    @Override
    public PermissionGroup getPermissionGroup(String name) {
        return getPermissionCache().getGroup(name);
    }

    @Override
    public PermissionHolder getPermissionHolder(String name) {
        OfflinePlayer offlinePlayer =
            ReformCloudAPIVelocity.getInstance().getOfflinePlayer(name);
        if (offlinePlayer == null) {
            return null;
        }

        return getPermissionHolder(offlinePlayer.getUniqueID());
    }

    @Override
    public PermissionHolder getPermissionHolder(UUID uniqueID) {
        if (ReformCloudAPIVelocity.getInstance().getPermissionCache() != null) {
            if (ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders()
                .containsKey(uniqueID)) {
                return ReformCloudAPIVelocity.getInstance().getCachedPermissionHolders()
                    .get(uniqueID);
            }

            return ReformCloudAPIVelocity.getInstance().getChannelHandler().sendPacketQuerySync(
                "ReformCloudController",
                ReformCloudAPIVelocity.getInstance().getProxyInfo().getCloudProcess().getName(),
                new PacketOutQueryGetPermissionHolder(
                    new PermissionHolder(uniqueID,
                        Collections.singletonMap(ReformCloudAPIVelocity
                            .getInstance().getPermissionCache().getDefaultGroup()
                            .getName(), -1L), new HashMap<>())
                )
            ).sendOnCurrentThread().syncUninterruptedly().getConfiguration().getValue(
                "holder", TypeTokenAdaptor.getPERMISSION_HOLDER_TYPE()
            );
        }

        return null;
    }

    @Override
    public PermissionCache getPermissionCache() {
        return ReformCloudAPIVelocity.getInstance().getPermissionCache();
    }
}
