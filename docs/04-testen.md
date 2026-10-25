# 4. Testen

In dit hoofdstuk staat hoe de Web API wordt getest: welke soorten tests er zijn, welke tests gepland zijn en wat de uitkomsten zijn.

## 4.1 Teststrategie

### De testpiramide

De tests volgen de testpiramide uit les 3: veel kleine, snelle tests onderin en een paar grotere tests bovenin.

```
            ▲     .http-flows        handmatig, ook gebruikt in de demo
           ▲▲▲    Integratietests    database en HTTP, met testApplication
         ▲▲▲▲▲▲   Unit tests         domein en services, zonder database of server
```

| Niveau | Wat wordt getest | Hoe | Snelheid |
|---|---|---|---|
| **Unit tests** | De regels en berekeningen in het domein, en de services | Zonder database en zonder server; services krijgen een `FakeRepository` | Zeer snel |
| **Integratietests** | De samenwerking tussen onderdelen: repository en database, routes en services | Echte Exposed-repositories met een H2-database in het geheugen; echte routes met `testApplication` | Snel |
| **`.http`-flows** | Complete routes door de API, zoals een gebruiker ze doorloopt | Handmatig in IntelliJ, tegen de draaiende server | Handmatig |

### Unit tests

Unit tests controleren één klasse of functie los van de rest:
- **domein:** de kosten per km en de TCO per soort auto, de huurprijs, de regels voor een reservering, `overlapsWith()` en de statusovergangen;
- **services:** de regels waarvoor gegevens uit een repository nodig zijn, zoals "alleen de eigenaar mag goedkeuren". De service krijgt daarvoor een `FakeRepository`: een eenvoudige versie die gegevens in een lijst bewaart. Dit kan omdat de service alleen de interface van de repository kent (zie hoofdstuk 3.1);
- **beveiliging:** het hashen en controleren van wachtwoorden en het afwijzen van verlopen tokens (gepland, zie 4.2).

De rekenvoorbeelden uit `aannames.md` worden gebruikt als verwachte uitkomsten.

### Integratietests

Integratietests controleren of onderdelen goed samenwerken:
- **Database:** de echte Exposed-repositories worden getest tegen een H2-database in het geheugen. Zo wordt onder andere gecontroleerd of de vertaling van het OO-model naar de tabellen klopt (hoofdstuk 2.5): een `ElectricCar` die wordt opgeslagen, moet er ook weer als `ElectricCar` uitkomen. Zo test de docent zijn repositories ook (`ExposedRepositoriesTest.kt` in de voorbeeldapplicatie).
- **HTTP:** de echte routes, plugins en services worden getest met `testApplication` van Ktor. De server en een testclient draaien dan in hetzelfde proces, zonder dat er een poort open hoeft. De client stuurt echte HTTP-verzoeken met JSON en controleert de statuscode en de inhoud van het antwoord. Dit is de werkwijze uit les 7 en uit les 8 van de gastdocent (*"A test runs server and client in one process"*).

### `.http`-flows

Per soort gebruiker is er een `.http`-bestand met een complete route door de API, zoals in de voorbeeldapplicatie van de docent:

| Bestand | Route |
|---|---|
| `visitor-flow.http` | Auto's zoeken en bekijken zonder in te loggen |
| `owner-flow.http` | Registreren → inloggen → auto aanbieden → auto wijzigen → kosten bekijken → aanvragen bekijken → aanvraag goedkeuren → auto verwijderen |
| `renter-flow.http` | Inloggen → auto zoeken → reservering aanvragen → reservering bekijken → annuleren |

Foto uploaden komt in periode 2 bij `owner-flow.http`. IntelliJ bewaart het token na het inloggen automatisch voor de volgende verzoeken (`client.global.set(...)`). Deze bestanden worden gebruikt om de API bij het assessment te demonstreren en om het testrapport (4.4) in te vullen. Ze staan in `server/test/http-requests/`.

### Reproduceerbare tests

