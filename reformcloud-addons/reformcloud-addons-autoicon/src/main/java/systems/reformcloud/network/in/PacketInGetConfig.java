/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.autoicon.IconData;
import systems.reformcloud.config.AutoIconConfig;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.info.ProxyInfo;
import systems.reformcloud.network.interfaces.NetworkQueryInboundHandler;
import systems.reformcloud.network.packet.DefaultPacket;
import systems.reformcloud.utility.StringUtil;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class PacketInGetConfig implements Serializable, NetworkQueryInboundHandler {

    @Override
    public void handle(Configuration configuration, UUID resultID) {
        if (AutoIconConfig.getInstance() != null) {
            ProxyInfo proxyInfo = ReformCloudController.getInstance()
                .getProxyInfo(configuration.getStringValue("from"));
            if (proxyInfo == null) {
                return;
            }

            IconData iconData = AutoIconConfig.getInstance()
                .ofGroup(proxyInfo.getProxyGroup().getName());
            if (iconData != null) {
                ReformCloudController.getInstance().getChannelHandler().sendDirectPacket(
                    configuration.getStringValue("from"),
                    new DefaultPacket(
                        StringUtil.NULL,
                        new Configuration().addValue("data", iconData),
                        resultID
                    )
                );
            }
        }
    }
}
