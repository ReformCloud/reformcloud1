/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.utility.files.FileUtils;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class DiscordConfig implements Serializable {
    private DiscordInformation discordInformation;

    public DiscordConfig() {
        if (!Files.exists(Paths.get("reformcloud/addons/discord/config.json"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/addons/discord"));
            new Configuration()
                    .addValue("config", new DiscordInformation(
                            "NTQ0MTIyMTMwMzEzMjQ4NzY5.D0Ggvg.eMhB10edmYVmYo1-zg_u2nUNsD0",
                            "535909711178891279",
                            "ReformCloud - The official CloudSystem"
                    )).write(Paths.get("reformcloud/addons/discord/config.json"));
        }

        this.discordInformation = Configuration.parse(Paths.get("reformcloud/addons/discord/config.json"))
                .getValue("config", new TypeToken<DiscordInformation>() {
                }.getType());
    }

    public DiscordInformation getDiscordInformation() {
        return this.discordInformation;
    }
}
