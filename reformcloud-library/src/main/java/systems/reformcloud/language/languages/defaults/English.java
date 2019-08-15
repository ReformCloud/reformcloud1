/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.language.languages.defaults;

import systems.reformcloud.language.utility.Language;

import java.io.Serializable;

/**
 * @author _Klaro | Pasqual K. / created on 04.02.2019
 */

public final class English extends Language implements Serializable {

    private static final long serialVersionUID = 3190567310112749787L;

    public English() {
        super(
            "§eUse the command §3\"help\" §efor help.",
            "The input is §cnot §rrecognized as an §einternal§r or §eexternal§r command. Use §e\"help\" §rfor help",
            "Loading Client §e[Name=%name%/Address=%ip%]§r...",
            "Loading ServerGroup §e[Name=%name%/Clients=%clients%]§r...",
            "Loading ProxyGroup §e[Name=%name%/Clients=%clients%]§r...",
            "ReformCloud has been §asuccessfully §reloaded, took §e%time%ms",
            "Trying to reload the §eCloudSystem§r, this may take a long time...",
            "§eReloading§r was completed §asuccessfully",
            "Deleting ServerGroup §e[Name=%name%/Clients=%clients%]§r...",
            "Deleting ProxyGroup §e[Name=%name%/Clients=%clients%]§r...",
            "Deleting Client §e[Name=%name%/IP=%ip%]§r...",
            "The icon size of proxy §e%group%§r is not §e64x64 pixels§r. Please correct the mistake, otherwise the icon cannot be used",
            "§cA newer version of §aReformCloud §cis available",
            "Your version is §aup-to-date",
            "ServerProcess §e%name%§r was §cstopped",
            "ProxyProcess §e%name%§r was §cstopped",
            "Waiting for §etasks§r to §eclose§r...",
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
            "Player §e[Name=%name%/UUID=%uuid%] §rexecuted on §3Proxy §e[Name=%proxy%/Server=%server%] §rthe command §6[Name=%command%]",
            "The §ecommand§r was executed via §epackets",
            "Log for §3process §e[Name=\"%name%\"]§r: §e%url%",
            "§3ServerProcess §e[Name=%uid%] §rwas added to §3queue",
            "§3ProxyProcess §e[Name=%uid%] §rwas added to §3queue",
            "Trying to §cshutdown §rProcess §e[Name=%name%]§r...",
            "ReformCloud copies custom §eTemplate§r to §e\"%path%\"§r, this may take a long time...",
            "Waiting for Process §e[Name=%name%/UID=%uid%/Group=%group%/Service=%type%]§r to §astart",
            "Channel §e[Host=%ip%/Port=%port%/Service=%name%]§r is §cdisconnected",
            "You was §ckicked§r out of the current §3screen§r, because the process §cdisconnected§r",
            "The Client §3%name%§r was updated §asuccessfully",
            "ReformCloud is now §aready§r and listening on §e%ip%:%port%",
            "Trying to download §e%name%§r...",
            "Download was completed §asuccessfully",
            "§cThere are no addons loaded",
            "The following §eaddons§r are loaded: ",
            "    - §e%name%§r | Version: §e%version%§r | Main-Class: §e%main%",
            "§cThe servergroup doesn't exists",
            "§cThe proxygroup doesn't exists",
            "§cThe client doesn't exists",
            "§cThe client isn't connected",
            "§cThe process §e%name%§c isn't connected to the controller",
            "The setting §e%name%§r was updated §asuccessfully§r for the group §e%group%§r to §e%value%",
            "You removed §asuccessfully §e%value%§r from §e%group%",
            "You added §asuccessfully §e%value%§r to §e%group%",
            "The setting is not updatable with the given settings: §c%reason%",
            "The client tries to copy the template",
            "§cYou can't copy a server if the template backend is not the client.",
            "Please enter the §3name§r of the group",
            "Please enter the §3client§r of the group",
            "Please choose a §3minecraft server version",
            "Which §3Spigot-Version§r should be used?",
            "Please choose a reset type [\"§eLOBBY§r\", \"§eDYNAMIC§r\", \"§eSTATIC§r\"]",
            "Please choose a proxy reset type [\"§eDYNAMIC§r\", \"§eSTATIC§r\"]",
            "Trying to create §e%group%§r...",
            "Which §3Bungeecord-Version§r should be used?",
            "Please enter the §3name§r of the client",
            "Please enter the ip §3address§r of the client",
            "Please provide the ip §3address§r of the controller",
            "Please enter the first Client §3name",
            "How many mb §3ram§r should the default lobby group have?",
            "How many mb §3ram§r should the default proxy group have?",
            "Do you want to load the default addons (You can download them later, too) [\"§ayes§r\" (recommended), \"§cno§r\"]",
            "The default §3WebUser \"§eadministrator§r\" was created with the password \"§e%password%§r\"",
            "An error occurred: §c%message%",
            "The web user §e%name%§r was §asuccessfully§r created with the password: \"§e%password%§r\"",
            "The template was created §asuccessfully",
            "Trying to deploy the template §e%template%§r of group §e%group%§r from §e%client1%§r to §e%client2%§r",
            "Enabling the §3debug§r on §e%name%§r...",
            "Disabling the §3debug§r on §e%name%§r...",
            "Enabling the §3standby§r mode on §e%name%§r...",
            "Disabling the §3standby§r mode on §e%name%§r...",
            "The command has been executed",
            "The controller will §cstop§r...",
            "The following §e%type%§r groups are registered: ",
            "The log for §e%type%§r was uploaded to §e%url%",
            "The queue was requested",
            "Trying to remove the queue entry §e%name%",
            "Trying to startup a process",
            "§cThe Client of the ServerGroup isn't connected to ReformCloudController or Client is not available to startup processes",
            "Trying to stop §e%name%",
            "You left the screen session §asuccessfully",
            "The permission was removed §asuccessfully",
            "The permission §e%perm%§r was added with the value §e%key%",
            "The player §e%name%§r was added to §e%proxy%§r whitelist",
            "The player §e%name%§r was removed from §e%proxy%§r whitelist"
        );
    }
}
