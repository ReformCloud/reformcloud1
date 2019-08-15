/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.language.languages.defaults;

import java.io.Serializable;
import systems.reformcloud.language.utility.Language;

/**
 * @author _Klaro | Pasqual K. / created on 06.02.2019
 */

public final class German extends Language implements Serializable {

    private static final long serialVersionUID = 6124860795431209981L;

    public German() {
        super(
            "§eVerwende den Command §3\"help\" §efür Hilfe.",
            "Der angegebene Command konnte §cnicht§r als ein §einterner §roder §eexterner §rCommand anerkannt werden. §eVerwende §3\"help\" §efür Hilfe.",
            "Lade Client §e[Name=%name%/Address=%ip%]§r...",
            "Lade ServerGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "Lade ProxyGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "ReformCloud wurde §aerfolgreich §rgeladen, dies dauerte §e%time%ms",
            "Versuche das §eCloudSystem§r neuzuladen, dies könnte einen kleinen Moment dauern...",
            "Der §eReload§r wurde §aerfolgreich§r durchgeführt",
            "Lösche ServerGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "Lösche ProxyGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "Lösche Client §e[Name=%name%/IP=%ip%]§r...",
            "Das Icon der ProxyGruppe §e%group%§r ist nicht §e64x64 Pixel§r groß. Bitte behebe den Fehler, denn sonst kann dein Icon nicht geladen werden",
            "§cEine neue Version von §aReformCloud §cist verfügbar",
            "Deine Version ist §aauf dem neustem Stand",
            "ServerProzess §e%name%§r wurde §cgestoppt",
            "ProxyProzess §e%name%§r wurde §cgestoppt",
            "Warten auf das §cSchließen§r von §eAufgaben§r...",
            "Addon §e[Name=%name%/Version=%version%]§r wurde §avorbereitet",
            "Addon §e[Name=%name%/Version=%version%]§r wurde §ageladen",
            "Addon §e[Name=%name%/Version=%version%]§r wurde §cgestoppt",
            "§aErfolgreich §rgebundener WebServer auf §e@%ip%:%port%",
            "ReformCloud §eWebServer §rkonnte §cnicht §rauf §e%ip%:%port§r gebunden werden",
            "Channel §e[Address=%ip%/Port=%port%]§a verbunden",
            "Channel §e[Address=%ip%/Port=%port%]§r wurde §cgetrennt",
            "Prozess §e[Name=%name%/UID=%uid%]§r wurde §agestartet §rauf Client §3%client%",
            "Prozess §e[Name=%name%/UID=%uid%]§r wurde §cgestoppt §rauf Client §3%client%",
            "Service §e[Name=%name%] §rist nun §abereit",
            "Spieler §e[Name=%name%/UUID=%uuid%] §rführte den Command §6[Name=%command%]§r auf §3Proxy §e[Name=%proxy%/Server=%server%] §raus",
            "Der §eCommand §rwurde mit §ePackets §rausgeführt",
            "Log für den §3Prozess §e[Name=\"%name%\"]§r: §e%url%",
            "§3ServerProzess §e[Name=%uid%] §rwurde zur §3Warteschlange§r hinzugefügt",
            "§3ProxyProzess §e[Name=%uid%] §rwurde zur §3Warteschlange§r hinzugefügt",
            "Versuche den Prozess §e[Name=%name%] §czu stoppen§r...",
            "ReformCloud kopiert das §eTemplate§r in §e\"%path%\"§r, dies könnte einen kleinen Moment dauern...",
            "Warte auf Prozess §e[Name=%name%/UID=%uid%/Group=%group%/Service=%type%]§r zum §astarten...",
            "Channel §e[Address=%ip%/Port=%port%/Service=%name%]§r wurde §cgetrennt",
            "Du wurdest aus dem jetzigen §3Screen§r §crausgeworfen§r, da der zugehörige Prozess die Verbindung §cgetrennt§r hat",
            "Der Client §3%name%§r wurde§a erfolgreich§r neugeladen",
            "ReformCloud wurde §aerfolgreich§r auf §e%ip%:%port%§r gebunden",
            "Versuche §e%name%§r herunterzuladen...",
            "Der Download wurde §aerfolgreich§r abgeschlossen",
            "§cEs sind keine Addons geladen",
            "Die folgenden §eAddons§r sind geladen: ",
            "    - §e%name%§r | Version: §e%version%§r | Haupt-Klasse: §e%main%",
            "§cDie ServerGruppe existiert nicht",
            "§cDie ProxyGruppe existiert nicht",
            "§cDer Client existiert nicht",
            "§cDer Client ist nicht verbunden",
            "§cDer Prozess §e%name%§c ist nicht zum Controller verbunden",
            "Die Einstellung §e%name%§r wurde für die Gruppe §e%group%§r auf §e%value% §aerfolgreich§r geändert",
            "Du hast §aerfolgreich §e%value%§r von §e%group%§r entfernt",
            "Du hast §aerfolgreich §e%value%§r zu §e%group%§r hinzugefügt",
            "Die Einstellung kann mit den angegebenen Eintellungen nicht geändert werden: §c%reason%",
            "Der Client versucht das Template zu kopieren",
            "§cDas Template Backend muss der Client sein, um das Template zu kopieren",
            "Bitte gebe den §3Namen§r der Gruppe an",
            "Bitte gib den §3Client§r der Gruppe an",
            "Bitte wähle eine §3Minecraft Server Version",
            "Welche §3Spigot Version§r soll genutzt werden?",
            "Bitte wähle einen Reset Typ [\"§eLOBBY§r\", \"§eDYNAMIC§r\", \"§eSTATIC§r\"]",
            "Bitte wähle einen Reset Typ [\"§eDYNAMIC§r\", \"§eSTATIC§r\"]",
            "Versuche §e%group%§r zu erstellen...",
            "Welche §3BungeeCord Version§r soll genutzt werden?",
            "Bitte gib den Namen des neuen Clients an",
            "Bitte gib die §3IP-Adresse§r des Clients an",
            "Bitte gib die §3IP-Adresse§r des Controllers an",
            "Bitte gib den §3Namen§r des ersten Clients an",
            "Wie viel MB §3Ram§r soll die standard Lobby Gruppe haben?",
            "Wie viel MB §3Ram§r soll die standard Proxy Gruppe haben?",
            "Möchtest du die standard Addons herunterladen? (Du kannst diese auch später herunterladen) [\"yes\" (Empfohlen), \"no\"]",
            "Der standard WebUser \"§eadministrator§r\" wurde mit dem Passwort \"§e%password%§r\" erstellt",
            "Ein Fehler ist aufgetreten: §c%message%",
            "Der WebUser §e%name%§r wurde mit dem Passwort \"§e%password%§r\" erstellt",
            "Das Template wurde §aerfolgreich§r erstellt",
            "Versuche das Template §e%template%§r von der Gruppe §e%group%§r von §e%client1%§r zu §e%client2%§r zu senden",
            "Aktiviere den §3Debug§r auf §e%name%§r...",
            "Deaktiviere den §3Debug§r auf §e%name%§r...",
            "Aktiviere den §3Standby§r Modus auf §e%name%§r...",
            "Deaktiviere den §3Standby§r Modus auf §e%name%§r...",
            "Der Befehl wurde ausgeführt",
            "Der Controller §cstoppt§r...",
            "Die folgenden §e%type%§r Gruppen sind registriert: ",
            "Der Log für §e%type%§r wurde hochgeladen auf §e%url%§r",
            "Die Warteschlange wurde angefordert",
            "Versuche den Warteschlangen Eintrag §e%name%§r zu entfernen...",
            "Versuche einen Prozess zu starten...",
            "§cEs wurde kein Client gefunden der zum Controller verbunden ist, oder bereit ist einen Prozess zu starten",
            "Versuche §e%name%§r zu stoppen",
            "Du hast den Screen §aerfolgreich§r verlassen",
            "Die Berechtigung wurde §aerfolgreich§r entfernt",
            "Die Permission §e%perm%§r wurde mit der value §e%key%§r hinzugefügt",
            "Der Spieler §e%name%§r wurde zur whitelist des Proxies §e%proxy%§r hinzugefügt",
            "Der Spieler §e%name%§r wurde von der whitelist des Proxies §e%proxy%§r entfernt"
        );
    }
}
