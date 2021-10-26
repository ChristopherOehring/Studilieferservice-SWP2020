# Softwareproject 2020 - Topic 09: "Delivery Service for Students"

Webservice built with Spring Boot utilising Docker, PostgreSQL, Apache Kafka and Composer by REWE digital. Features a Microservice-Frontend. Lets costumers create groups to order groceries for a joint delivery saving shipping costs or helping to reach the minimum order value by bundling the individual orders into one single order.

Made by a team of students in cooperation with REWE digital in the "Softwareproject" at TU Ilmenau.

# StudiLieferservice
**Softwareprojekt 2020 - Thema 09: "Lieferservice für Studenten"**

## Status  `v1.0`
Fertiges, lauffähiges Programm, vollständig in Docker; umfassende Implementierung 
aller wichtigen Komponenten und Verbindungen zwischen diesen (Kafka, Web, REST)

## Komponenten (Microservices)
implementiert (alle):
* **Nutzerverwaltung** (user-manager)
* **Gruppenverwaltung** (group-manager)
* **Einkaufslistenverwaltung** (shopping-list-manager)
* **Produktverwaltung** (product-manager)
* **Chatfunktion** (chat-manager)
* _(Composer: von REWE digital, nur für Routing)_

## Funktionalität
**Nutzerverwaltung**: _vollständig_: Anlegen eines Accounts, 
mit diesem anmelden und ihn bearbeiten;  
Weiterleitung zur Gruppenverwaltung; Sessions

**Gruppenverwaltung**: _relativ vollständig_: Erstellen von Gruppen und sich diese mit den 
Mitgliedern anzeigen lassen; Chat und Einkaufsliste sind eingebunden; es lassen 
sich Liefertermine eingeben; Sessions

**Einkaufslistenverwaltung**: _vollständig_: automatisches Erstellen von Einkaufslisten bei 
Erstellung einer Gruppe (Kafka); Hinzufügen von Artikeln (Kafka) und sich diese in einer 
Liste im Browser anzeigen lassen; Sessions

**Produktverwaltung**: _vollständig_: Produkte können gespeichert und an andere 
Microservices geschickt werden (Kafka). Im Browser können die Produkte ausgewählt werden; 
Sessions

**Chatfunktion**: _vollständig_: Nutzer von Gruppen können sich im Browser 
in Chats schreiben, die Chatverläufe werden gespeichert; Chats werden automatisch 
beim Erstellen einer Gruppe erstellt (Kafka); Sessions

_Hinweis: Kann-Kriterien etc. nicht berücksichtigt_

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
sich einloggen, wonach man zur Gruppenverwaltung weitergeleitet wird.

Es ist danach möglich, auf die Seite für den Benutzer in der Gruppenverwaltung zuzugreifen 
(http://localhost:9010/web/userMenu/). Dort können neue Gruppen erstellt werden, 
wodurch man zur Seite der neu erstellten Gruppe weitergeleitet wird.

Auf die Einkaufslistenverwaltung kann man erstmal indirekt zugreifen, 
sie ist in die Seite der Gruppe eingebunden. Allerdings kann auch direkt auf die Einkaufsliste zu dieser 
Gruppe und dem Nutzer zugegriffen werden: http://localhost:9070/shoppingList/<groupId>/<userEmail>. 
Dort kann dann die Menge von Produkten verändert werden. Dafür müssen diese aber erst in der 
Produktverwaltung erstellt und der Einkaufsliste hinzugefügt werden.

Per Link in der Einkaufsliste kann auf die Produktverwaltung (http://localhost:9060/products) 
zugegriffen werden. 
Allerdings können Produkte nur per Konsole zur Auswahl hinzugefügt werden.

Auf den Chat kann nur im Browser zugegriffen werden, er ist wie die Einkaufsliste 
in die Seite der Gruppe eingebunden (http://localhost:9040/chat/<groupId>/<userEmail>).
