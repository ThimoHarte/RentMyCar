# 0. Inleiding

## 0.1 Aanleiding en doel

Dit document hoort bij de module *AI-powered software development* van de opleiding Associate degree ICT van Avans Hogeschool (jaar 2). Het beschrijft de uitwerking van leeruitkomst 1: *AI-powered backend development*.

Het doel van dit document is het ontwerp, de realisatie en het testen te beschrijven van een Web API voor de casus Rent my car. De Web API is gebouwd in Kotlin met het framework Ktor. In periode 2 wordt deze API gebruikt door een Android-app.

Ik werk dit project alleen uit. Dit is afgestemd met de docent.

## 0.2 De casus: Rent my car

Rent my car is een platform voor het delen van auto's. Het doel is duurzamer vervoer: een auto die stilstaat, kan door iemand anders worden gebruikt.

Gebruikers kunnen op het platform:
- hun eigen auto aanbieden om te verhuren aan andere gebruikers;
- een auto van een ander zoeken, reserveren en huren.

Het platform kent drie soorten auto's:
- **ICE** (Internal Combustion Engine): een auto met een verbrandingsmotor op benzine, diesel of LPG;
- **BEV** (Battery Electric Vehicle): een elektrische auto met een accu;
- **FCEV** (Fuel Cell Electric Vehicle): een elektrische auto die stroom maakt uit waterstof.

Een eigenaar kan per auto de kosten per kilometer en de TCO (Total Cost of Ownership, de totale kosten per jaar) laten berekenen. Die berekening verschilt per soort auto.

Een huurder kan auto's zoeken en filteren, bijvoorbeeld op afstand of prijs, en een auto reserveren voor een bepaalde periode. Daarna kan de huurder de route naar de auto opvragen. Tijdens het rijden houdt de app het rijgedrag bij; rustig rijden levert bonuspunten op.

## 0.3 Afbakening

De casus is groot. Daarom maak ik onderscheid tussen wat in de backend (periode 1) komt en wat pas in de app (periode 2) wordt uitgewerkt.

**Wel in de backend (periode 1):**
- auto's beheren: toevoegen, bekijken, zoeken en filteren, wijzigen en verwijderen;
- de kosten per kilometer en de TCO berekenen;
- gebruikers registreren en laten inloggen;
- reserveringen maken, met controle dat twee reserveringen van dezelfde auto niet overlappen;
- foto's van een auto uploaden;
- een AI-agent die een deel van de functionaliteit uitvoert (de precieze invulling volgt; het taalmodel draait lokaal en is gratis).

**Pas in periode 2 (Android-app):**
- auto's tonen op een kaart;
- de looproute naar de auto;
- het meten van rijgedrag met de sensoren van de telefoon en het toekennen van bonuspunten.

De backend krijgt voor deze onderdelen alleen wat nodig is om de gegevens op te slaan en op te vragen.

**Buiten scope:**
- echte betalingen.

## 0.4 Technische opzet en projectstructuur

Het project is geschreven in **Kotlin**. Als build tool gebruik ik de **Kotlin Toolchain** in plaats van Gradle, zoals aanbevolen in de module.

Het project is aangemaakt met de Kotlin Multiplatform-wizard ([kmp.jetbrains.com](https://kmp.jetbrains.com)), met als doelen Android en Server. Daardoor is de structuur meteen geschikt voor periode 2.

| Module | Doel | Periode |
|---|---|---|
| `server` | De Web API, gebouwd met Ktor | 1 |
| `core` | Gedeelde code voor server en app, zoals de datamodellen die via de API worden verstuurd | 1 en 2 |
| `app/shared` | De gebruikersinterface (Compose) en de logica van de app | 2 |
| `app/androidApp` | De Android-app zelf | 2 |

Ik heb voor deze opzet gekozen omdat de server en de app dezelfde datamodellen uit `core` gebruiken. Als ik een veld toevoeg of wijzig, geldt dat direct voor beide kanten. Dat voorkomt verschillen tussen wat de API verstuurt en wat de app verwacht.

**Opslag van gegevens:** de gegevens worden opgeslagen in een **H2-database**. H2 draait mee in de applicatie, waardoor er geen aparte databaseserver geïnstalleerd hoeft te worden. Voor de toegang tot de database gebruik ik de Kotlin-library **Exposed**. De rest van de applicatie praat met de database via een *repository-interface*. Daardoor kan ik tijdens het bouwen en testen eerst met gegevens in het geheugen werken en daarna overstappen op de database, zonder dat de rest van de code hoeft te veranderen.

## 0.5 Leeswijzer

| Hoofdstuk | Inhoud |
|---|---|
| 1. Analyse | Eisen, rollen, user stories, permissiematrix en use cases |
| 2. Ontwerp | Klassendiagram, toelichting op de OO-keuzes, packagediagram en databasemodel |
| 3. Web API | Architectuur, endpoints, foutafhandeling, authenticatie, de AI-agent en sequence diagrams |
| 4. Testen | Teststrategie, unit tests, integratietests en testrapport |
| 5. Installatie en gebruik | Hoe de applicatie wordt gestart en getest, inclusief testdata |
| 6. Productverantwoording | Verantwoording van het AI-gebruik, highlights, lowlights en lessons learned |
| 7. Android app | Uitwerking in periode 2 |

Daarnaast zijn er twee losse documenten:
- `aannames.md`: de vaste waarden die in de berekeningen worden gebruikt, zoals energieprijzen;
- `ai-log.md`: een logboek van het gebruik van AI tijdens het project.

**Taalafspraken:** dit document en de commitberichten zijn in het Nederlands. De code, inclusief de namen en het commentaar, is in het Engels, omdat de opdracht Engelstalige broncode vraagt.
