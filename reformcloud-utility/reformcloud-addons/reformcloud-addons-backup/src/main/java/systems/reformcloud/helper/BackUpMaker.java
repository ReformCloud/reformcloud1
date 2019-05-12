/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.helper;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.backup.FTPConfig;
import systems.reformcloud.backup.util.FTPUtil;
import systems.reformcloud.network.out.PacketOutDisableBackup;
import systems.reformcloud.network.out.PacketOutEnableBackup;
import systems.reformcloud.utility.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 12.05.2019
 */

public final class BackUpMaker implements Serializable {
    private FTPConfig ftpConfig;
    private boolean deleted = false;

    private static BackUpMaker instance;

    public BackUpMaker(FTPConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    public void start() {
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
                new PacketOutEnableBackup(this.ftpConfig)
        );

        ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
            while (!deleted && !Thread.currentThread().isInterrupted()) {
                try {
                    if (this.ftpConfig.isSaveController()) {
                        FTPClient ftpClient = this.ftpConfig.isUseFTPS() ? new FTPSClient() : new FTPClient();
                        FTPUtil.openConnection(
                                ftpClient,
                                this.ftpConfig.getHost(),
                                this.ftpConfig.getPort(),
                                this.ftpConfig.getUserName(),
                                this.ftpConfig.getPassword()
                        );
                        FTPUtil.uploadDirectory(
                                ftpClient,
                                ClassLoader.getSystemClassLoader().getResource(".").getPath(),
                                this.ftpConfig.getExcluded()
                        );
                        FTPUtil.closeConnection(ftpClient);
                    }


                } catch (final IOException ex) {
                    StringUtil.printError(
                            ReformCloudController.getInstance().getLoggerProvider(),
                            "Error while opening ftp connection",
                            ex
                    );
                }

                ReformCloudLibraryService.sleep(TimeUnit.MINUTES, this.ftpConfig.getSaveIntervallInMinutes());
            }
        });
    }

    public void close() {
        this.deleted = true;
        ReformCloudController.getInstance().getChannelHandler().sendToAllSynchronized(
                new PacketOutDisableBackup()
        );
    }

    public static BackUpMaker getInstance() {
        return instance;
    }

    public FTPConfig getFtpConfig() {
        return ftpConfig;
    }
}