Leeruitkomst 1 vraagt om reproduceerbare tests: een test moet elke keer dezelfde uitkomst geven.
- **Lege database per test:** elke integratietest begint met een nieuwe, lege H2-database in het geheugen, met een eigen naam (`System.nanoTime()`). Tests beïnvloeden elkaar dus niet.
- **Vaste tijd:** functies die van de tijd afhangen, krijgen de huidige tijd mee als parameter (`now`, zie hoofdstuk 2.1). In een test wordt een vaste tijd gebruikt, zodat een test over "de start ligt in het verleden" altijd hetzelfde resultaat geeft.
- **Vaste testgegevens:** de tests gebruiken vaste gebruikers, auto's en tijden uit `TestData.kt` (bijvoorbeeld `OWNER`, `RENTER`, `petrolCar()` en `NOW`). De fake repositories staan in `Fakes.kt`.
- **Opbouw van een test:** elke test volgt *Arrange, Act, Assert*: eerst klaarzetten, dan de actie uitvoeren, dan de uitkomst controleren.

### Gereedschap

| Gereedschap | Waarvoor |
|---|---|
| `kotlin.test` | Het testframework voor alle tests. Dit is de standaard in een Kotlin Toolchain-project en wordt gebruikt in les 3 en in de voorbeeldapplicatie van de docent. |
| `ktor-server-test-host` | `testApplication` voor de HTTP-tests (staat al in het project). |
| H2 (in het geheugen) | De database voor de integratietests. |
| IntelliJ HTTP Client | Het uitvoeren van de `.http`-flows. |

De tests van de server worden uitgevoerd met `./kotlin test -m server` (of `.\kotlin.bat test -m server` op Windows). Zonder `-m server` worden ook de modules van de Android-app gebouwd; dat geeft nu nog een interne build-fout en wordt in periode 2 opgelost.

### Afwijkingen van de les

| Onderwerp | Les / (gast)docent | Keuze | Waarom |
|---|---|---|---|
| Testbibliotheek | De gastdocent gebruikt Kotest (`shouldBe`) | `kotlin.test` | Standaard in de Kotlin Toolchain en gebruikt in les 3 en door de docent; één bibliotheek minder om te leren. |
| Database in tests | Leeruitkomst 1 noemt Testcontainers als mogelijkheid | H2 in het geheugen | Testcontainers heeft Docker nodig. H2 is ook de database van de applicatie zelf, en de docent gebruikt het ook in zijn tests. |
| Dekkingspercentage | – | Niet gemeten | De tests richten zich op de belangrijke regels en flows; een percentage zegt weinig over de kwaliteit van de tests. |

## 4.2 Unit tests

✱ = deze tests worden als eerste gemaakt; samen voldoen ze aan de minimale eis van drie unit tests. De overige tests maken de controle vollediger.

De kolom *Gebouwd* geeft aan of de test al in de code staat (in `server/test/`). De tests die nog niet zijn gebouwd, staan gepland; het gedrag dat ze controleren is wel met de hand getest (zie 4.4).

| Nr | Test | Verwachte uitkomst | Eis | Gebouwd |
|---|---|---|---|---|
| UT-01 ✱ | Kosten per km van een benzineauto met een verbruik van 6 liter per 100 km | € 0,1452 | FE-08 | ✅ `CarCostsTest` |
| UT-02 | Kosten per km van een BEV (17 kWh per 100 km) en van een FCEV (1 kg per 100 km) | € 0,0578 en € 0,21 | FE-08 | Gepland |
| UT-03 ✱ | TCO van een benzineauto van € 30.000 (rekenvoorbeeld uit `aannames.md`) | € 9.128 per jaar | FE-09 | ✅ `CarCostsTest` |
| UT-04 | Huurprijs van 4 uur met een uurtarief van € 6,00 en 80 km met een km-tarief van € 0,25 | € 44,00 | FE-13 | Gepland |
| UT-05 ✱ | `overlapsWith()` voor vier gevallen: 9–12 en 11–14 (ja), 9–12 en 12–15 (nee, sluit aan), 9–12 en 13–15 (nee), 9–17 en 10–11 (ja, valt erbinnen) | ja, nee, nee, ja | FE-15 | ✅ `ReservationTest` |
| UT-06 | `Reservation.request()` met een eindmoment vóór het startmoment, een duur korter dan 1 uur, een startmoment in het verleden, en de eigen auto | Vier keer een `IllegalArgumentException` | FE-12 | Gepland |
| UT-07 | `approve()` op een reservering met status `REJECTED` of `CANCELLED` | `IllegalStateException` | FE-14 | Gepland |
| UT-08 | `cancel()` nadat het startmoment is bereikt | `IllegalStateException` | FE-16 | Gepland |
| UT-09 ✱ | `ReservationService`: een aanvraag goedkeuren terwijl er al een overlappende goedgekeurde reservering is | `ConflictException`; de status blijft `REQUESTED` | FE-15 | ✅ `ServiceTest` |
| UT-10 | `ReservationService`: een gebruiker die niet de eigenaar is, probeert een aanvraag goed te keuren | `ForbiddenException` | Permissiematrix (1.6) | Gepland |
| UT-11 | `CarService`: een auto verwijderen die nog een goedgekeurde reservering in de toekomst heeft | `ConflictException`; de auto blijft actief | FE-06 | Gepland |
| UT-12 | `PasswordHasher`: een wachtwoord hashen en daarna controleren met het juiste en met een fout wachtwoord | `true` en `false` | NFE-05 | Gepland |
| UT-13 | `TokenService`: een token waarvan de geldigheid is verlopen, wordt afgewezen | Token is ongeldig | NFE-04 | Gepland |

