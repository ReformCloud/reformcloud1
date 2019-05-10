/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import systems.reformcloud.event.utility.Listener;
import systems.reformcloud.listeners.ClientListener;
import systems.reformcloud.listeners.ProxyListener;
import systems.reformcloud.utility.ControllerAddonImpl;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 09.05.2019
 */

public final class CloudFlareAddon extends ControllerAddonImpl implements Serializable {
    private Listener proxyListener = new ProxyListener(), clientListener = new ClientListener();

    @Override
    public void onAddonLoading() {
        new CloudFlareUtil();
        ReformCloudController.getInstance().getEventManager().registerListener(proxyListener);
        ReformCloudController.getInstance().getEventManager().registerListener(clientListener);
    }

    @Override
    public void onAddonReadyToClose() {
        CloudFlareUtil.getInstance().shutdown();
        ReformCloudController.getInstance().getEventManager().unregisterListenerByClassLoader(proxyListener.getClass().getClassLoader());
        ReformCloudController.getInstance().getEventManager().unregisterListenerByClassLoader(clientListener.getClass().getClassLoader());
    }
}
