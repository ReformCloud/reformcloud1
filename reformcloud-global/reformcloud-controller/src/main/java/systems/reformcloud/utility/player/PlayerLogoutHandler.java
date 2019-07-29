/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.player;

import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.event.events.PlayerDisconnectsEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.packet.PacketFuture;
import systems.reformcloud.network.query.out.PacketOutQueryGetOnlinePlayers;
import systems.reformcloud.utility.annotiations.InternalClass;
import systems.reformcloud.utility.annotiations.MayNotBePresent;

/**
 * This class should fix the problem that when the player logout event is not
 * called properly on the proxy service the player could connect with a sub
 * server using an offline account. It should also fix the problem that the
 * proxy online count is not counted correctly.
 *
 * @author _Klaro | Pasqual K. / created on 22.06.2019
 */

@InternalClass
public final class PlayerLogoutHandler extends Thread implements Serializable {

    public PlayerLogoutHandler() {
        setDaemon(true);
        setPriority(Thread.MIN_PRIORITY);
        setName("PlayerLogoutHandler");

        start();
    }

    private final List<ProxyInfo> proxyInfos = new LinkedList<>();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ReformCloudController.getInstance().getAllRegisteredProxies().forEach(proxyInfo -> {
                List<UUID> forRemoval = new ArrayList<>();
                List<UUID> currentOnline = this.getPlayersOfProxy(proxyInfo);
                if (currentOnline != null) {
                    proxyInfo.getOnlinePlayers().forEach(uuid -> {
                        if (!currentOnline.contains(uuid)) {
                            ReformCloudController.getInstance().getColouredConsoleProvider()
                                .serve().accept(uuid.toString());
                            forRemoval.add(uuid);
                            proxyInfo.setOnline(proxyInfo.getOnline() - 1);
                            proxyInfos.add(proxyInfo);
                            Runnable patch = () -> {
                                ReformCloudController.getInstance().getPlayerDatabase()
                                    .logoutPlayer(uuid);
                                ReformCloudController.getInstance().getUuid().remove(uuid);
                                ReformCloudController.getInstance().getEventManager()
                                    .fire(new PlayerDisconnectsEvent(uuid));
                            };
                            patchAsync(patch);
                        }
                    });
                }

                forRemoval.forEach(e -> proxyInfo.getOnlinePlayers().remove(e));
            });

            if (!proxyInfos.isEmpty()) {
                proxyInfos.forEach(proxyInfo -> {
                    proxyInfo.setFull(proxyInfo.getOnline() >= proxyInfo.getProxyGroup().getMaxPlayers());
                    ReformCloudController.getInstance().updateProxyInfo(proxyInfo);
                    ReformCloudLibraryService.sleep(50);
                });
                proxyInfos.clear();
            }

            ReformCloudLibraryService.sleep(TimeUnit.SECONDS, 30);
        }
    }

    @MayNotBePresent
    private List<UUID> getPlayersOfProxy(ProxyInfo proxyInfo) {
        PacketFuture packetFuture = ReformCloudController.getInstance().getChannelHandler()
            .sendPacketQuerySync(proxyInfo.getCloudProcess().getName(),
                "ReformCloudController", new PacketOutQueryGetOnlinePlayers());
        return packetFuture == null ? null : packetFuture.sendOnCurrentThread()
            .syncUninterruptedly().getConfiguration()
            .getValue("players", new TypeToken<List<UUID>>() {
            });
    }

    private void patchAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }
}
