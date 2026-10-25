# AI-log

In dit logboek houd ik bij op welke momenten ik AI heb gebruikt tijdens het project, wat ik heb gevraagd, wat de AI voorstelde, hoe ik dat heb gecontroleerd en wat ik uiteindelijk heb besloten. Hoofdstuk 06 (productverantwoording) vat dit logboek samen.

**Gebruikte tool:** Claude Code (model: Claude Opus 5.5), in de terminal naast IntelliJ IDEA.

**Afspraken met de AI:**
- Eerst overleggen, pas iets maken als ik daarom vraag.
- De code zo eenvoudig mogelijk houden, zodat ik alles zelf kan uitleggen.
- De AI legt uit wat het voorstelt; ik neem alleen over wat ik begrijp.
- Commits en pushes doe ik zelf.

---

### 2026-10-05 – Opdracht en studiemateriaal doornemen
- **Doel:** de opdracht en de eisen van de module goed begrijpen voordat ik begin.
- **Wat ik vroeg:** alle studiedocumenten doornemen en samenvatten wat er van mij verwacht wordt.
- **Wat de AI deed:** de lokale studiedocumenten gelezen en via de Chrome-extensie van Claude alle pagina's en bijlagen van de Brightspace-cursus opgehaald en lokaal opgeslagen. Daarna een samenvatting gegeven van de leeruitkomsten, deadlines, eisen en de casus Rent my car.
- **Hoe ik het controleerde:** de samenvatting naast de Brightspace-pagina's gelegd (proftaak, eisen, portfolio-checklist).
- **Mijn beslissing:** ik kies de voorbeeldcasus Rent my car en werk alleen (afgestemd met de docent).
- **Les:** de AI miste eerst context (de casusbeschrijving stond niet in mijn map); pas met het volledige studiemateriaal was de samenvatting compleet.

### 2026-10-05 – Repository inrichten
- **Doel:** het project in Git en op GitHub zetten.
- **Wat ik vroeg:** de `.gitignore` in orde maken en helpen toen GitHub Desktop de repository in een submap had aangemaakt.
- **Wat de AI deed:** regels toegevoegd aan `.gitignore` (IDE-instellingen, lokale database, uploads, `.env`) en de `.git`-map naar de juiste projectmap verplaatst.
- **Hoe ik het controleerde:** in GitHub Desktop gekeken of alle projectbestanden zichtbaar waren en daarna zelf gecommit en gepubliceerd.
- **Mijn beslissing:** studiemateriaal staat buiten het project, zodat het niet in de repository komt.

### 2026-10-05 – Analyse: eerste keuzes voor de casus
- **Doel:** bepalen hoe de belangrijkste regels van Rent my car werken.
- **Wat ik vroeg:** de AI stelde vragen over de casus; bij onderwerpen waar ik nog geen kennis van had, vroeg ik om uitleg en een voorstel.
- **Mijn beslissingen:**
  - Eén gebruiker kan zowel auto's aanbieden als huren (eigen keuze).
  - Een reservering heeft een start- en eindmoment (datum + tijd), minimaal 1 uur, en mag niet overlappen met een andere reservering van dezelfde auto (eigen idee, regels samen uitgewerkt).
  - Huurprijs = uurtarief × uren + km-tarief × km (eigen keuze voor combinatie uur + km).
  - Kosten per km = verbruik per 100 km × energieprijs ÷ 100; aannames voor energieprijzen door de AI voorgesteld.
  - TCO per jaar bij 15.000 km: afschrijving, verzekering, wegenbelasting, onderhoud en energie. Ik wist niet wat TCO was; de AI heeft het uitgelegd en een eenvoudige formule voorgesteld.
- **Hoe ik het controleerde:** de AI ging eerst uit van geschatte prijzen (bijvoorbeeld € 2,00 per liter benzine). Bij het opstellen van `aannames.md` zijn de prijzen opgezocht in actuele bronnen; zie de notitie "Aannames en bronnen".

### 2026-10-05 – Documentatie-opzet
- **Doel:** bepalen hoe ik het ontwikkeldocument en de UML-diagrammen maak.
- **Wat ik vroeg:** een manier om documentatie en diagrammen bij te houden.
- **Wat de AI voorstelde:** Markdown-bestanden in `docs/` in Git en diagrammen met PlantUML (tekst → diagram).
- **Hoe ik het controleerde:** de AI de GitHub-repositories van de docent laten bekijken (o.a. `pjfmast/ktor-android-incident-demo`). De docent gebruikt één `ontwikkeldocument.md` en PlantUML-bestanden in `docs/puml/`.
- **Mijn beslissing:** het beste van beide: losse hoofdstukken die aansluiten op de portfolio-checklist van Brightspace, en diagrammen volgens de methode van de docent (`.puml` in `docs/puml/`, geëxporteerd naar `.svg`).
- **Les:** een AI-voorstel vergelijken met hoe de docent het zelf doet, gaf een betere en beter te onderbouwen keuze.

