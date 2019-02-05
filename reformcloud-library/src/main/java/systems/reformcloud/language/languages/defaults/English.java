/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.language.languages.defaults;

import lombok.Getter;
import systems.reformcloud.language.utility.Language;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 04.02.2019
 */

@Getter
public final class English extends Language implements Serializable {
    private static final long serialVersionUID = 3190567310112749787L;

    public English() {
        super(
                "§eUse the command §3\"help\" §efor help.",
                "The input is §cnot §rrecognized §eas an internal or external command. Use §e\"help\" §rfor help",
                "Loading Client §e[Name=%name%/Address=%ip%]§r...",
                "Loading ServerGroup §e[Name=%name%/Clients=%clients%]§r...",
                "Loading ProxyGroup §e[Name=%name%/Clients=%clients%]§r...",
                "ReformCloud has been §asuccessfully §rloaded, took §e%time%ms",
                "Trying to reload the §eCloudSystem§r, this may take a long time...",
                "§eReloading§r was completed §asuccessfully",
                "§cA newer version of §aReformCloud §cis available",
                "Your version is §aup-to-date",
                "ServerProcess §e%name%§r was §cstopped",
                "ProxyProcess §e%name%§r was §cstopped",
                "Waiting for tasks to §eclose§r...",
                "Addon §e[Name=%name%/Version=%version%]§r was §aprepared",
                "Addon §e[Name=%name%/Version=%version%]§r was §aenabled",
                "Addon §e[Name=%name%/Version=%version%]§r was §cclosed",
                "§aSuccessfully §rbound WebServer §e@%ip%:%port%",
                "ReformCloud §ccould not §rbind on §e%ip%:%port",
                "Channel §e[Address=%ip%/Port=%port%]§a connected",
                "Channel §e[Address=%ip%/Port=%port%]§r got §cdisconnected",
                "Process §e[Name=%name%/UID=%uid%]§r was §astarted §ron Client §3%client%",
                "Process §e[Name=%name%/UID=%uid%]§r was §cstopped §ron Client §3%client%",
                "Service §e[Name=%name%] §ris now §aready",
                "Player §e[Name=%name%/UUID=%uuid%] §rexecuted on §3Proxy §e[Name=%proxy%] §rthe command §6[Name=%command%]",
                "The following §ecommand §r was executed via §epacket",
                "Log for §3process §e[Name=\"%name%\"]§r: §e%url%",
                "§eServerProcess §rwas added to queue: §e%uid%",
                "§eProxyProcess §rwas added to queue: §e%uid%",
                "Trying to §cshutdown §rProcess §e[Name=%name%]§r...",
                "ReformCloud copies custom §eTemplate§r to §e\"%path%\"§r, this may take a long time...",
                "Waiting for Process §e[Name=%name%/UID=%uid%/Group=%group%/Service=%type%]§r to §astart",
                "Channel §e[Host=%ip%/Port=%port%]§r is §cdisconnected"
        );
    }
}
