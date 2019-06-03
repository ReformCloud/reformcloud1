/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.commands.CommandPermissions;
import systems.reformcloud.database.PermissionDatabase;
import systems.reformcloud.listener.PlayerDisconnectedListener;
import systems.reformcloud.network.out.PacketOutUpdatePermissionCache;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 10.03.2019
 */

public final class PermissionsAddon extends ControllerAddonImpl implements Serializable {

    private static PermissionsAddon instance;

    private PermissionDatabase permissionDatabase;

    public static PermissionsAddon getInstance() {
        return PermissionsAddon.instance;
    }

    @Override
    public void onAddonClazzPrepare() {
        instance = this;
    }

    @Override
    public void onAddonLoading() {
        permissionDatabase = new PermissionDatabase();
        ReformCloudController.getInstance().getChannelHandler()
            .sendToAllDirect(new PacketOutUpdatePermissionCache());
        this.registerCommand(new CommandPermissions());
        ReformCloudController.getInstance().getEventManager()
            .registerListener(new PlayerDisconnectedListener());
    }

    @Override
    public void onAddonReadyToClose() {
        instance = null;
        permissionDatabase = null;

        ReformCloudController.getInstance().getNettyHandler()
            .unregisterQueryHandler("QueryGetPermissionCache")
            .unregisterQueryHandler("QueryGetPermissionHolder")

            .unregisterHandler("UpdatePermissionHolder");
    }

    public PermissionDatabase getPermissionDatabase() {
        return this.permissionDatabase;
    }
}
