/*
  Copyright © 2018 Pasqual K. | All rights reserved
 */

package systems.reformcloud.signs.configuration;

import com.google.gson.reflect.TypeToken;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import systems.reformcloud.signs.netty.packets.PacketOutSignUpdate;
import systems.reformcloud.utility.StringUtil;
import systems.reformcloud.utility.TypeTokenAdaptor;
import systems.reformcloud.utility.files.FileUtils;

/**
 * @author _Klaro | Pasqual K. / created on 12.12.2018
 */

public class SignConfiguration {
    private final Path path = Paths.get("layout.json");
    private Map<UUID, Sign> signMap = ReformCloudLibraryService.concurrentHashMap();
    private SignLayoutConfiguration signLayoutConfiguration;

    /**
     * Loads the SignConfiguration
     */
    public void loadAll() {
        if (!Files.exists(Paths.get("reformcloud/addons/signs")))
            FileUtils.createDirectory(Paths.get("reformcloud/addons/signs"));
        if (!Files.exists(Paths.get("reformcloud/database/signs")))
            FileUtils.createDirectory(Paths.get("reformcloud/database/signs"));

        if (!Files.exists(Paths.get("reformcloud/addons/signs/" + path))) {
            new Configuration().addValue("config", new SignLayoutConfiguration(
                    new SignLayout.GroupLayout(
                            new SignLayout(new String[]{"§8§m---------§r", "&c§lmaintenance", " ", "§8§m---------"}, "REDSTONE_BLOCK", 0),
                            new SignLayout(new String[]{"%server%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "GOLD_BLOCK", 0),
                            new SignLayout(new String[]{"%server%", "&6&lFULL", "%online_players%/%max_players%", "%motd%"}, "DIAMOND_BLOCK", 0),
                            new SignLayout(new String[]{"%server%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "EMERALD_BLOCK", 0)

                    ), ReformCloudLibraryService.concurrentHashMap(),
                    Collections.singletonList(new TemplateMap<>("Lobby", "default", new SignLayout.TemplateLayout(
                            new SignLayout(new String[]{"%server%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "GOLD_BLOCK", 0),
                            new SignLayout(new String[]{"%server%", "&6&lFULL", "%online_players%/%max_players%", "%motd%"}, "DIAMOND_BLOCK", 0),
                            new SignLayout(new String[]{"%server%", "&6&l%client%", "%online_players%/%max_players%", "%motd%"}, "EMERALD_BLOCK", 0)
                    ))), new SignLayout.LoadingLayout(4, 0,
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
            )).write(Paths.get("reformcloud/addons/signs/" + path));
        }

        this.signLayoutConfiguration = Configuration.parse(Paths.get("reformcloud/addons/signs/" + path)).getValue("config", TypeTokenAdaptor.getSIGN_LAYOUT_CONFIG_TYPE());
        this.loadSigns();
        this.update();
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
        configuration.addValue("signs", this.signMap.values());
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
        configuration.addValue("signs", this.signMap.values());
        configuration.write(Paths.get("reformcloud/database/signs/database.json"));
    }

    /**
     * Loads all signs
     */
    private void loadSigns() {
        if (!Files.exists(Paths.get("reformcloud/database/signs/database.json")))
            new Configuration().addValue("signs", new ArrayList<>()).write(Paths.get("reformcloud/database/signs/database.json"));

        Configuration configuration = Configuration.parse(Paths.get("reformcloud/database/signs/database.json"));
        List<Sign> signs = configuration.getValue("signs", new TypeToken<List<Sign>>() {
        }.getType());

        if (signs == null) {
            StringUtil.printError(ReformCloudController.getInstance().getLoggerProvider(), "Could not load sign database", new LoadException(new IllegalArgumentException("Sign Database broken or not loadable")));
            return;
        }

        signs.forEach(e -> this.signMap.put(e.getUuid(), e));
    }

    private void update() {
        ReformCloudController.getInstance().getAllRegisteredServers().forEach(serverInfo -> ReformCloudController.getInstance()
                .getChannelHandler().sendPacketSynchronized(serverInfo.getCloudProcess().getName(),
                        new PacketOutSignUpdate(this.signLayoutConfiguration, this.signMap)
                ));
    }

    public Path getPath() {
        return this.path;
    }

    public Map<UUID, Sign> getSignMap() {
        return this.signMap;
    }

    public SignLayoutConfiguration getSignLayoutConfiguration() {
        return this.signLayoutConfiguration;
    }
}