### 2026-10-05 – Inleiding en keuze voor opslag
- **Doel:** hoofdstuk 0 (inleiding) schrijven en bepalen hoe de gegevens worden opgeslagen.
- **Wat de AI deed:** een voorstel gedaan voor de inhoud van de inleiding; na mijn akkoord de tekst uitgeschreven.
- **Wat ik aanpaste:** de beschrijving van de projectstructuur gecorrigeerd. De AI ging uit van de wizard-link met iOS, maar ik had in de wizard alleen Android en Server gekozen.
- **Opslag:** ik vroeg wat "Exposed en H2" betekent. De AI legde drie opties uit (lijst in het geheugen, H2, PostgreSQL) en stelde voor eerst in het geheugen te werken en daarna over te stappen op Exposed met H2.
- **Mijn beslissing:** ik wil echt een database gebruiken (Exposed + H2), omdat we in de opleiding met databases hebben gewerkt en het assessment vraagt naar de vertaling tussen OO en het relationele model. De tussenstap in het geheugen mag, maar mag ook worden overgeslagen als het een kleine stap is.

### 2026-10-05 – Analyse (hoofdstuk 1)
- **Doel:** eisen, rollen, gegevens, user stories, permissiematrix en use cases bepalen.
- **Wat de AI deed:** vragen gesteld over de casus en een voorstel gedaan voor de inhoud van hoofdstuk 1; na mijn akkoord het hoofdstuk en het use case diagram (PlantUML) uitgeschreven.
- **Waar ik het AI-voorstel heb verbeterd:**
  - **Locatie:** de AI stelde coördinaten voor, die de app via GPS zou invullen. Ik merkte op dat dat fout gaat als je de advertentie niet bij de auto maakt (bijvoorbeeld op je werk). Besluit: in periode 1 alleen een adres en zoeken op plaatsnaam; coördinaten en zoeken op afstand in periode 2 via de kaart.
  - **Sleuteloverdracht:** de AI stelde "ophaalinstructies" voor, zoals een kluiscode. Ik vroeg waar dat vandaan kwam en hoe je weet wie de huurder is. De AI gaf toe dat het een eigen idee was en niet in de casus stond. Besluit: werken zoals SnappCar: de huurder vraagt aan, de eigenaar keurt goed of af en controleert het rijbewijs bij de overdracht.
  - **Goedkeuren:** ik heb toegevoegd dat de eigenaar niet twee aanvragen voor dezelfde periode mag kunnen goedkeuren.
- **Mijn beslissingen:** geen beheerder; een bezoeker mag zoeken zonder in te loggen; annuleren mag tot de start; een auto met toekomstige reserveringen mag niet worden verwijderd; filters op soort, plaats, maximale uurprijs en periode; alleen goedgekeurde reserveringen maken een auto bezet.
- **Les:** AI-voorstellen klinken logisch, maar ik moet zelf nagaan of ze kloppen met de casus en met de praktijk.

### 2026-10-05 – Aannames en bronnen
- **Doel:** de vaste waarden voor de berekeningen (energieprijzen, TCO-bedragen) onderbouwen met bronnen.
- **Wat ik vroeg:** actuele prijzen opzoeken en `aannames.md` opstellen.
- **Wat de AI deed:** via webzoekopdrachten prijzen gezocht bij o.a. ANWB, hoeveelbenzine.nl, joulo.nl en evtrader.com, en het document met formules en rekenvoorbeelden geschreven.
- **Wat opviel:**
  - De eerdere schattingen van de AI waren te laag: benzine is € 2,42 per liter en niet € 2,00. Zonder bronnen te controleren was dat er zo in gekomen.
  - Twee zoekresultaten gaven verschillende benzineprijzen (€ 2,71 en € 2,47). De AI heeft dat gecontroleerd bij de ANWB zelf. Het verschil zat tussen de adviesprijs en de gemiddelde pompprijs.
  - Voor de verzekering en het onderhoud van een waterstofauto is geen betrouwbare bron gevonden. Die bedragen staan als "schatting" in het document.
- **Mijn beslissing:** ik vertrouw de opgezochte bronnen; de rekenvoorbeelden worden later ook gebruikt als testgevallen.
- **Nog te doen:** zelf een paar bronnen openen en controleren.

### 2026-10-05 – Domein-klassendiagram (hoofdstuk 2.1 en 2.2)
- **Doel:** het domeinmodel ontwerpen: de klassen, hun relaties en de gebruikte OO-concepten.
- **Wat de AI deed:** een voorstel gedaan voor de klassen en relaties; na mijn akkoord het klassendiagram (PlantUML) en de toelichting geschreven.
- **Wat ik vroeg en controleerde:**
  - Ik vroeg om uitleg bij twee ontwerpkeuzes: waar de interface komt en waar de overlapcontrole zit.
  - Ik vroeg of de voorstellen uit het lesmateriaal komen. De AI liet per onderdeel de bron zien (les 3, 6 en 7, de HousingMarket-workshop, de demo van de docent) en gaf aan welke keuze een eigen ontwerpkeuze is (`overlapsWith()` in `Reservation`).
  - Ik vroeg wat "domein" betekent en of het woord "repository" klopt. Beide begrippen zijn toegevoegd aan het begin van hoofdstuk 2.
- **Mijn beslissingen:** interfaces bij de repositories (hoofdstuk 3) en niet geforceerd in het domein; de regel voor overlap in `Reservation`, het opzoeken in de service.
- **Nog te checken bij de docent:** of de interfaces in het implementatie-klassendiagram voldoende zijn voor de eis "klassendiagram met overerving, interfaces en abstracte class".

