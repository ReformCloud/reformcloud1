/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import systems.reformcloud.ReformCloudController;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.signs.configuration.SignConfiguration;
import systems.reformcloud.signs.netty.in.PacketInCreateSign;
import systems.reformcloud.signs.netty.in.PacketInRemoveSign;
import systems.reformcloud.signs.netty.in.PacketInRequestSigns;
import systems.reformcloud.utility.runtime.Shutdown;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public final class SignSelector implements Shutdown {
    private static SignSelector instance;

    private SignConfiguration signConfiguration;

    /**
     * Creates a new SignSelector instance
     *
     * @throws Throwable
     */
    public SignSelector() throws Throwable {
        if (instance == null)
            instance = this;
        else
            throw new InstanceAlreadyExistsException();

        this.signConfiguration = new SignConfiguration();
        this.signConfiguration.loadAll();

        ReformCloudController.getInstance().getNettyHandler()
                .registerHandler("CreateSign", new PacketInCreateSign())
                .registerHandler("RemoveSign", new PacketInRemoveSign())

                .registerQueryHandler("QueryGetSigns", new PacketInRequestSigns());
    }

    public static SignSelector getInstance() {
        return SignSelector.instance;
    }

    /**
     * Shutdowns the SignSelector
     */
    @Override
    public void shutdownAll() {
        instance = null;
    }

    public SignConfiguration getSignConfiguration() {
        return this.signConfiguration;
    }
}
