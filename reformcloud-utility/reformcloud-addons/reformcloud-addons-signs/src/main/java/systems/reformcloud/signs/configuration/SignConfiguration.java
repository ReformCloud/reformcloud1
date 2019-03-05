/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.configuration;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.exceptions.LoadException;
import systems.reformcloud.signs.Sign;
import systems.reformcloud.signs.SignLayout;
import systems.reformcloud.signs.SignLayoutConfiguration;
import systems.reformcloud.signs.map.TemplateMap;
import systems.reformcloud.signs.netty.packets.PacketOutCreateSign;
import systems.reformcloud.signs.netty.packets.PacketOutRemoveSign;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.files.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

@Getter
public class SignConfiguration {
    private final Path path = Paths.get("layout.json");
    private Map<UUID, Sign> signMap = ReformCloudLibraryService.concurrentHashMap();
    private SignLayoutConfiguration signLayoutConfiguration;

    /**
     * Loads the SignConfiguration
     */
    public void loadAll() {
        if (!Files.exists(Paths.get("reformcloud/signs")))
            FileUtils.createDirectory(Paths.get("reformcloud/signs"));
        if (!Files.exists(Paths.get("reformcloud/database/signs")))
            FileUtils.createDirectory(Paths.get("reformcloud/database/signs"));

        if (!Files.exists(Paths.get("reformcloud/signs/" + path))) {
            new Configuration().addProperty("config", new SignLayoutConfiguration(
                    new SignLayout.GroupLayout(
                            new SignLayout(new String[]{"§8§m---------§r", "&c§lmaintenance", " ", "§8§m---------"}, "STAINED_CLAY", 4),
                            new SignLayout(new String[]{"%serve%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "STAINED_CLAY", 4),
                            new SignLayout(new String[]{"%serve%", "&6&lFULL", "%online_players%/%max_players%", "%motd%"}, "STAINED_CLAY", 4),
                            new SignLayout(new String[]{"%serve%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "STAINED_CLAY", 4)

                    ), ReformCloudLibraryService.concurrentHashMap(), Collections.singletonList(new TemplateMap<>("Lobby", "default", new SignLayout.TemplateLayout(
                    new SignLayout(new String[]{"%serve%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "STAINED_CLAY", 4),
                    new SignLayout(new String[]{"%serve%", "&6&lFULL", "%online_players%/%max_players%", "%motd%"}, "STAINED_CLAY", 4),
                    new SignLayout(new String[]{"%serve%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "STAINED_CLAY", 4)
            ))), new SignLayout.LoadingLayout(
                    4, 0,
                    new SignLayout[]{
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " l", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " lo", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " loa", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " load", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " loadi", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " loadin", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " loading", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " loadin", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " loadi", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " load", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " loa", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " lo", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " l", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " ", "§0§m-------------"}),
                            new SignLayout(new String[]{"§0§m-------------", "  Server", " ", "§0§m-------------"})
                    })
            )).write(Paths.get("reformcloud/signs/" + path));
        }

        this.signLayoutConfiguration = Configuration.parse(Paths.get("reformcloud/signs/" + path)).getValue("config", TypeTokenAdaptor.getSIGN_LAYOUT_CONFIG_TYPE());
        this.loadSigns();
    }

    /**
     * Creates a sign
     *
     * @param sign
     */
    public void addSign(final Sign sign) {
        this.signMap.put(sign.getUuid(), sign);

        ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutCreateSign(sign));

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/database/signs/database.json"));
        configuration.addProperty("signs", this.signMap.values());
        configuration.write(Paths.get("reformcloud/database/signs/database.json"));
    }

    /**
     * Deletes a sign
     *
     * @param sign
     */
    public void removeSign(final Sign sign) {
        this.signMap.remove(sign.getUuid());

        ReformCloudController.getInstance().getChannelHandler().sendToAllAsynchronous(new PacketOutRemoveSign(sign));

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/database/signs/database.json"));
        configuration.remove("signs");
        configuration.addProperty("signs", this.signMap.values());
        configuration.write(Paths.get("reformcloud/database/signs/database.json"));
    }

    /**
     * Loads all signs
     */
    public void loadSigns() {
        if (!Files.exists(Paths.get("reformcloud/database/signs/database.json")))
            new Configuration().addProperty("signs", new ArrayList<>()).write(Paths.get("reformcloud/database/signs/database.json"));

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/database/signs/database.json"));
        List<Sign> signs = configuration.getValue("signs", new TypeToken<List<Sign>>() {
        }.getType());

        if (signs == null) {
            StringUtil.printError(ReformCloudController.getInstance().getLoggerProvider(), "Could not load sign database", new LoadException(new IllegalArgumentException("Sign Database broken or not loadable")));
            return;
        }

        signs.forEach(e -> this.signMap.put(e.getUuid(), e));
    }
}
