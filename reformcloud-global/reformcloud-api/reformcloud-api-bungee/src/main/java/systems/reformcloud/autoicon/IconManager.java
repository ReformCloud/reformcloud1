/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.autoicon;

import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.Favicon;
import systems.reformcloud.ReformCloudAPIBungee;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.network.packet.DefaultPacket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author _Klaro | Pasqual K. / created on 23.04.2019
 */

public final class IconManager implements Serializable {

    private static IconManager instance;

    private IconData iconData;

    private Favicon current;

    private List<Favicon> favicons = new ArrayList<>();

    private volatile boolean running = true;

    public IconManager() {
        if (instance != null) {
            return;
        }

        instance = this;

        ReformCloudAPIBungee.getInstance().getChannelHandler().sendPacketQuerySync(
            "ReformCloudController",
            ReformCloudAPIBungee.getInstance().getProxyInfo().getCloudProcess().getName(),
            new DefaultPacket(
                "GetConfig", new Configuration()
            ),
            (configuration, resultID) -> {
                this.iconData = configuration.getValue("data", new TypeToken<IconData>() {
                });
                if (this.iconData.getIcons().size() > 1) {
                    for (byte[] bytes : this.iconData.getIcons()) {
                        try {
                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                                bytes);
                            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                            byteArrayInputStream.close();
                            favicons.add(Favicon.create(bufferedImage));
                        } catch (final IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    ReformCloudLibraryService.EXECUTOR_SERVICE.execute(() -> {
                        while (!Thread.currentThread().isInterrupted() && running) {
                            current = this.favicons.get(
                                ReformCloudLibraryService.THREAD_LOCAL_RANDOM
                                    .nextInt(this.favicons.size()));
                            ReformCloudLibraryService
                                .sleep(TimeUnit.SECONDS, iconData.getUpdateTimeInSeconds());
                        }
                    });
                } else if (this.iconData.getIcons().size() == 1) {
                    try {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                            this.iconData.getIcons().get(0));
                        BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
                        byteArrayInputStream.close();
                        this.current = Favicon.create(bufferedImage);
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            },
            (configuration, resultID) -> instance = null
        );
    }

    public static IconManager getInstance() {
        return IconManager.instance;
    }

    public void delete() {
        instance = null;
        this.running = false;
        this.current = null;
    }

    public Favicon getCurrent() {
        return this.current;
    }
}
