/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.config.AutoIconConfig;
import systems.reformcloud.network.in.PacketInGetConfig;
import systems.reformcloud.network.out.PacketOutDisableIcons;
import systems.reformcloud.network.out.PacketOutEnableIcons;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class AutoIcon extends ControllerAddonImpl implements Serializable {

    @Override
    public void onAddonLoading() {
        new AutoIconConfig();
        ReformCloudController.getInstance().getNettyHandler()
            .registerQueryHandler("GetConfig", new PacketInGetConfig());
        ReformCloudController.getInstance().getAllRegisteredProxies()
            .forEach(e -> ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                e.getCloudProcess().getName(),
                new PacketOutEnableIcons()
            ));
    }

    @Override
    public void onAddonReadyToClose() {
        ReformCloudController.getInstance().getNettyHandler().unregisterQueryHandler("GetConfig");
        ReformCloudController.getInstance().getAllRegisteredProxies()
            .forEach(e -> ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                e.getCloudProcess().getName(),
                new PacketOutDisableIcons()
            ));
    }
}
