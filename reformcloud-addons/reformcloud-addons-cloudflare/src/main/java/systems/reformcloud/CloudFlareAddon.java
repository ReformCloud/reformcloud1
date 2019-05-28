/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.event.utility.Listener;
import systems.reformcloud.listeners.client.ClientCreatedListener;
import systems.reformcloud.listeners.client.ClientDeletedListener;
import systems.reformcloud.listeners.proxy.ProxyStartedListener;
import systems.reformcloud.listeners.proxy.ProxyStoppedListener;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 09.05.2019
 */

public final class CloudFlareAddon extends ControllerAddonImpl implements Serializable {
    private Listener
            proxyStartedListener = new ProxyStartedListener(),
            proxyStoppedListener = new ProxyStoppedListener(),
            clientCreatedListener = new ClientCreatedListener(),
            clientDeletedListener = new ClientDeletedListener();

    @Override
    public void onAddonLoading() {
        new CloudFlareUtil();
        ReformCloudController.getInstance().getEventManager().registerListener(clientCreatedListener);
        ReformCloudController.getInstance().getEventManager().registerListener(clientDeletedListener);
        ReformCloudController.getInstance().getEventManager().registerListener(proxyStartedListener);
        ReformCloudController.getInstance().getEventManager().registerListener(proxyStoppedListener);
    }

    @Override
    public void onAddonReadyToClose() {
        CloudFlareUtil.getInstance().shutdown();
        ReformCloudController.getInstance().getEventManager().unregisterListenerByClassLoader(clientCreatedListener.getClass().getClassLoader());
        ReformCloudController.getInstance().getEventManager().unregisterListenerByClassLoader(clientDeletedListener.getClass().getClassLoader());
        ReformCloudController.getInstance().getEventManager().unregisterListenerByClassLoader(proxyStartedListener.getClass().getClassLoader());
        ReformCloudController.getInstance().getEventManager().unregisterListenerByClassLoader(proxyStoppedListener.getClass().getClassLoader());
    }
}
