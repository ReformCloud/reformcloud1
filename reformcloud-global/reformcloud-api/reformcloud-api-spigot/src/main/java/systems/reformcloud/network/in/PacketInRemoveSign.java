/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;
import systems.reformcloud.signaddon.SignSelector;
import systems.reformcloud.signs.Sign;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class PacketInRemoveSign implements NetworkInboundHandler {
    @Override
    public void handle(Configuration configuration) {
        if (SignSelector.getInstance() != null) {
            SignSelector.getInstance().handleSignRemove(configuration.getValue("sign", new TypeToken<Sign>() {
            }.getType()));
        }
    }
}
