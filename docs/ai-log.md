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

### 2026-10-05 – Afspraken over AI-gebruik en dit logboek
- **Doel:** bepalen hoe ik AI inzet en verantwoord.
- **Afspraak:** de AI mag code en tekst voorstellen, maar ik bepaal wat er gebouwd wordt, lees alles door en neem alleen over wat ik kan uitleggen. Dit logboek beschrijft eerlijk wat de AI deed en wat ik deed.
- **Les:** bij het assessment wordt gekeken hoe kritisch en verantwoord AI is gebruikt, niet of er weinig AI is gebruikt.
