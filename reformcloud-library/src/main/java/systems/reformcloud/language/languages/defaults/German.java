/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.language.languages.defaults;

import systems.reformcloud.language.utility.Language;

import java.io.Serializable;

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
                "Versuche den Prozess §e[Name=%name%] §czustoppen§r...",
                "ReformCloud kopiert das §eTemplate§r in §e\"%path%\"§r, dies könnte einen kleinen Moment dauern...",
                "Warte auf Prozess §e[Name=%name%/UID=%uid%/Group=%group%/Service=%type%]§r zum §astarten...",
                "Channel §e[Address=%ip%/Port=%port%/Service=%name%]§r wurde §cgetrennt",
                "Du wurdest aus dem jetzigen §3Screen§r §crausgeworfen§r, da der zugehörige Prozess die Verbindung §cgetrennt§r hat",
                "Der Client §3%name%§r wurde§a erfolgreich§r neugeladen",
                "ReformCloud wurde §aerfolgreich§r auf §e%ip%:%port%§r gebunden"
        );
    }
}
