/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.serverprocess.screen;

import systems.reformcloud.ReformCloudClient;
import systems.reformcloud.network.packets.sync.out.PacketOutSyncScreenUpdate;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author _Klaro | Pasqual K. / created on 05.02.2019
 */

public class ScreenHandler implements Serializable {
    private final Queue<String> log = new ConcurrentLinkedDeque<>();
    private boolean enabled = false;

    private final String name;

    public ScreenHandler(final String name) {
        this.name = name;
    }

    public void addScreenLine(final String line) {
        if (line == null)
            return;

        while (log.size() >= ReformCloudClient.getInstance().getCloudConfiguration().getLogSize())
            log.poll();

        this.log.offer(line);

        if (this.enabled) {
            this.sendLine(line);
        }
    }

    public void enableScreen() {
        for (String line : log)
            this.sendLine(line);

        this.enabled = true;
    }

    public void disableScreen() {
        this.enabled = false;
    }

    private void sendLine(final String line) {
        if (line == null || line.trim().isEmpty())
            return;

        this.sendScreenLine(line);
    }

    private void sendScreenLine(final String text) {
        ReformCloudClient.getInstance().getChannelHandler().sendPacketAsynchronous("ReformCloudController", new PacketOutSyncScreenUpdate(text, name));
    }
}