### 2026-10-05 – Diagrammen als plaatje en een gezamenlijke stijl
- **Doel:** de PlantUML-diagrammen als SVG in het document tonen, met een mooiere en modernere stijl.
- **Wat de AI deed:** de officiële PlantUML-tool gebruikt (met de Java die al op mijn pc stond) om de `.puml`-bestanden naar SVG te exporteren. Daarna een gezamenlijk stijlbestand gemaakt (`docs/puml/style.iuml`), de plaatjes bekeken en de indeling aangepast waar tekst over elkaar heen stond.
- **Wat ik vroeg:** de diagrammen mooier en moderner maken, met de tekst beter op zijn plek.
- **Beperking:** zonder de tekenmotor Graphviz is de indeling niet altijd perfect; een label in het klassendiagram staat nog dicht tegen een klasse aan.

### 2026-10-05 – Architectuur en packages (hoofdstuk 2.3 en 3.1)
- **Doel:** bepalen hoe de code wordt ingedeeld.
- **Wat de AI deed:** het architectuurlesmateriaal van de gastdocent gelezen (`nomisRev/avans-toolchain-and-ktor-2026`) en een voorstel gedaan: indeling per feature met daarbinnen routes, service en repository; geen framework voor dependency injection; DTO's en enums in de gedeelde module `core`; package-naam `rentmycar`. Na mijn akkoord het packagediagram, het architectuurdiagram en de tekst geschreven.
- **Mijn eis:** de AI moet steeds aangeven waar een keuze afwijkt van de les, de docent of de gastdocent. Dat staat nu in een tabel in hoofdstuk 3.1. Afwijkingen: geen Koin (wel aanbevolen op Brightspace), en de domeinklassen staan in de server in plaats van in de gedeelde module (anders dan in de demo van de docent).
- **Gevolg voor eerdere keuzes:** de brandstofprijs is verplaatst van de enum `FuelType` naar `CostAssumptions`, omdat `FuelType` naar de gedeelde module gaat en de app de prijzen niet nodig heeft. Hoofdstuk 2, het klassendiagram en `aannames.md` zijn hierop aangepast.

### 2026-10-05 – ERD en databasemodel (hoofdstuk 2.4 en 2.5)
- **Doel:** bepalen hoe het OO-model in databasetabellen wordt opgeslagen.
- **Wat de AI voorstelde:** drie manieren om overerving in tabellen op te slaan (één tabel, een tabel per soort met een gedeelde tabel, een losse tabel per soort), met voor- en nadelen. De AI gaf aan dat dit niet uit het lesmateriaal komt maar uit het boek van Martin Fowler.
- **Mijn beslissingen:**
  - Overerving in één tabel (optie A), omdat dit het eenvoudigst is.
  - **Auto's niet echt verwijderen maar op "niet actief" zetten.** De AI adviseerde om oude reserveringen mee te verwijderen omdat dat eenvoudiger is. Ik heb gekozen voor soft delete, omdat ik vorig jaar heb geleerd dat niet echt verwijderen vaak beter is: zo blijft de geschiedenis bewaard.
  - De huurprijs wordt bij het aanvragen vastgelegd (advies van de AI overgenomen), zodat een latere tariefwijziging een bestaande reservering niet verandert.
- **Gevolgen:** de analyse (FE-06, FE-13, nieuwe paragraaf "Verwijderen van een auto") en het klassendiagram (`isActive`, `deactivate()`, `rentalPrice()`, `estimatedPrice` als vast gegeven) zijn aangepast.

### 2026-10-05/06 – Endpoints, foutafhandeling en beveiliging (hoofdstuk 3.2 t/m 3.4)
- **Doel:** het API-contract vastleggen (endpoints, request/response, statuscodes) en bepalen hoe inloggen en beveiliging werken.
- **Wat de AI deed:** de routes, StatusPages en authenticatie in de demo van de docent bekeken en een voorstel gedaan voor de endpoints, de foutafhandeling en de beveiliging.
- **Waar ik het AI-voorstel heb laten verbeteren:** de AI stelde eerst voor: tokens 24 uur geldig, wachtwoorden met BCrypt en geen rate limiting. Ik twijfelde aan alle drie (een kort token is toch veiliger? stond er niet iets over wachtwoorden in de gastles? waarom geen rate limiting?) en vroeg om meer onderzoek naar best practices. Uitkomst:
  - De 24 uur kwam uit een oud oefenvoorbeeld (les 6). OWASP en de gastdocent adviseren 15 minuten → aangepast.
  - De gastdocent gebruikt Argon2id, en OWASP noemt BCrypt alleen nog voor oudere systemen → aangepast naar Argon2id.
  - De enige reden voor geen rate limiting was "eenvoud", terwijl de docent, de gastdocent en OWASP het allemaal doen, en het maar een paar regels code is → toegevoegd.
