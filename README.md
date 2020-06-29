# StudiLieferservice
**Softwareprojekt 2020 - Thema 09: "Lieferservice für Studenten"**

## Status  `v0.2`
Fast fertiges, lauffähiges Programm, vollständig in Docker; umfassende Implementierung 
aller Komponenten und teilweise Verbindungen zwischen diesen (Kafka vollständig, 
Web noch nur teilweise); gemischte Funktionalität

Web-Schnittstellen (Composer etc.) und Session-Management müssen noch größtenteils 
implementiert werden.

## Komponenten (Microservices)
implementiert (alle):
* **Nutzerverwaltung** (user-manager)
* **Gruppenverwaltung** (group-manager)
* **Einkaufslistenverwaltung** (shopping-list-manager)
* **Produktverwaltung** (product-manager)
* **Chatfunktion** (chat-manager)
* _(Composer: momentan nur teilweise verwendet)_

## Funktionalität
**Nutzerverwaltung**: _relativ vollständig_: Anlegen eines Accounts, 
mit diesem anmelden und ihn bearbeiten; nicht alle Funktionen sind im Browser aufrufbar; 
Weiterleitung zur Gruppenverwaltung fehlt; Sessions fehlen

**Gruppenverwaltung**: _relativ vollständig_: Erstellen von Gruppen und sich diese mit den 
Mitgliedern anzeigen lassen; Chat und Einkaufsliste sind noch nicht eingebunden; es lassen 
sich noch keine Liefertermine eingeben; Sessions fehlen noch

**Einkaufslistenverwaltung**: _nahezu vollständig_: automatisches Erstellen von Einkaufslisten bei 
Erstellung einer Gruppe (Kafka); Hinzufügen von Artikeln (Kafka) und sich diese in einer 
Liste im Browser anzeigen lassen; Sessions fehlen noch

**Produktverwaltung**: _nahezu vollständig_: Produkte können gespeichert und an andere 
Microservices geschickt werden (Kafka). Im Browser können die Produkte ausgewählt werden; 
Sessions fehlen noch

**Chatfunktion**: _nahezu vollständig_: Nutzer von Gruppen können sich im Browser 
in Chats schreiben, die Chatverläufe werden gespeichert; Chats werden automatisch 
beim Erstellen einer Gruppe erstellt (Kafka); Sessions fehlen noch

## Setup
Um das Projekt zu starten bzw. um auf die Webseiten zugreifen zu können, müssen die 
Komponenten gestartet werden. Damit alles funktioniert, muss das Programm per docker-compose 
gestartet werden. Als Erstes sollte der Befehl `mvn clean install -DskipTests` ausgeführt 
werden. Danach muss die Datei "build.sh" ausgeführt werden, worauf sich ein Terminal öffnet. 
Dort muss nun der Befehl `docker compose up` eingegeben werden, worauf die Docker Container 
starten und im Log angezeigt wird, wie diese hochgefahren werden.

## Access
Der Ausgangspunkt der Anwendung ist die Webseite http://localhost:9080 
(Startseite der Nutzerverwaltung). Von dort aus kann man sich einen Account erstellen und 
sich einloggen, allerdings gibt es noch keine Weiterleitung zur Gruppenverwaltung.

Es ist danach möglich, auf die Seite für den Benutzer in der Gruppenverwaltung zuzugreifen: 
http://localhost:9010/web/userMenu/<userEmail>. Dort können neue Gruppen erstellt werden, 
wodurch man zur Seite der neu erstellten Gruppe weitergeleitet wird.

Auf die Einkaufslistenverwaltung kann man momentan nur indirekt zugreifen, indem man sich aus 
der URL die ID der erstellten Gruppe holt. Danach kann auf die Einkaufsliste zu dieser 
Gruppe und dem Nutzer zugegriffen werden: http://localhost:9070/shoppingList/<groupId>/<userEmail>. 
Dort kann dann die Menge von Produkten verändert werden. Dafür müssen diese aber erst in der 
Produktverwaltung erstellt und dann per Konsole der Einkaufsliste hinzugefügt werden.

Es kann auf die Produktverwaltung im Browser zugegriffen werden: http://localhost:9060/products. 
Allerdings können Produkte nur per Konsole zur Auswahl hinzugefügt werden.

Auf den Chat kann nur im Browser zugegriffen werden, die Seite ist erreichbar 
unter: http://localhost:9040/chat/<groupId>/<userEmail>.