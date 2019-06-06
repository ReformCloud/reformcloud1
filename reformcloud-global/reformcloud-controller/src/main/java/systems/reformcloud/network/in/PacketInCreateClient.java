/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.network.in;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.client.Client;
import systems.reformcloud.network.interfaces.NetworkInboundHandler;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.04.2019
 */

public final class PacketInCreateClient implements Serializable, NetworkInboundHandler {

    @Override
    public void handle(Configuration configuration) {
        Client client = configuration.getValue("client", new TypeToken<Client>() {
        });
        if (client == null) {
            return;
        }

        ReformCloudController.getInstance().createClient(client);
    }
}
