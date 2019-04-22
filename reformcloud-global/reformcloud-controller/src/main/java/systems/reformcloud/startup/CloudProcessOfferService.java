/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.startup;

import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.meta.info.ServerInfo;
import systems.reformcloud.network.out.PacketOutStartGameServer;
import systems.reformcloud.network.out.PacketOutStartProxy;
import systems.reformcloud.utility.map.maps.Trio;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 30.10.2018
 */

public final class CloudProcessOfferService implements Runnable, Serializable {
    @Getter
    private Map<String, String> waiting = ReformCloudLibraryService.concurrentHashMap();

    @Getter
    private Map<String, String> waitingPerClient = ReformCloudLibraryService.concurrentHashMap();

    private List<Trio<String, String, Integer>> servers = new ArrayList<>();
    private List<Trio<String, String, Integer>> proxies = new ArrayList<>();

    private void offerServers() {
        ReformCloudController.getInstance().getInternalCloudNetwork().getServerGroups().values().forEach(serverGroup -> {
            Client client = ReformCloudController.getInstance().getBestClient(serverGroup.getClients(), serverGroup.getMemory());

            if (client == null)
                return;

            final Collection<String> servers = ReformCloudController.getInstance().getInternalCloudNetwork()
                    .getServerProcessManager().getOnlineServers(serverGroup.getName());
            final Collection<String> waiting = this.getWaiting(serverGroup.getName());

            final int waitingAndOnline = servers.size() + waiting.size();
            final String id = Integer.toString(this.nextServerID(serverGroup.getName()));
            final String name = serverGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;

            if (waitingAndOnline < serverGroup.getMinOnline() && (serverGroup.getMaxOnline() > waitingAndOnline || serverGroup.getMaxOnline() == -1)) {
                this.waiting.put(name, serverGroup.getName());
                this.waitingPerClient.put(name, client.getName());
                this.registerID(serverGroup.getName(), name, Integer.valueOf(id));
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
            final String id = Integer.toString(this.nextProxyID(proxyGroup.getName()));
            final String name = proxyGroup.getName() + ReformCloudController.getInstance().getCloudConfiguration().getSplitter() + (Integer.parseInt(id) <= 9 ? "0" : "") + id;

            if (waitingAndOnline < proxyGroup.getMinOnline() && (proxyGroup.getMaxOnline() > waitingAndOnline || proxyGroup.getMaxOnline() == -1)) {
                this.waiting.put(name, proxyGroup.getName());
                this.waitingPerClient.put(name, startup.getName());
                this.registerProxyID(proxyGroup.getName(), name, Integer.valueOf(id));
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

    public void unregisterID(final ServerInfo serverInfo) {
        List<Trio<String, String, Integer>> clone = new ArrayList<>(this.servers);
        clone.forEach(e -> {
            if (e.getFirst().equals(serverInfo.getServerGroup().getName()) && e.getSecond().equals(serverInfo.getCloudProcess().getName()))
                this.servers.remove(e);
        });
    }

    public int nextServerID(final String groupName) {
        List<Integer> servers = new ArrayList<>();
        this.servers.stream().filter(e -> e.getFirst().equals(groupName)).forEach(e -> servers.add(e.getThird()));

        int id = 1;
        while (servers.contains(id))
            id++;

        return id;
    }

    public void registerID(final String group, final String name, final int id) {
        this.servers.add(new Trio<>(group, name, id));
    }

    public void unregisterProxyID(final ProxyInfo proxyInfo) {
        List<Trio<String, String, Integer>> clone = new ArrayList<>(this.proxies);
        clone.forEach(e -> {
            if (e.getFirst().equals(proxyInfo.getProxyGroup().getName()) && e.getSecond().equals(proxyInfo.getCloudProcess().getName()))
                this.proxies.remove(e);
        });
    }

    public int nextProxyID(final String groupName) {
        List<Integer> servers = new ArrayList<>();
        this.proxies.stream().filter(e -> e.getFirst().equals(groupName)).forEach(e -> servers.add(e.getThird()));

        int id = 1;
        while (servers.contains(id))
            id++;

        return id;
    }

    public void registerProxyID(final String group, final String name, final int id) {
        this.proxies.add(new Trio<>(group, name, id));
    }

    public void removeWaitingProcess(String name) {
        this.waiting.remove(name);

        List<Trio<String, String, Integer>> clone = new ArrayList<>(this.proxies);
        clone.forEach(e -> {
            if (e.getSecond().equals(name))
                this.proxies.remove(e);
        });

        List<Trio<String, String, Integer>> cloneServers = new ArrayList<>(this.servers);
        cloneServers.forEach(e -> {
            if (e.getSecond().equals(name))
                this.servers.remove(e);
        });
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            this.offerServers();
            this.offerProxies();

            ReformCloudLibraryService.sleep(TimeUnit.SECONDS, 2);
        }
    }
}
