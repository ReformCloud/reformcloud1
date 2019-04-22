/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.selector;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.ReformCloudLibraryService;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.exceptions.InstanceAlreadyExistsException;
import systems.reformcloud.mobs.SelectorMob;
import systems.reformcloud.mobs.config.SelectorMobConfig;
import systems.reformcloud.mobs.inventory.SelectorMobInventory;
import systems.reformcloud.mobs.inventory.SelectorMobInventoryItem;
import systems.reformcloud.mobs.inventory.item.SelectorsMobServerItem;
import systems.reformcloud.packets.in.PacketInCreateMob;
import systems.reformcloud.packets.in.PacketInDeleteMob;
import systems.reformcloud.packets.in.PacketInQueryGetAll;
import systems.reformcloud.packets.out.*;
import systems.reformcloud.utility.Require;
import systems.reformcloud.utility.files.FileUtils;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author _Klaro | Pasqual K. / created on 21.04.2019
 */

@Getter
public final class MobSelector implements Serializable {
    @Getter
    private static MobSelector instance;

    private final String directory = "reformcloud/addons/mobs",
            databaseDir = "reformcloud/database/mobs";

    private Map<UUID, SelectorMob> mobs = new HashMap<>();
    private SelectorMobConfig selectorMobConfig;

    public MobSelector() {
        if (instance != null)
            throw new InstanceAlreadyExistsException();

        instance = this;

        this.defaultInit();
        this.registerNetworkHandlers();
        this.reload();
    }

    private void defaultInit() {
        if (!Files.exists(Paths.get(directory))) {
            FileUtils.createDirectory(Paths.get(directory));
            FileUtils.createDirectory(Paths.get(databaseDir));
        }
    }

    public void reload() {
        if (!Files.exists(Paths.get(directory + "/config.json"))) {
            new Configuration().addProperty("config", new SelectorMobConfig(
                    new SelectorMobInventory(
                            "§7» §a%group_name%",
                            54,
                            Arrays.asList(
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 0, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 1, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 2, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 3, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 4, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 5, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 6, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 7, (short) 0),
                                    new SelectorMobInventoryItem(" ", "GLASS_PANE", 8, (short) 0)
                            )
                    ), new SelectorsMobServerItem("§a%server_name%", "CAKE",
                    Collections.singletonList("§7%server_online_players%§8/§7%server_max_players%"), (short) 0)
            )).write(Paths.get(directory + "/config.json"));

            if (!Files.exists(Paths.get(databaseDir + "/database.json")))
                new Configuration()
                        .addProperty("database", new HashMap<>())
                        .write(databaseDir + "/database.json");
        }

        this.mobs = Configuration.parse(databaseDir + "/database.json").getValue("database", new TypeToken<Map<UUID, SelectorMob>>() {
        }.getType());
        this.selectorMobConfig = Configuration.parse(directory + "/config.json").getValue("config", new TypeToken<SelectorMobConfig>() {
        }.getType());

        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
                ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
                new PacketOutEnableMobs()
        );
        ReformCloudLibraryService.sleep(500);
        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
                ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
                new PacketOutUpdateMobs(this.mobs, this.selectorMobConfig)
        );
    }

    public void saveDatabase() {
        new Configuration()
                .addProperty("database", this.mobs == null ? new HashMap<>() : this.mobs)
                .write(databaseDir + "/database.json");
    }

    private void registerNetworkHandlers() {
        ReformCloudController.getInstance().getNettyHandler().registerQueryHandler("RequestAll", new PacketInQueryGetAll());

        ReformCloudController.getInstance().getNettyHandler().registerHandler("DeleteMob", new PacketInDeleteMob());
        ReformCloudController.getInstance().getNettyHandler().registerHandler("CreateMob", new PacketInCreateMob());
    }

    public void close() {
        ReformCloudController.getInstance().getNettyHandler().unregisterQueryHandler("RequestAll");

        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("DeleteMob");
        ReformCloudController.getInstance().getNettyHandler().unregisterHandler("CreateMob");

        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
                ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
                new PacketOutDisableMobs()
        );
        this.saveDatabase();
    }

    public void createMob(SelectorMob selectorMob) {
        Require.requireNotNull(selectorMob);
        this.mobs.put(selectorMob.getUniqueID(), selectorMob);
        ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
                ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
                new PacketOutCreateMob(selectorMob)
        );
        this.saveDatabase();
    }

    public void deleteMob(UUID selectorMob) {
        Require.requireNotNull(selectorMob);
        SelectorMob selectorMob1 = this.mobs.remove(selectorMob);
        if (selectorMob1 != null)
            ReformCloudController.getInstance().getChannelHandler().sendToAllLobbiesDirect(
                    ReformCloudController.getInstance().getInternalCloudNetwork().getServerProcessManager(),
                    new PacketOutDeleteMob(selectorMob1)
            );

        this.saveDatabase();
    }

    public SelectorMob getMob(UUID uuid) {
        return this.mobs.get(uuid);
    }
}
