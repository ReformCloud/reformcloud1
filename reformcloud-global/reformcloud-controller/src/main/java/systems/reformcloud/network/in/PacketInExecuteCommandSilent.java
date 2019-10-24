/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.commands.utility.CommandSender;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 04.05.2019
 */

public final class PacketInExecuteCommandSilent implements Serializable,
    NetworkQueryInboundHandler {

    @Override
    public void handle(Configuration configuration, UUID resultID) {
        ReformCloudController.getInstance().getCommandManager()
            .dispatchCommand(new CommandSenderImpl(
                configuration, resultID
            ), configuration.getStringValue("line"));
    }

    private class CommandSenderImpl implements CommandSender {

        private Configuration in;
        private UUID resultID;

        private boolean sent = false;

        CommandSenderImpl(Configuration in, UUID resultID) {
            this.in = in;
            this.resultID = resultID;
        }

        @Override
        public void sendMessage(String message) {
            if (sent) {
                return;
            }

            sent = true;
            ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                in.getStringValue("from"),
                new DefaultPacket(StringUtil.NULL, new Configuration().addStringValue("result", message),
                    resultID)
            );
        }

        @Override
        public boolean hasPermission(String permission) {
            return true;
        }
    }
}