- **Refresh tokens:** ik vroeg wat er na 15 minuten gebeurt en of er een voorbeeld is in het lesmateriaal. De AI heeft de studiedocumenten, alle slides, de repositories van de docent en 14 repositories van de gastdocent doorzocht. Er is nergens een uitgewerkt voorbeeld; de gastdocent noemt refresh tokens wel "the usual pattern" (ktor-fundamentals les 9, in de sprekersnotities). Besluit: in periode 1 een token van 15 minuten zonder refresh token; refresh tokens gepland voor periode 2, met de Ktor-documentatie als bron.
- **Mijn beslissingen:** de lijst met endpoints; één `PATCH .../status` voor goedkeuren, afwijzen en annuleren (zoals de docent); de beveiligingskeuzes hierboven.
- **Les:** bij beveiliging niet het eerste AI-antwoord overnemen. Mijn eigen twijfel ("is korter niet veiliger?") klopte, en extra onderzoek leverde betere en beter onderbouwde keuzes op.

### 2026-10-06 – Sequence diagrams (hoofdstuk 3.6)
- **Doel:** laten zien hoe de onderdelen samenwerken bij de belangrijkste verzoeken.
- **Wat de AI voorstelde:** drie sequence diagrams (inloggen, reservering aanvragen, aanvraag goedkeuren); de opdracht vraagt er minimaal 3.
- **Mijn beslissing:** ook "auto aanbieden" toevoegen, zodat alle belangrijke flows (beveiliging, aanmaken met overerving, domeinregels en de overlapcontrole) zijn uitgewerkt. Totaal 4 diagrammen.
- **Wat de AI deed:** de vier diagrammen gemaakt in de gezamenlijke stijl, met ook de foutpaden, en als plaatje gecontroleerd.
- **Gevolgen voor het ontwerp:** bij het uitwerken bleken twee details nodig, die de AI heeft gemeld en verwerkt:
  - een fabrieksfunctie `Reservation.request()` in een companion object, die de regels controleert en de prijs vastlegt;
  - `approve(now)` krijgt de huidige tijd mee, omdat goedkeuren alleen vóór de start kan. Tijd als parameter maakt de functies ook beter testbaar.
  - Daarnaast zijn `PasswordHasher` en `TokenService` toegevoegd aan het packagediagram.

### 2026-10-06 – Controle van alle diagrammen
- **Wat ik zag:** in het sequence diagram "Aanvraag goedkeuren" stonden Nederlandse teksten tussen de Engelse code, bijvoorbeeld `goedgekeurde reservering` en `request (of null)`.
- **Wat ik vroeg:** controleren of de taal klopt, en voor de zekerheid alle diagrammen nalopen op juistheid en volledigheid.
- **Afspraak voor alle diagrammen:** namen van klassen en functies, teruggegeven waarden en foutmeldingen zijn Engels (dat is code). Voorwaarden, notities, titels en legenda's zijn Nederlands (dat is uitleg in het document). Voor "of null" wordt de Kotlin-notatie gebruikt, bijvoorbeeld `User?`.
- **Wat de AI verder vond bij de controle:**
  - in het sequence diagram "Inloggen" had een pad geen label;
  - in het architectuurdiagram ontbrak rate limiting bij de plugins;
  - in het ERD ontbraken een paar gegevens uit hoofdstuk 1.4 (wachtwoord-hash, bouwjaar, zitplaatsen, aanschafprijs, verbruik);
  - in het packagediagram ontbraken de eigen exceptions en `ApiError`.
- Alles is aangepast en alle tien diagrammen zijn opnieuw als SVG gemaakt.
- **Les:** een AI maakt kleine inconsistenties die je pas ziet als je het resultaat zelf goed bekijkt.

### 2026-10-06 – Opmaak van de diagrammen gelijk getrokken
- **Wat ik vroeg:** alle diagrammen nalopen op leesbaarheid, zorgen voor één gezamenlijke opmaak, geen tekst door lijnen, en waar nuttig grotere of vette tekst (maar niet alles).
- **Wat de AI deed:**
  - de gezamenlijke stijl aangepast: iets grotere letters, vaste regel "titels, namen en kopjes vet, gewone tekst niet", dezelfde stijl voor legenda's en actoren;
  - alle titels in dezelfde vorm gezet: "Soort diagram – onderwerp";
  - voor het klassendiagram, het packagediagram, het ERD en het databasemodel een andere tekenmotor (ELK) gekozen, die rechte lijnen trekt en labels niet over lijnen zet;
  - alle diagrammen opnieuw als SVG gemaakt en als plaatje bekeken.
- **Wat niet werkte:** ELK maakte het use case diagram veel te breed en onleesbaar; daar is de oude tekenmotor gebleven. Een nieuwe instelling voor de breedte van klassen zette de namen van de actoren scheef; die is weer weggehaald.

### 2026-10-06 – Teststrategie (hoofdstuk 4)
- **Doel:** vóór het programmeren vastleggen welke tests er komen, zodat ze bij elke stap meteen worden gemaakt.
- **Wat de AI deed:** de testles van de gastdocent (ktor-fundamentals les 8) en de tests van de docent (`ExposedRepositoriesTest.kt`, `.http`-flows) bekeken, en een voorstel gedaan: de testpiramide, 13 unit tests, 11 integratietests, `.http`-flows per soort gebruiker en een testrapport met alle endpoints.
- **Afwijkingen die de AI meldde:** `kotlin.test` in plaats van Kotest (gastdocent), H2 in het geheugen in plaats van Testcontainers, en geen dekkingspercentage.
- **Mijn beslissingen:** akkoord met de hoeveelheid tests (de tests met ✱ eerst), met `kotlin.test` en H2, en met een testrapport dat na het bouwen wordt ingevuld.
- **Nog te doen:** de AI kan helpen testgevallen te bedenken, maar ik controleer zelf of de verwachte uitkomsten kloppen (bijvoorbeeld door de rekenvoorbeelden na te rekenen).

