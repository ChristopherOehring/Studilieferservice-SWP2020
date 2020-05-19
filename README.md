# StudiLieferservice
**Softwareprojekt 2020 - Thema 09: "Lieferservice für Studenten"**

## Status  `v0.1`
Erster lauffähiger Prototyp (noch nicht in Docker); grundlegende Implementierung der 
wichtigsten Komponenten und Verbindungen zwischen diesen; minimale Funktionalität

## Komponenten (Microservices)
Bereits implementiert:
* **Nutzerverwaltung** (user-manager)
* **Gruppenverwaltung** (group-manager)
* **Einkaufslistenverwaltung** (shopping-list-manager)
* _(Composer: momentan nicht verwendet)_

## Funktionalität
**Nutzerverwaltung**: _relativ minimal_: Anlegen eines Accounts, 
mit diesem anmelden und ihn teilweise bearbeiten; Link zur Gruppenverwaltung 
(eine für alle Nutzer)

**Gruppenverwaltung**: _minimal_: Erstellen von Gruppen und sich diese 
in einer Liste anzeigen lassen; jeweils Link zur entsprechenden Einkaufsliste

**Einkaufslistenverwaltung**: _minimal_: automatisches Erstellen von Einkaufslisten bei
Erstellung einer Gruppe; Hinzufügen von Artikeln und sich diese in einer Liste 
anzeigen lassen

## Setup
Um das Projekt zu starten bzw. um auf die Webseiten zugreifen zu können, müssen die 
einzelnen Komponenten gestartet werden. In den jeweiligen Modulen müssen daher die
Klassen `UserManagerApplication.java`, `GroupManagerApplication.java` und 
`ShoppingListManagerApplication.java` ausgeführt werden.

## Access
Der Ausgangspunkt der Anwendung ist die Webseite http://localhost:8080 
(Startseite der Nutzerverwaltung). Von dort aus kann man nach dem Login auf die 
anderen Komponenten zugreifen.

Es ist auch möglich (wenn auch nicht unbedingt vorgesehen), direkt auf die 
Gruppenverwaltung zuzugreifen: http://localhost:8010/web/group/index

Auf die Einkaufslistenverwaltung kann man nicht direkt zugreifen. Um die 
Erreichbarkeit zu überprüfen, gibt es aber die Adresse 
http://localhost:8070/shoppingList/, die eine einfache Nachricht anzeigt.
