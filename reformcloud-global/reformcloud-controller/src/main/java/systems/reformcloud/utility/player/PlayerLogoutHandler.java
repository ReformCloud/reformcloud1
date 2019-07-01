/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.event.events.PlayerDisconnectsEvent;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.utility.annotiations.InternalClass;

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
                proxyInfo.getOnlinePlayers().forEach(uuid -> {
                    if (!findPlayerOnServer(uuid)) {
                        forRemoval.add(uuid);
                        proxyInfo.setOnline(proxyInfo.getOnline() - 1);
                        proxyInfos.add(proxyInfo);
                        Runnable patch = () -> {
                            ReformCloudController.getInstance().getPlayerDatabase().logoutPlayer(uuid);
                            ReformCloudController.getInstance().getUuid().remove(uuid);
                            ReformCloudController.getInstance().getEventManager().fire(new PlayerDisconnectsEvent(uuid));
                        };
                        patchAsync(patch);
                    }
                });

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

            ReformCloudLibraryService.sleep(900);
        }
    }

    private boolean findPlayerOnServer(UUID player) {
        return ReformCloudController.getInstance()
            .getAllRegisteredServers()
            .stream()
            .anyMatch(serverInfo -> serverInfo.getOnlinePlayers().contains(player));
    }

    private void patchAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }
}
