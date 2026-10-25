# 6. Productverantwoording

*Dit hoofdstuk is met hulp van AI opgesteld op basis van mijn AI-log en het verloop van het project.*

## 6.1 Verantwoording AI-gebruik

Ik heb tijdens het hele project gewerkt met **Claude Code** (model Claude Opus 5.5), in de terminal naast IntelliJ IDEA. De uitgebreide verantwoording, per datum met wat ik vroeg, wat de AI voorstelde en wat ik besloot, staat in het losse document **AI-log**. Hieronder de samenvatting.

**Waarvoor ik AI heb gebruikt:**

| Fase | Wat de AI deed                                                                                      | Wat ik deed |
|---|-----------------------------------------------------------------------------------------------------|---|
| Analyse | De studiedocumenten samengevat, vragen gesteld over de casus en voorstellen gedaan                  | De scope en de regels van de casus bepaald, en voorstellen afgewezen die niet bij de casus pasten |
| Ontwerp | Voorstellen voor het domeinmodel, de architectuur en de database; de diagrammen in PlantUML gemaakt | Per onderdeel gevraagd of het uit het lesmateriaal kwam, en de keuzes gemaakt |
| Realisatie | Uitleg van Kotlin-begrippen, voorbeelden van code gegeven                                           | De stijl van de code bepaald, een deel zelf geschreven, en voorstellen beoordeeld |
| Testen | Een teststrategie voorgesteld, tests geschreven en de API met de hand getest                        | Gecontroleerd of de verwachte uitkomsten klopten, met de rekenvoorbeelden |
| Documentatie | Hoofdstukken uitgeschreven en de documentatie gelijkgemaakt aan de software                         | De inhoud bepaald en eerlijk aangegeven wat nog niet gerealiseerd is |

**Hoe ik de AI heb gecontroleerd:**
- **Bronnen vragen.** De AI geeft bij elke keuze aan of die uit het lesmateriaal komt, van de docent of de gastdocent, of een eigen voorstel is. Zo kwam bijvoorbeeld uit dat de kluiscode voor de sleutel niet in de casus stond.
- **Vergelijken met de docent.** Ik liet de AI de code van de docent en de gastdocent bekijken, en koos bewust voor de aanpak van de docent met mijn eigen verbeteringen (`application.conf`, met tokens van 15 minuten en een sleutel van minstens 32 tekens).
- **Zelf twijfelen.** Bij de beveiliging stelde de AI eerst tokens van 24 uur, BCrypt en geen rate limiting voor. Mijn twijfel daarover klopte: na extra onderzoek (OWASP, gastles) werden het 15 minuten, Argon2id en rate limiting.
- **Testen.** Na elke wijziging draaiden de tests; aan het einde is de hele API met de hand getest (testrapport, hoofdstuk 4.4).

**Waar ik een voorstel van de AI niet heb overgenomen:** GPS-coördinaten voor de plek van een auto, de kluiscode, het meeverwijderen van reserveringen (ik koos *soft delete*), compacte Kotlin-code die ik slecht kon lezen, en een uitgebreid sjabloon voor dit document dat bedoeld is voor een groter team.


## 6.2 Highlights

- **Het domeinmodel.** `Car` is een sealed class met drie soorten auto's. De TCO staat één keer in `Car` en werkt voor elke soort, doordat elke soort zijn eigen `costPerKm()` en `fixedCostsPerYear()` heeft (polymorfisme).
- **Onderbouwde beveiliging.** Argon2id, tokens van 15 minuten, rate limiting en eigenaarschap controleren in de service (403). Voor elke keuze is de bron bekend: OWASP, de docent of de gastdocent.
- **Leesbare code.** Functies met `return` onderaan, geen `it`, `for`-lussen in plaats van `.map {}`, en boven elke databasequery de SQL in het commentaar. Zo kan ik elke regel uitleggen.
- **Alles getest.** Alle 7 geautomatiseerde tests slagen, en alle situaties uit het testrapport geven de verwachte statuscode.
- **Eerlijke documentatie.** Wat niet is gebouwd, staat er ook zo in, met de planning voor periode 2.

## 6.3 Lowlights en verbeterpunten

- **Niet alles is gebouwd.** Foto's en zoeken op beschikbaarheid in een periode volgen in periode 2. De AI-agent is nog niet gerealiseerd.
- **Minder geautomatiseerde tests dan gepland.** Er zijn 7 van de 24 geplande tests gebouwd. Dat is genoeg voor de minimale eis, maar het gedrag van de andere tests is alleen met de hand gecontroleerd.
- **Git ging een keer mis.** Door een vastgelopen merge moesten de branches in één keer worden opgeruimd. Vaker en kleiner committen voorkomt dat.
- **Verbeterpunten in de code:**
  - het kenteken gelijkmaken (nu zijn `ab-123-c` en `AB-123-C` twee verschillende auto's);
  - het adres en het bouwjaar controleren, en een bewuste keuze maken over een tarief van € 0;
  - de eigenaar van een auto wordt per auto apart opgehaald (het N+1-probleem); bij veel auto's is een join beter;
  - goedkeuren is beveiligd met `synchronized`, wat alleen werkt met één server;
  - refresh tokens, zodat de app in periode 2 ingelogd kan blijven.

## 6.4 Lessons learned en advies

**Wat ik heb geleerd:**
- AI-voorstellen klinken logisch, maar ik moet zelf nagaan of ze kloppen met de casus en met de praktijk.
- Bij beveiliging neem ik niet het eerste antwoord over, maar vraag ik naar de bronnen.
- Een AI-voorstel vergelijken met hoe de docent het doet, geeft een betere en beter te onderbouwen keuze.
- Een AI maakt kleine fouten die je pas ziet als je het resultaat zelf goed bekijkt.
- Ik beslis zelf wat ik overneem, en kies voor code die ik kan lezen en uitleggen.

**Advies aan een volgende student:**
- Maak vooraf afspraken met de AI, bijvoorbeeld in een `CLAUDE.md`: eerst overleggen, eenvoudige code, en altijd de bron noemen.
- Laat de AI niet te veel tegelijk schrijven. Kleine stappen zijn beter te begrijpen en te controleren.
- Commit vaak en klein, zodat je makkelijk terug kunt als er iets misgaat.