## 4.3 Integratietests

✱ = deze tests worden als eerste gemaakt; samen voldoen ze aan de minimale eisen van drie integratietests en tests voor de vier soorten CRUD-requests.

| Nr | Test | Verwachte uitkomst | Eis | Gebouwd |
|---|---|---|---|---|
| IT-01 ✱ | `ExposedCarRepository`: een `IceCar`, een `ElectricCar` en een `HydrogenCar` opslaan en weer ophalen | Dezelfde soort en dezelfde gegevens terug | 2.5 | ✅ `CarRepositoryTest` |
| IT-02 | `ExposedCarRepository`: een auto op niet actief zetten en daarna zoeken | De auto komt niet meer terug | FE-06 | Gepland |
| IT-03 ✱ | CRUD op auto's: `POST /api/cars`, `GET /api/cars/{id}`, `PUT /api/cars/{id}`, `DELETE /api/cars/{id}`, daarna opnieuw `GET` | 201, 200, 200, 204, 404 | FE-03 t/m FE-06 | ✅ `ApiTest` |
| IT-04 ✱ | `POST /api/cars` zonder token | 401 | NFE-04 | ✅ `ApiTest` |
| IT-05 | `PUT /api/cars/{id}` voor de auto van een andere gebruiker | 403 | Permissiematrix (1.6) | Gepland |
| IT-06 | `GET /api/cars/{id}` voor een auto die niet bestaat | 404 | FE-11 | Gepland |
| IT-07 | Registreren → inloggen → `GET /api/users/me` | 201, 200, 200 | FE-01, FE-02 | Gepland |
| IT-08 | Inloggen met een fout wachtwoord | 401 met de algemene foutmelding | FE-02 | Gepland |
| IT-09 | Reservering aanvragen → goedkeuren → een tweede, overlappende aanvraag goedkeuren | 201, 200, 409 | FE-12, FE-14, FE-15 | Gepland |
| IT-10 | `GET /api/cars?type=BEV&city=Breda` | Alleen actieve BEV's in Breda | FE-10 | Gepland |
| IT-11 | Elf keer inloggen binnen een minuut | De elfde keer 429 | NFE-10 | Gepland |

**Stand:** er zijn 7 geautomatiseerde tests gebouwd: 4 unit tests en 3 integratietests. Daarmee is voldaan aan de minimale eis van drie unit tests, drie integratietests en tests voor de vier soorten CRUD-requests (NFE-07). Alle 7 tests slagen (`.\kotlin.bat test -m server`, 25 oktober 2026).

## 4.4 Testrapport API-requests

Dit testrapport is ingevuld op 25 oktober 2026. De requests zijn uitgevoerd tegen de draaiende server (`.\kotlin.bat run -m server`) met de demodata en met de verzoeken uit de `.http`-bestanden. Een paar situaties worden ook door een geautomatiseerde test gecontroleerd; dat staat erbij.

