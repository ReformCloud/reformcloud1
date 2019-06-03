/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.configuration.ParametersConfiguration;
import systems.reformcloud.network.out.PacketOutParametersUpdate;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 14.04.2019
 */

public final class ParametersAddon extends ControllerAddonImpl implements Serializable {

    private static ParametersAddon instance;

    private ParametersConfiguration parametersConfiguration;

    public static ParametersAddon getInstance() {
        return ParametersAddon.instance;
    }

    @Override
    public void onAddonClazzPrepare() {
        instance = this;
    }

    @Override
    public void onAddonLoading() {
        this.parametersConfiguration = new ParametersConfiguration();
        ReformCloudController.getInstance().getChannelHandler()
            .sendToAllSynchronized(new PacketOutParametersUpdate());
    }

    @Override
    public void onAddonReadyToClose() {
        ReformCloudController.getInstance().getNettyHandler()
            .unregisterQueryHandler("RequestParameters");
    }

    public ParametersConfiguration getParametersConfiguration() {
        return this.parametersConfiguration;
    }
}
