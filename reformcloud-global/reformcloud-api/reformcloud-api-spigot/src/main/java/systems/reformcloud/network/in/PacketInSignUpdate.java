/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.signs.SignLayoutConfiguration;
import systems.reformcloud.utility.TypeTokenAdaptor;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author _Klaro | Pasqual K. / created on 19.04.2019
 */

public final class PacketInSignUpdate implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        if (SignSelector.getInstance() != null) {
            SignLayoutConfiguration signLayoutConfiguration = configuration
                .getValue("signConfig", TypeTokenAdaptor.getSIGN_LAYOUT_CONFIG_TYPE());
            Map<UUID, Sign> map = configuration
                .getValue("signMap", new TypeToken<Map<UUID, Sign>>() {
                }.getType());
            SignSelector.getInstance().setSignLayoutConfiguration(signLayoutConfiguration);
            SignSelector.getInstance().setSignMap(map);
        }
    }
}
