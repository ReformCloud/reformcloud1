/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.netty.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.netty.interfaces.NetworkInboundHandler;
import systems.reformcloud.netty.packet.enums.QueryType;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInRequestSigns implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration, List<QueryType> queryTypes) {
        SignSelector.getInstance().setSignLayoutConfiguration(configuration.getValue("configuration", TypeTokenAdaptor.getSignLayoutConfigType()));
        SignSelector.getInstance().setSignMap(configuration.getValue("signs", new TypeToken<Map<UUID, Sign>>() {
        }.getType()));
    }
}