### 2026-10-06 – Eerste code: enums en eenvoudige domeinklassen (branch `feature/domain-classes`)
- **Werkwijze:** de AI legt in de chat uit hoe iets gemaakt wordt; ik typ de code zelf. Bij het eerste bestand (`FuelType`) heeft de AI een voorbeeld gegeven; `ReservationStatus`, `Address`, `Photo` en `User` heb ik zelf geschreven. De AI heeft alleen de lege bestanden met de package-regel aangemaakt.
- **Wat de AI uitlegde:** wat een enum is, het verschil tussen `class` en `data class` (structural en referential equality, `toString()`, `copy()`), en het verschil tussen `val` en `var`.
- **Fouten die ik maakte en die de AI vond bij het controleren:**
  - code uit de terminal gekopieerd, waardoor er onzichtbare tekens (`│` en tekst van het terminalvenster) in `FuelType.kt` stonden;
  - `string` en `int` met een kleine letter; types beginnen in Kotlin met een hoofdletter;
  - `Photo` en `User` als `data class` gemaakt; het moeten gewone klassen zijn, omdat ze een eigen `id` hebben en omdat de `toString()` van een data class het wachtwoord-hash zou tonen;
  - in `User` alles `val`, terwijl naam, e-mail, wachtwoord-hash en bonuspunten moeten kunnen veranderen.
- **Aanpassing ontwerp:** `fileName` van `Photo` is in het klassendiagram `{readOnly}` geworden, omdat een bestandsnaam nooit verandert.
- **Ook gevonden:** het bouwen van de Android-kant van `core` vraagt om het accepteren van de Android SDK-licenties; tot die tijd wordt alleen de server-kant gebouwd (`-p jvm`).

### 2026-10-06 – `CostAssumptions` (door de AI geschreven)
- **Wat ik vroeg:** na de uitleg over `object` (singleton) en `const val` heb ik de AI gevraagd deze stap helemaal zelf te maken.
- **Wat de AI deed:** `server/src/common/CostAssumptions.kt` geschreven met alle vaste waarden uit `aannames.md` en de functie `fuelPrice()`, die met `when` de literprijs per brandstofsoort teruggeeft. De build is gecontroleerd.
- **Hoe ik het controleer:** de waarden vergelijken met de tabellen in `aannames.md`, en ik moet kunnen uitleggen wat een `object`, `const val` en de `when` zonder `else` doen.

### 2026-10-06 – `Car` met overerving (gedeeltelijk door de AI)
- **Wat de AI uitlegde:** overerving, abstracte functies met `override`, de sealed class, polymorfisme, het `init`-blok met `require()` en `private set` (encapsulatie).
- **Taakverdeling:** op mijn verzoek heeft de AI `Car.kt` en `IceCar.kt` gemaakt als voorbeeld. `ElectricCar.kt` en `HydrogenCar.kt` schrijf ik zelf op dezelfde manier.
- **Ontwerpkeuze (voorstel van de AI):** de gegevens van een auto zijn `val` in plaats van `var`. Bij het wijzigen van een auto wordt een nieuw object gemaakt, zodat de controles in `init` altijd worden uitgevoerd. Alleen `isActive` kan veranderen, en alleen via `deactivate()`. Het klassendiagram is hierop aangepast (`{readOnly}`).

### 2026-10-05 – Afspraken over AI-gebruik en dit logboek
- **Doel:** bepalen hoe ik AI inzet en verantwoord.
- **Afspraak:** de AI mag code en tekst voorstellen, maar ik bepaal wat er gebouwd wordt, lees alles door en neem alleen over wat ik kan uitleggen. Dit logboek beschrijft eerlijk wat de AI deed en wat ik deed.
- **Les:** bij het assessment wordt gekeken hoe kritisch en verantwoord AI is gebruikt, niet of er weinig AI is gebruikt.

