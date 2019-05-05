/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import systems.reformcloud.configuration.DiscordConfig;
import systems.reformcloud.utility.ConsoleInputHandler;
import systems.reformcloud.utility.ConsoleWriter;
import systems.reformcloud.utility.ControllerAddonImpl;
import systems.reformcloud.utility.StringUtil;

import javax.security.auth.login.LoginException;
import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class DiscordAddon extends ControllerAddonImpl implements Serializable {
    private static final long serialVersionUID = 3321468728377410418L;

    private static DiscordAddon instance;

    private JDA jda;
    private DiscordConfig discordConfig;

    private TextChannel textChannel;

    private ConsoleWriter consoleWriter;

    public static DiscordAddon getInstance() {
        return DiscordAddon.instance;
    }

    @Override
    public void onAddonClazzPrepare() {
        instance = this;
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
    }

    @Override
    public void onAddonLoading() {
        consoleWriter = new ConsoleWriter();
        this.discordConfig = new DiscordConfig();

        final JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT)
                .setAudioEnabled(false)
                .setAutoReconnect(true)
                .setToken(this.discordConfig.getDiscordInformation().getToken())
                .addEventListeners(new ConsoleInputHandler())
                .setActivity(Activity.playing(this.discordConfig.getDiscordInformation().getGame()));

        try {
            this.jda = jdaBuilder.build().awaitReady();
            this.textChannel = jda.getTextChannelById(this.discordConfig.getDiscordInformation().getChannelID());
        } catch (final InterruptedException | LoginException ex) {
            StringUtil.printError(ReformCloudController.getInstance().getLoggerProvider(), "Error while startup of addon DiscordBot", ex);
        }
    }

    @Override
    public void onAddonReadyToClose() {
        if (jda != null) {
            this.jda.shutdownNow();
            this.jda = null;
        }
    }

    public JDA getJda() {
        return this.jda;
    }

    public DiscordConfig getDiscordConfig() {
        return this.discordConfig;
    }

    public TextChannel getTextChannel() {
        return this.textChannel;
    }

    public ConsoleWriter getConsoleWriter() {
        return this.consoleWriter;
    }
}
