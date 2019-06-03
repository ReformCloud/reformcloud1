/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import java.io.Serializable;
import systems.reformcloud.configuration.FTPConfigLoader;
import systems.reformcloud.helper.BackUpMaker;
import systems.reformcloud.network.query.PacketInQueryGetFTPConfig;
import systems.reformcloud.utility.ControllerAddonImpl;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class BackupAddon extends ControllerAddonImpl implements Serializable {

    private BackUpMaker backUpMaker;

    @Override
    public void onAddonLoading() {
        this.backUpMaker = new BackUpMaker(new FTPConfigLoader().load());
        this.backUpMaker.start();
        ReformCloudController.getInstance().getNettyHandler()
            .registerQueryHandler("GetFTPConfig", new PacketInQueryGetFTPConfig());
    }

    @Override
    public void onAddonReadyToClose() {
        ReformCloudController.getInstance().getNettyHandler()
            .unregisterQueryHandler("GetFTPConfig");
        this.backUpMaker.close();
    }
}
