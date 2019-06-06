/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.commands.ProxyCommand;
import systems.reformcloud.configuration.ProxyAddonConfiguration;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class ProxyAddon extends ControllerAddonImpl implements Serializable {

    private static ProxyAddon instance;

    private ProxyAddonConfiguration proxyAddonConfiguration;

    public static ProxyAddon getInstance() {
        return ProxyAddon.instance;
    }

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
        ReformCloudController.getInstance().getNettyHandler()
            .unregisterQueryHandler("GetProxyConfig");
    }

    public ProxyAddonConfiguration getProxyAddonConfiguration() {
        return this.proxyAddonConfiguration;
    }
}
