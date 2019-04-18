/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import lombok.Getter;
import systems.reformcloud.commands.ProxyCommand;
import systems.reformcloud.configuration.ProxyAddonConfiguration;
import systems.reformcloud.utility.ControllerAddonImpl;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

@Getter
public final class ProxyAddon extends ControllerAddonImpl implements Serializable {
    @Getter
    private static ProxyAddon instance;

    private ProxyAddonConfiguration proxyAddonConfiguration;

    @Override
    public void onAddonClazzPrepare() {
        instance = this;
    }

    @Override
    public void onAddonLoading() {
        this.proxyAddonConfiguration = new ProxyAddonConfiguration();
        ReformCloudController.getInstance().getCommandManager().registerCommand(new ProxyCommand());
    }

    @Override
    public void onAddonReadyToClose() {
        ReformCloudController.getInstance().getNettyHandler().unregisterQueryHandler("GetProxyConfig");
    }
}
