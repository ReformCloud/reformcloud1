/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import lombok.Getter;
import systems.reformcloud.configuration.ParametersConfiguration;
import systems.reformcloud.network.out.PacketOutParametersUpdate;
import systems.reformcloud.utility.ControllerAddonImpl;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

@Getter
public final class ParametersAddon extends ControllerAddonImpl implements Serializable {
    @Getter
    public static ParametersAddon instance;

    private ParametersConfiguration parametersConfiguration;

    @Override
    public void onAddonClazzPrepare() {
        instance = this;
    }

    @Override
    public void onAddonLoading() {
        this.parametersConfiguration = new ParametersConfiguration();
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(new PacketOutParametersUpdate());
    }

    @Override
    public void onAddonReadyToClose() {
        ReformCloudController.getInstance().getNettyHandler().unregisterQueryHandler("RequestParameters");
    }
}