### 2026-10-22 – Studiegids bij de code (door de AI geschreven)
- **Wat ik vroeg:** een uitgebreid PDF-document dat de code uitlegt om mee te studeren: wat dingen betekenen, waarom ze zo gedaan zijn, hoe je ze noemt (bijvoorbeeld wat een `companion object` is en waarom het gebruikt wordt) en de opbouw van de code.
- **Wat de AI deed:** alle bestanden in `core/src`, `server/src` en `server/test` gelezen en `docs/Studiegids-Code-RentMyCar.pdf` gemaakt (ongeveer 65 pagina's): opbouw en naamgeving, libraries, web-API-begrippen, uitleg per laag en per bestand, Kotlin-begrippen, ontwerppatronen, oefenvragen en aandachtspunten. De code zelf is niet veranderd.
- **Hoe ik het controleer:** de uitleg naast de code leggen en de rekenvoorbeelden en oefenvragen zelf nalopen. Wat ik niet kan uitleggen, vraag ik na.

### 2026-10-24 – Functies met `return` in plaats van `=` (door de AI omgezet)
- **Wat ik vroeg:** of de code eenvoudiger kan. Ik vind functies met `{ return ... }` beter te lezen dan functies met `=` (expression body), en vroeg de AI dat in het hele project om te zetten.
- **Wat de AI deed:** 47 functies in 21 bestanden (`core/src`, `server/src` en `server/test`) omgezet naar een block body met `return`. Bij de repositories staat de `return` vóór `transaction(database) { ... }`, omdat een `return` in de lambda niet mag. Er is niets aan de werking veranderd; alle servertests slagen nog.
- **Niet omgezet:** de testfuncties in `ApiTest.kt` (`fun ... = apiTest { client -> ... }`), omdat dan de hele test een niveau moet inspringen.
- **Wat de AI ook voorstelde (nog niet gedaan):** minder `it` gebruiken en de parameter van een lambda een eigen naam geven, andere vereenvoudigingen zoals benoemde argumenten in `toCar()`/`makeCar()` en `return newCar` in `updateCar()`.
- **Vervolg (zelfde dag):** op mijn verzoek heeft de AI ook alle `it` in het project vervangen door een eigen naam voor de parameter van de lambda (bijvoorbeeld `{ car -> car.isActive }` en `{ row -> row[UserTable.name] = name }`). Reden: ik vind `it` lastig te lezen. Werking niet veranderd; alle servertests slagen.
- **Vervolg 2 (zelfde dag):** op mijn verzoek heeft de AI korte schrijfwijzen in het hele project uitgeschreven, omdat ik die moeilijk te lezen vind:
  - `?: throw` en `?: return null` → eerst een `val`, dan `if (x == null) { ... }`;
  - `!!` → een `if` met een duidelijke foutmelding; in `updateCar()` gewoon `return newCar`;
  - routes: `call.intParameter("id")` en `call.userId()` eerst in een eigen `val` in plaats van binnen een andere aanroep;
  - `?.let { }` en `?.toDouble()` bij het zoeken van auto's → `var` met `if`;
  - `.any { }` → een `for`-lus met een `if` (`deleteCar()` en `approve()`);
  - `if ... else` op één regel, `return try`, `if (...) return false` en de `repeat`-lus in `Rounding.kt` uitgeschreven;
  - benoemde argumenten (`brand = ...`) bij het maken van auto's en reserveringen (`toCar()`, `makeCar()`, `DemoData`, `TestData`);
  - in `toCar()` en `makeCar()` is de `when` vervangen door losse `if`-blokken met elk een eigen `return`.
- **Bewust niet veranderd:** `cause.message ?: "..."` in `StatusPages.kt` (6 keer hetzelfde, korte patroon).
- **Controle:** alle servertests slagen. De werking is gelijk gebleven; alleen de foutmeldingen bij kapotte data in de database zijn duidelijker dan de oude `NullPointerException` van `!!`. Daarvoor wordt `RuntimeException` gebruikt (geeft 500), niet `IllegalStateException`, want die wordt in `StatusPages` een 409.
- **Vervolg 3 (zelfde dag):** de AI legde uit wat `override` is (een functie uit de interface `CarRepository` invullen; `ExposedCarRepository` en `FakeCarRepository` doen dat allebei). Daarna op mijn verzoek in alle drie de repositories de zoekfuncties in stappen geschreven: eerst in `transaction` alleen de rij(en) ophalen (`val query = ...`), daarna met een `if (row == null)` of een `for`-lus de objecten maken. `.map { }` is overal vervangen door een `for`-lus, ook in de routes. Alle servertests slagen.
- **Vervolg 4 (zelfde dag):** op mijn verzoek nog meer uitgeschreven:
  - `CarService.search()`: één `for`-lus met `continue` in plaats van drie keer `.filter { }`;
  - lange voorwaarden een naam gegeven (`isRenter`, `isOwner`, `isOpen`) en `car == null || !car.isActive` gesplitst in twee `if`'s;
  - `changeStatus()`: afwijzen en annuleren in eigen functies `reject()` en `cancel()`, net als `approve()`;
  - constructorparameters hernoemd naar `initialStatus` (`Reservation`) en `initiallyActive` (`Car` en subklassen), zodat ze niet meer dezelfde naam hebben als de property met `private set`;
  - `Fakes.kt` (tests): `.find`, `.filter`, `.any` en `removeIf` vervangen door `for`-lussen.
- **Bewust niet veranderd:** `require`/`check`, de builder-ketens in `TokenService` en `PasswordHasher`, en extension functions zoals `toResponse()`.
- Alle servertests slagen.
- **Vervolg 5 (zelfde dag):** ik wilde zien welk type elke `val` heeft. De AI liet zien hoe dat met types in de code zou gaan, maar ik heb gekozen voor de inlay hints van IntelliJ (types grijs in de editor), zodat de code zelf niet langer wordt. Daarna op mijn verzoek:
  - vaste getallen een naam gegeven: de Argon2-instellingen in `PasswordHasher`, `MAX_ATTEMPTS_PER_MINUTE` (rate limiting), `EURO_DECIMALS` en `COST_PER_KM_DECIMALS`, en `validMilliseconds` in `TokenService`;
  - meer benoemde argumenten: `Dependencies`, `User(...)`, `Address(...)`, `users.add(...)` en `Reservation(...)` in demodata en tests;
  - `hoursBetween()` in stappen, met uitleg waarom UTC;
  - alle tests in drie delen: Arrange (klaarzetten), Act (uitvoeren), Assert (controleren); de tests in `ApiTest` hebben nu ook een `{ }`-body.
- **Diagram:** in `sequence-approve-reservation.puml` stond nog `approved.any { it.overlapsWith(request) }`; aangepast naar de `for`-lus. Let op: de SVG is geëxporteerd met een eerdere, langere versie van die regel; de laatste export werd geweigerd (PlantUML-jar van internet). Nog opnieuw exporteren.
- Alle servertests slagen.
- **Vervolg 6 (zelfde dag):** `Car.toResponse()` uitgeschreven: eerst vier `var`'s (standaard `null`), dan per soort auto een `if (this is ...)` (smart cast), dan één `CarResponse(...)`. Daarmee is de `when` met `copy()` weg. `AddressDto` heeft nu benoemde argumenten. In `CarTable` heet `kwhPer100Km` nu `kWhPer100Km`, net als in de rest van de code; de kolomnaam in de database (`kwh_per_100km`) is niet veranderd. Alle servertests slagen.
- **Afspraak:** hierna stoppen met herschrijven. Volgende stappen: de server zelf testen met de `.http`-bestanden, committen en oefenen met uitleggen (ook van de korte schrijfwijzen).
- **Vervolg 7 (zelfde dag):** de AI legde uit waar de databasecode ingewikkeld van wordt (één tabel voor drie soorten auto's: eigen ontwerpkeuze, al de eenvoudigste; de eigenaar meeladen met een join) en welke andere manieren er zijn (Exposed DAO, JDBC, alleen lijsten). Advies: Exposed DSL houden. Op mijn verzoek laadt `ExposedCarRepository` de eigenaar nu via `userRepository.findById(...)` in plaats van `CarTable innerJoin UserTable`, net zoals `ExposedReservationRepository` dat doet. `Dependencies.kt`, `CarRepositoryTest` en het voorbeeld in `docs/03-web-api.md` zijn aangepast. Alle servertests slagen.
- **Vervolg 8 (zelfde dag):** ketens van aanroepen (builder-patroon) opgesplitst in losse regels: eerst `val builder = ...`, dan per regel `builder.with...(...)`, en als laatste `val token = builder.sign(...)` / `builder.build()`, gevolgd door `return`. Gedaan in `TokenService.createToken()`, de `verifier` (nu in een eigen functie `createVerifier()`) en `PasswordHasher.calculateHash()`. Ook de queries in de drie repositories: `val query = X.selectAll()`, dan `query.where { ... }` en `query.orderBy(...)`. De AI heeft eerst in de broncode van java-jwt, BouncyCastle en Exposed gecontroleerd dat deze methodes het object zelf aanpassen (`return this`), anders zou opsplitsen niet werken. Alle servertests slagen.
- **Vervolg 9 (zelfde dag):** de AI heeft op mijn vraag eerlijk opgesomd wat er méér is veranderd dan alleen de schrijfwijze (geen libraries aangepast; wel o.a. `when` → losse `if`'s in `toCar()`/`makeCar()`, waardoor de compiler niet meer controleert of alle soorten auto's behandeld zijn, en de eigenaar via `userRepository`, wat per auto een extra database-vraag geeft: het N+1-probleem). Dit zijn twee afwegingen die ik moet kunnen uitleggen.
- Daarna op mijn verzoek berekeningen en controles die direct achter `return` stonden in stappen met een naam gezet: `tco()` (`depreciation`, `fixedCosts`, `energyCosts`), `rentalPrice()`, `costPerKm()` en `fixedCostsPerYear()` in de drie soorten auto's, `overlapsWith()`, `PasswordHasher.hash()` en `verify()`, `isValidEmail()`, `userId()`, `atTime()`/`plusHours()` in de demodata en `rounded()` (met rekenvoorbeeld). Alle servertests slagen, ook de tests van de kostenberekening (UT-01, UT-03) en de overlap (UT-05).
- **Vervolg 10 (zelfde dag):** de AI legde uit wat `eq` is (Exposed, "equals", wordt `=` in SQL; geen `==` omdat het een voorwaarde voor de database bouwt). Een eigen functie met een langere naam heb ik niet laten maken (extra laag). In plaats daarvan staat nu boven elke query in de drie repositories in gewone woorden wat hij doet, met de SQL erbij, bijvoorbeeld `// In SQL: SELECT * FROM cars WHERE id = <id>`. Alle servertests slagen.
- **Vervolg 11 (zelfde dag):** op mijn verzoek heeft elke functie (132 in `server/src`, `core/src` en `server/test`) nu precies één regel commentaar erboven. Bij een `override` staat in dezelfde regel kort waarom, bijvoorbeeld `(override: fills in the function from CarRepository)` of `(override: every kind of car calculates this in its own way)`. Daarna is al het commentaar in het project teruggebracht tot één regel per keer (35 blokken samengevoegd); bij de queries in de vorm `// Find the user with this email address (SQL: SELECT * FROM users WHERE email = <email>)`. Uitleg die niet meer boven een functie paste (bijvoorbeeld waarom een fout e-mailadres en een fout wachtwoord hetzelfde antwoord geven) staat nu in de functie bij de regel waar het over gaat. Alle servertests slagen.
- **Vervolg 12 (andere dag):** op mijn verzoek staat bij elke route een commentaarregel met een voorbeeldlink, zoals `// Get one car, for example /api/cars/1` (bij POST, PUT, PATCH en DELETE met de methode ervoor). Daarna heb ik vragen gesteld over `core` en `server`, DTO's, `Route.carRoutes()` (extension function, geen subklasse; in de routeboom een child van `/api`), `routing` (komt van Ktor), `GreetingUtil.kt` (uit het projectsjabloon, gebruikt door het voorbeeldscherm van de app; opruimen in periode 2) en `sealed` (je kunt geen `Car` zelf maken, alleen een subklasse, en `when` heeft geen `else` nodig).
- Daarna heeft de AI, net als bij `override`, achter het commentaar tussen haakjes kort uitgelegd wat een Kotlin-woord doet: `abstract`, `sealed`, `private set`, `companion object`, `interface`, `object`, `data class` (één keer per bestand), `enum` en extension functions. De teksten heb ik samen met de AI aangepast tot ze duidelijk genoeg waren: bij `sealed` mijn eigen formulering ("you cannot make a Car itself, only a subclass like IceCar"), bij extension "extends ... with a function", en bij `companion object` "belongs to the class, not to one car". Bij `const val` en `init` niet, want die zijn duidelijk genoeg. "see chapter 3.3" is weggehaald uit `StatusPages.kt`. Alleen commentaar veranderd; alle servertests slagen. Daarna is "(see chapter 3.4)" uit `Application.kt` gehaald (geen verwijzingen naar hoofdstukken meer in de code; verwijzingen naar `docs/aannames.md` blijven) en staat bij `fixedCostsPerYear()` nu `(override: every kind of car has its own values)`, omdat die functie niets uitrekent maar vaste bedragen optelt.
- **Vervolg 13 (zelfde dag):** op mijn verzoek staat de `return` nu ook bij een `when` onderaan: eerst `val type = when (this) { ... }` en dan `return type` in `Car.type()`, en hetzelfde met `val price` in `CostAssumptions.fuelPrice()`. Dat waren de enige twee plekken met `return when`. De controle van `sealed` en `enum` blijft werken: de `when` moet nog steeds voor elke soort een antwoord hebben. Daarna ook `val carExists = numberOfCars > 0` in `existsByLicensePlate()` en `val hours = minutes / 60.0` in `hoursBetween()`. Alle servertests slagen.
- **Vervolg 14 (zelfde dag):** de AI zocht in de servercode naar meer korte schrijfwijzen en deed zes voorstellen. Ik heb er vier afgewezen, omdat ik de code zo leesbaar genoeg vind: de filters in `search()`, `currentTime()` in stappen, benoemde argumenten in `DemoData` (die zie ik grijs in de editor) en benoemde argumenten in de aanroep `Car(...)` in de subklassen. Wel gedaan: in `StatusPages.kt` is `cause.message ?: "..."` (6×) vervangen door de hulpfunctie `messageOf()` met een gewone `if`. In `Application.kt` staat nu uitleg bij `validate` (de laatste waarde in de lambda is het resultaat) en bij `challenge { _, _ -> }` (`_` betekent: deze waarde wordt niet gebruikt). Alle servertests slagen.
- **Vervolg 15 (zelfde dag):** ik vroeg of de rate limiter en de JWT-token net zo zijn gemaakt als bij de docent. De AI heeft de code van de docent (`pjfmast/ktor-android-incident-demo`) en de auth-demo van de gastdocent (`nomisRev/avans-toolchain-and-ktor-2026`) opgehaald en vergeleken. De rate limiter is bij alle drie gelijk (10 per minuut); de token leek vooral op die van de gastdocent. Ik wilde het meer laten lijken op de docent, met mijn verbeteringen erbij. Eerst vroeg ik of 9 tekens niet genoeg is voor het secret; de AI legde uit dat een JWT-secret geen wachtwoord is maar een sleutel die offline geraden kan worden, en dat HS256 minstens 32 bytes vraagt (RFC 7518, OWASP). De AI gaf ook eerlijk aan dat `application.conf` de code niet eenvoudiger maakt; ik heb toch gekozen voor de opzet van de docent.
- **Wat er veranderd is:** nieuw `server/resources/application.conf` zoals bij de docent (`server` en `jwt`, met `${?ENV}`); `ServerConfig` en `AppConfig` in `Server.kt`, `JwtConfig` in `TokenService.kt`; `TokenService(jwtConfig)` met `realm`, zoals `JwtService` van de docent; issuer en audience uit de config in plaats van de `companion object`; `createDependencies(databaseUrl, jwtConfig)`; in de tests `TEST_JWT_CONFIG`. **Behouden als verbetering:** 15 minuten, `sub`, verplichte claims, minstens 32 tekens (nu in `JwtConfig`), de waarschuwing zonder `JWT_SECRET`, en inloggen met Argon2id in `UserService` (de docent controleert het wachtwoord als gewone tekst in `JwtService`). `docs/03-web-api.md` aangepast, ook een onjuiste zin dat de geldigheid in een configuratiebestand stond.
- **Controle:** alle servertests slagen; daarnaast de server echt gestart: de waarschuwing verschijnt, inloggen geeft een token met `iss` en `aud` uit `application.conf` en 900 seconden geldigheid, `/api/users/me` geeft 200 met token en 401 zonder.
