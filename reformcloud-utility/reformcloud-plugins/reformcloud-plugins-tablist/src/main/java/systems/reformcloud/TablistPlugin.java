/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import org.bukkit.plugin.java.JavaPlugin;
import systems.reformcloud.listeners.JoinListener;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 25.03.2019
 */

public final class TablistPlugin extends JavaPlugin implements Serializable {
    public static TablistPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
    }
}
