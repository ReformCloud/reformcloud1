/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import org.bukkit.plugin.java.JavaPlugin;
import systems.reformcloud.listeners.ChatListener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.03.2019
 */

public final class ReformAddonChat extends JavaPlugin implements Serializable {
    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }
}
