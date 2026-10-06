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

### 2026-10-05 – Afspraken over AI-gebruik en dit logboek
- **Doel:** bepalen hoe ik AI inzet en verantwoord.
- **Afspraak:** de AI mag code en tekst voorstellen, maar ik bepaal wat er gebouwd wordt, lees alles door en neem alleen over wat ik kan uitleggen. Dit logboek beschrijft eerlijk wat de AI deed en wat ik deed.
- **Les:** bij het assessment wordt gekeken hoe kritisch en verantwoord AI is gebruikt, niet of er weinig AI is gebruikt.
