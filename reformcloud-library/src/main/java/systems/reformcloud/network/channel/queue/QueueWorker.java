/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel.queue;

import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.packet.AwaitingPacket;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

public final class QueueWorker implements Serializable, Runnable {
    /**
     * The instance of the channel handler to send the packets in the correct channels
     */
    private final ChannelHandler instance = ReformCloudLibraryServiceProvider.getInstance().getChannelHandler();

    public QueueWorker() {
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            while (!instance.getPacketQueue().isEmpty()) {
                AwaitingPacket awaitingPacket = instance.getPacketQueue().poll();
                if (!awaitingPacket.getChannelHandlerContext().channel().isWritable()) {
                    instance.getPacketQueue().offer(awaitingPacket);
                    continue;
                }

                instance.sendPacket1(awaitingPacket);
                ReformCloudLibraryService.sleep(TimeUnit.MILLISECONDS, instance.getPacketQueue().isEmpty() ? 4 : 2);
            }

            ReformCloudLibraryService.sleep(TimeUnit.MILLISECONDS, 4);
        }
    }
}
