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
            "Der angegebene Command konnte §cnicht§r als ein §einterner §roder §eexterner §rCommand gefunden werden. §eVerwende §3\"help\" §efür Hilfe.",
            "Lade Client §e[Name=%name%/Address=%ip%]§r...",
            "Lade ServerGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "Lade ProxyGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "ReformCloud wurde §aerfolgreich §rgeladen, dies dauerte §e%time%ms",
            "Versuche das §eCloudSystem§r neuzuladen, dies könnte einen kleinen Moment dauern...",
            "Der §eReload§r wurde §aerfolgreich§r durchgeführt",
            "Lösche ServerGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "Lösche ProxyGruppe §e[Name=%name%/Clients=%clients%]§r...",
            "Lösche Client §e[Name=%name%/IP=%ip%]§r...",
            "Das Icon der ProxyGruppe %group% ist nicht 64x64 Pixel groß. Bitte behebe den Fehler, denn sonst kann dein Icon nicht geladen werden",
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
            "Spieler §e[Name=%name%/UUID=%uuid%] §rführte den Command §6[Name=%command%] auf §3Proxy §e[Name=%proxy%/Server=%server%] §raus",
            "Der §eCommand §rwurde mit §ePackets §rausgeführt",
            "Log für den §3Prozess §e[Name=\"%name%\"]§r: §e%url%",
            "ServerProzess §e[Name=%uid%] §rwurde zur Warteschlange hinzugefügt",
            "ProxyProzess §e[Name=%uid%] §rwurde zur Warteschlange hinzugefügt",
            "Versuche den Prozess §e[Name=%name%] §czu stoppen§r...",
            "ReformCloud kopiert das §eTemplate§r in §e\"%path%\"§r, dies könnte einen kleinen Moment dauern...",
            "Warte auf Prozess §e[Name=%name%/UID=%uid%/Group=%group%/Service=%type%]§r zum §astarten...",
            "Channel §e[Address=%ip%/Port=%port%/Service=%name%]§r wurde §cgetrennt",
            "Du wurdest aus dem jetzigen §3Screen§r §crausgeworfen§r, da der zugehörige Prozess die Verbindung §cgetrennt§r hat",
            "Der Client §3%name%§r wurde§a erfolgreich§r neugeladen",
            "ReformCloud wurde §aerfolgreich§r auf §e%ip%:%port%§r gebunden",
            "Versuche %name% herunterzuladen...",
            "Der Download wurde erfolgreich abgeschlossen",
            "Es sind keine Addons geladen",
            "Die folgenden Addons sind geladen: ",
            "    - %name% | Version: %version% | Haupt-Klasse: %main%",
            "Die ServerGruppe existiert nicht",
            "Die ProxyGruppe existiert nicht",
            "Der Client existiert nicht",
            "Der Client ist nicht verbunden",
            "Der Prozess %name% ist nicht zum Controller verbunden",
            "Die Einstellung %name% wurde für die Gruppe %group% auf %value% erfolgreich geändert",
            "Du hast erfolgreich %value% von %group% entfernt",
            "Du hast erfolgreich %value% zu %group% hinzugefügt",
            "Die Einstellung kann mit den angegebenen Eintellungen nicht geändert werden: %reason%",
            "Der Client versucht das Template zu kopieren",
            "Das Template Backend muss der Client sein, um das Template zu kopieren",
            "Bitte gebe den Namen der Gruppe an",
            "Bitte gib den Client der Gruppe an",
            "Bitte wähle eine Minecraft Server Version",
            "Welche Spigot Version soll genutzt werden?",
            "Bitte wähle einen Reset Typ [\"LOBBY\", \"DYNAMIC\", \"STATIC\"]",
            "Bitte wähle einen Reset Typ [\"DYNAMIC\", \"STATIC\"]",
            "Versuche %group% zu erstellen...",
            "Welche BungeeCord Version soll genutzt werden?",
            "Bitte gib den Namen des neuen Clients an",
            "Bitte gib die IP-Addresse des Clients an",
            "Bitte gib die IP-Adresse des Controllers an",
            "Bitte gib den Namen des ersten Clients an",
            "Wie viel Ram soll die default Lobby Gruppe haben? (in MB)",
            "Wie viel Ram soll die default Proxy Gruppe haben? (in MB)",
            "Möchtest du die standard Addons herunterladen? (Du kannst diese "
                + "auch später herunterladen) [\"yes\" (Empfohlen), \"no\"]",
            "Der default WebUser \"administrator\" wurde mit dem Passwort \"%password%\" erstellt",
            "Ein Fehler ist aufgetreten: %message%",
            "Der WebUser %name% wurde mit dem Passwort \"%password%\" erstellt",
            "Das Template wurde erfolgreich erstellt",
            "Versuche das Template %template% von der Gruppe %group% von %client1% zu %client2% zu senden",
            "Aktiviere den Debug auf %name%...",
            "Deaktiviere den Debug auf %name%...",
            "Aktiviere den Standby Modus auf %name%...",
            "Deaktiviere den Standby Modus auf %name%...",
            "Der Befehl wurde ausgeführt",
            "Der Controller stoppt...",
            "Die folgenden %type% Gruppen sind registeriert: ",
            "Der Log für %type% wurde hochgeladen auf %url%",
            "Die Warteschlange wurde angefordert",
            "Versuche den Warteschlangen Eintrag %name% zu entfernen...",
            "Versuche einen Prozess zu starten...",
            "Es wurde kein Client gefunden der zum Controller verbunden ist, oder bereit ist einen Prozess zu starten",
            "Versuche %name% zu stoppen",
            "Du hast den Screen erfolgreich verlassen",
            "Die Berechtigung wurde erfolgreich entfernt",
            "Die Permission %perm% wurde mit der value %key% hinzugefügt",
            "Der Spieler %name% wurde zur whitelist des Proxies %proxy% hinzugefügt",
            "Der Spieler %name% wurde von der whitelist des Proxies %proxy% entfernt"
        );
    }
}
