/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs;

import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.signs.configuration.SignConfiguration;
import systems.reformcloud.signs.netty.in.PacketInCreateSign;
import systems.reformcloud.signs.netty.in.PacketInRemoveSign;
import systems.reformcloud.signs.netty.in.PacketInRequestSignUpdate;
import systems.reformcloud.signs.netty.in.PacketInRequestSigns;
import systems.reformcloud.utility.runtime.Shutdown;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

@Getter
public final class SignSelector implements Shutdown {
    @Getter
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
                .registerHandler("RequestSigns", new PacketInRequestSigns())
                .registerHandler("RequestSignUpdate", new PacketInRequestSignUpdate());
    }

    /**
     * Shutdowns the SignSelector
     */
    @Override
    public void shutdownAll() {
        instance = null;

        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("CreateSign");
        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("RemoveSign");
        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("RequestSigns");
        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("RequestSignUpdate");
    }
}
