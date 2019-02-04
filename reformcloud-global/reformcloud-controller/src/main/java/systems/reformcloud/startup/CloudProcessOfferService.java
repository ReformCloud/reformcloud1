/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.startup;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.netty.out.PacketOutStartGameServer;
import systems.reformcloud.netty.out.PacketOutStartProxy;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public class CloudProcessOfferService implements Runnable {
    @Getter
    private Map<String, String> waiting = ReformCloudLibraryService.concurrentHashMap();

    private void offerServers() {
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().values().forEach(serverGroup -> {
            Client client = ReformCloudController.getInstance().getBestClient(serverGroup.getClients(), serverGroup.getMemory());

            if (client == null)
                return;

            final Collection<String> servers = ReformCloudController.getInstance().getInternalCloudNetwork()
                    .getServerProcessManager().getOnlineServers(serverGroup.getName());
            final Collection<String> waiting = this.getWaiting(serverGroup.getName());

            final int waitingAndOnline = servers.size() + waiting.size();
            final String id = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeServerID(serverGroup.getName());
            final String name = serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;

            if (waitingAndOnline < serverGroup.getMinOnline() && (serverGroup.getMaxOnline() > waitingAndOnline || serverGroup.getMaxOnline() == -1)) {
                this.waiting.put(name, serverGroup.getName());
                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(client.getName(),
                        new PacketOutStartGameServer(serverGroup, name, UUID.randomUUID(), new Configuration(), id)
                );
            }
        });
    }

    private void offerProxies() {
        ReformCloudController.getInstance().getInternalCloudNetwork().getProxyGroups().values().forEach(proxyGroup -> {
            Client startup = ReformCloudController.getInstance().getBestClient(proxyGroup.getClients(), proxyGroup.getMemory());

            if (startup == null)
                return;

            final Collection<String> proxies = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager()
                    .getOnlineProxies(proxyGroup.getName());
            final Collection<String> waiting = this.getWaiting(proxyGroup.getName());

            final int waitingAndOnline = proxies.size() + waiting.size();
            final String id = ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager().nextFreeProxyID(proxyGroup.getName());
            final String name = proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;

            if (waitingAndOnline < proxyGroup.getMinOnline() && (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1)) {
                this.waiting.put(name, proxyGroup.getName());
                ReformCloudController.getInstance().getChannelHandler().sendPacketAsynchronous(startup.getName(),
                        new PacketOutStartProxy(proxyGroup, name, UUID.randomUUID(), new Configuration(), id)
                );
            }
        });
    }

    public Collection<String> getWaiting(final String name) {
        Collection<String> collection = new ArrayList<>();

        for (Map.Entry<String, String> map : this.waiting.entrySet())
            if (map.getValue().equals(name))
                collection.add(map.getKey());

        return collection;
    }

    @Override
    public void run() {
        this.offerServers();
        this.offerProxies();
    }
}
