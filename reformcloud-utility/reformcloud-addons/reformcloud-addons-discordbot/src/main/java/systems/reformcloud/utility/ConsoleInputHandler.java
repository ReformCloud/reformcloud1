/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.utility;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import systems.reformcloud.DiscordAddon;
import systems.reformcloud.ReformCloudController;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 10.02.2019
 */

public final class ConsoleInputHandler extends ListenerAdapter implements Serializable {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getChannel().getId().equals(DiscordAddon.getInstance().getTextChannel().getId())
                || event.getAuthor().isBot())
            return;

        if (ReformCloudController.getInstance().getCommandManager().dispatchCommand(event.getMessage().getContentRaw()))
            ReformCloudController.getInstance().getStatisticsProvider().addConsoleCommand();
    }
}
