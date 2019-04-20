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
                "Deleting ServerGroup §e[Name=%name%/Clients=%clients%]§r...",
                "Deleting ProxyGroup §e[Name=%name%/Clients=%clients%]§r...",
                "Deleting Client §e[Name=%name%/IP=%ip%]§r...",
                "The icon size of proxy %group% is not 64x64 pixels. Please correct the mistake, otherwise the icon cannot be used",
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
                "Player §e[Name=%name%/UUID=%uuid%] §rexecuted on §3Proxy §e[Name=%proxy%/Server=%server%] §rthe command §6[Name=%command%]",
                "The following §ecommand §r was executed via §epacket",
                "Log for §3process §e[Name=\"%name%\"]§r: §e%url%",
                "ServerProcess §rwas added to queue: §e%uid%",
                "ProxyProcess §rwas added to queue: §e%uid%",
                "Trying to §cshutdown §rProcess §e[Name=%name%]§r...",
                "ReformCloud copies custom §eTemplate§r to §e\"%path%\"§r, this may take a long time...",
                "Waiting for Process §e[Name=%name%/UID=%uid%/Group=%group%/Service=%type%]§r to §astart",
                "Channel §e[Host=%ip%/Port=%port%/Service=%name%]§r is §cdisconnected",
                "You was §ckicked§r out of the current §3screen§r, because the process §cdisconnected§r",
                "The Client §3%name%§r was updated §asuccessfully",
                "ReformCloud is now §aready§r and listening on §e%ip%:%port%",
                "Trying to download %name%...",
                "Download was completed successfully",
                "There are no addons loaded",
                "The following addons are loaded: ",
                "    - %name% | Version: %version% | Main-Class: %main%",
                "The servergroup doesn't exists",
                "The proxygroup doesn't exists",
                "The client doesn't exists",
                "The client isn't connected",
                "The process %name% isn't connected to the controller",
                "The setting %name% was updated for the group %group% to %value%",
                "You removed successfully %value% from %group%",
                "You added successfully %value% to %group%",
                "The setting is nit updatable with the given settings: %reason%",
                "The client tries to copy the template",
                "You can't copy a server if the template backend is not the client.",
                "Please enter the name of the group",
                "Please enter the client of the group",
                "Please choose a minecraft version",
                "Which Spigot-Version should be used?",
                "Please choose a reset type [\"LOBBY\", \"DYNAMIC\", \"STATIC\"]",
                "Please choose a proxy reset type [\"DYNAMIC\", \"STATIC\"]",
                "Trying to create %group%...",
                "Which Bungeecord-Version should be used?",
                "Please enter the name of the client",
                "Please enter the ip address of the client",
                "Please provide the ip address of the controller",
                "Please enter the first Client name",
                "How many mb ram should the default lobby group have?",
                "How many mb ram should the default proxy group have?",
                "Do you want to load the default addons (You can download them later, too) [\"yes\" (recommended), \"no\"]",
                "The default WebUser \"administrator\" was created with the password \"%password%\"",
                "An error occurred: %message%",
                "The web user %name% was successfully created with the password: \"%password%\"",
                "The template was created successfully",
                "Trying to deploy the template %template% of group %group% from %client1% to %client2%",
                "Enabling the debug on %name%...",
                "Disabling the debug on %name%...",
                "Enabling the standby mode on %name%...",
                "Disabling the standby mode on %name%...",
                "The command has been executed",
                "The controller will stop",
                "The following %type% groups are registered",
                "The log for %type% was uploaded to %url%",
                "The queue was requested",
                "Trying to remove the queue entry %name%",
                "Trying to startup a process",
                "The Client of the ServerGroup isn't connected to ReformCloudController or Client is not available to startup processes",
                "Trying to stop %name%",
                "You left the screen session successfully",
                "The permission was removed successfully",
                "The permission %perm% was added with the value %key%",
                "The player %name% was added to %proxy% whitelist",
                "The player %name% was removed from %proxy% whitelist"
        );
    }
}
