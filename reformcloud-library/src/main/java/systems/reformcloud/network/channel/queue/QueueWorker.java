/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.channel.queue;

import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import systems.reformcloud.ReformCloudLibraryServiceProvider;
import systems.reformcloud.network.channel.ChannelHandler;
import systems.reformcloud.network.packet.AwaitingPacket;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 28.03.2019
 */

@AllArgsConstructor
public final class QueueWorker implements Serializable, Job {
    private final ChannelHandler instance = ReformCloudLibraryServiceProvider.getInstance().getChannelHandler();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        if (!instance.getPacketQueue().isEmpty()) {
            AwaitingPacket awaitingPacket = instance.getPacketQueue().poll();
            if (!awaitingPacket.getChannelHandlerContext().channel().isWritable()) {
                instance.getPacketQueue().offer(awaitingPacket);
            }

            instance.sendPacket(awaitingPacket);
        }
    }
}