| Request | Situatie | Verwacht | Werkelijk | Resultaat |
|---|---|---|---|---|
| `POST /api/users/register` | Nieuw account | 201 | 201 | ✅ |
| `POST /api/users/register` | E-mailadres bestaat al | 409 | 409 | ✅ |
| `POST /api/users/login` | Juist wachtwoord | 200 + token | 200 + token | ✅ |
| `POST /api/users/login` | Fout wachtwoord | 401 | 401, "Invalid email or password." | ✅ |
| `POST /api/users/login` | Meer dan 10 pogingen per minuut | 429 | De elfde poging 429 | ✅ |
| `GET /api/users/me` | Met geldig token | 200 | 200 | ✅ |
| `GET /api/users/me` | Zonder token | 401 | 401 | ✅ |
| `GET /api/cars` | Zonder filters | 200 + alle actieve auto's | 200 + alle actieve auto's | ✅ |
| `GET /api/cars?type=…&city=…` | Met filters | 200 + passende auto's | 200, alleen BEV's in Breda | ✅ |
| `GET /api/cars?type=…` | Onbekende soort auto | 400 | 400 | ✅ |
| `GET /api/cars?maxHourlyRate=…` | Uurprijs is geen getal | 400 | 400 | ✅ |
| `GET /api/cars?from=…&to=…` | Einde vóór begin | 400 | – | Periode 2 |
| `GET /api/cars/{id}` | Bestaande auto | 200 | 200 (ook in IT-03) | ✅ |
| `GET /api/cars/{id}` | Onbekende of niet-actieve auto | 404 | 404 (ook in IT-03) | ✅ |
| `GET /api/cars/mine` | Met geldig token | 200 + eigen auto's | 200 + eigen auto's | ✅ |
| `POST /api/cars` | Geldige auto | 201 | 201 (ook in IT-03) | ✅ |
| `POST /api/cars` | Negatief tarief | 400 | 400 | ✅ |
| `POST /api/cars` | Kenteken bestaat al | 409 | 409 | ✅ |
| `POST /api/cars` | Zonder token | 401 | 401 (IT-04) | ✅ |
| `PUT /api/cars/{id}` | Eigen auto | 200 | 200 (ook in IT-03) | ✅ |
| `PUT /api/cars/{id}` | Auto van een ander | 403 | 403 | ✅ |
| `DELETE /api/cars/{id}` | Eigen auto zonder toekomstige reserveringen | 204 | 204 (ook in IT-03) | ✅ |
| `DELETE /api/cars/{id}` | Auto met een toekomstige reservering | 409 | 409 | ✅ |
| `DELETE /api/cars/{id}` | Auto van een ander | 403 | 403 | ✅ |
| `GET /api/cars/{id}/costs` | Eigen auto | 200 + kosten | 200 + kosten | ✅ |
| `GET /api/cars/{id}/costs` | Auto van een ander | 403 | 403 | ✅ |
| `POST /api/cars/{id}/photos` | Geldige afbeelding | 201 | – | Periode 2 |
| `POST /api/cars/{id}/photos` | Ongeldig bestandstype | 400 | – | Periode 2 |
| `DELETE /api/cars/{id}/photos/{photoId}` | Eigen foto | 204 | – | Periode 2 |
| `GET /api/photos/{fileName}` | Bestaande foto | 200 + afbeelding | – | Periode 2 |
| `POST /api/reservations` | Geldige aanvraag | 201 + geschatte prijs | 201 + geschatte prijs | ✅ |
| `POST /api/reservations` | Korter dan 1 uur | 400 | 400 | ✅ |
| `POST /api/reservations` | Eigen auto | 400 | 400, "You cannot reserve your own car." | ✅ |
| `GET /api/reservations/mine` | Met geldig token | 200 + eigen reserveringen | 200 + eigen reserveringen | ✅ |
| `GET /api/cars/{id}/reservations` | Eigen auto | 200 | 200 | ✅ |
| `GET /api/reservations/{id}` | Na goedkeuring, als huurder | 200 + contactgegevens eigenaar | 200 + `ownerContact` | ✅ |
| `GET /api/reservations/{id}` | Reservering van een ander | 403 | 403 | ✅ |
| `PATCH /api/reservations/{id}/status` | Goedkeuren door de eigenaar | 200 | 200 | ✅ |
| `PATCH /api/reservations/{id}/status` | Goedkeuren met overlap | 409 | 409 (ook in UT-09) | ✅ |
| `PATCH /api/reservations/{id}/status` | Goedkeuren door een ander dan de eigenaar | 403 | 403 | ✅ |
| `PATCH /api/reservations/{id}/status` | Annuleren door de huurder vóór de start | 200 | 200 | ✅ |
| `PATCH /api/reservations/{id}/status` | Annuleren na de start | 409 | 409, "This reservation has already started." | ✅ |

### Wat de tests opleverden

_Wordt ingevuld na het bouwen: welke fouten de tests hebben gevonden en wat er daardoor in het ontwerp of de code is verbeterd._
