/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.configuration;

import com.google.gson.reflect.TypeToken;
import systems.reformcloud.ReformCloudController;
import systems.reformcloud.configurations.Configuration;
import systems.reformcloud.meta.proxy.settings.ProxySettings;
import systems.reformcloud.network.in.PacketInGetProxyConfig;
import systems.reformcloud.network.out.PacketOutUpdateProxyConfig;
import systems.reformcloud.utility.files.FileUtils;
import systems.reformcloud.utility.map.maps.Double;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author _Klaro | Pasqual K. / created on 06.04.2019
 */

public final class ProxyAddonConfiguration implements Serializable {
    private List<ProxySettings> proxySettings = new ArrayList<>();

    public ProxyAddonConfiguration() {
        if (!Files.exists(Paths.get("reformcloud/proxy/configs.json"))) {
            FileUtils.createDirectory(Paths.get("reformcloud/proxy"));
            new Configuration().addProperty("config", Collections.singletonList(
                    new ProxySettings(
                            ReformCloudController.getInstance().getAllProxyGroups().parallelStream().findFirst().get().getName(),
                            "§2Reform§fCloud §8● §a§lOfficial Cloud System §8» §7%current_server% §8● §7%current_proxy% \n §7Online count §8» " +
                                    "§a%online_players%§8/§a%max_players_current%§8/§a%max_players_global%",
                            "§7Discord §8» https://discord.gg/uskXdVZ §8● §7Twitter §8» §a@ReformCloud \n §2Reform§fCloud " +
                                    "§8by §a_Klaro §8» §7@k_klaro",
                            "§4§lMaintenance §8● §c§l✘",
                            "§2§lReform§f§lCloud §8● §7§l%online_players%§8§l/§7§l%max_players_global%",
                            new String[]{" ", "§7powered by §2Reform§fCloud §8● §a§lOfficial Cloud System", " "},
                            true,
                            true,
                            true,
                            false,
                            1,
                            Collections.singletonList(new Double<>(
                                    "§2Reform§fCloud §8● §a§lOfficial Cloud System §7by §a_Klaro",
                                    "§7Discord §8» https://discord.gg/uskXdVZ §8● §7Twitter §8» §a@ReformCloud"
                            )),
                            Collections.singletonList(new Double<>(
                                    "§m§2Reform§fCloud §8● §a§lOfficial Cloud System §7by §a_Klaro",
                                    "§7Discord §8» https://discord.gg/uskXdVZ §8● §7Twitter §8» §a@ReformCloud"
                            ))
                    )
            )).write(Paths.get("reformcloud/proxy/configs.json"));
        }

        this.proxySettings = Configuration.parse(Paths.get("reformcloud/proxy/configs.json"))
                .getValue("config", new TypeToken<List<ProxySettings>>() {
                }.getType());

        ReformCloudController.getInstance().getNettyHandler().registerQueryHandler(
                "GetProxyConfig", new PacketInGetProxyConfig()
        );
    }

    public Optional<ProxySettings> getForProxy(String name) {
        return this.proxySettings.stream().filter(e -> e.getTargetProxyGroup().equals(name)).findFirst();
    }

    public void reload() {
        this.proxySettings = Configuration.parse(Paths.get("reformcloud/proxy/configs.json"))
                .getValue("config", new TypeToken<List<ProxySettings>>() {
                }.getType());

        this.proxySettings.forEach(e -> {
            ReformCloudController.getInstance()
                    .getInternalCloudNetwork()
                    .getServerProcessManager()
                    .getAllRegisteredProxyProcesses()
                    .stream()
                    .filter(e1 -> e1.getGroup().equals(e.getTargetProxyGroup()))
                    .forEach(e2 -> ReformCloudController.getInstance().sendPacketSync(
                            e2.getCloudProcess().getName(),
                            new PacketOutUpdateProxyConfig(Optional.of(e))
                    ));
        });
    }
}
