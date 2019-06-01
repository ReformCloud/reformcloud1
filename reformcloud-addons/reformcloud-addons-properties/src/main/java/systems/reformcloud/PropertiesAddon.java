/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.configuration.PropertiesConfig;
import systems.reformcloud.network.out.PacketOutDisableProperties;
import systems.reformcloud.network.out.PacketOutEnableProperties;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

public final class PropertiesAddon extends ControllerAddonImpl implements Serializable {
    @Override
    public void onAddonClazzPrepare() {
    }

    @Override
    public void onAddonLoading() {
        new PropertiesConfig();
        ReformCloudController.getInstance().sendPacketToAll(new PacketOutEnableProperties());
    }

    @Override
    public void onAddonReadyToClose() {
        ReformCloudController.getInstance().sendPacketToAll(new PacketOutDisableProperties());
        ReformCloudController.getInstance().getNettyHandler().unregisterQueryHandler("RequestProperties");
    }
}
