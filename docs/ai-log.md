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
- **Hoe ik het controleerde:** nog te doen: de aangenomen prijzen en bedragen controleren met een paar actuele bronnen en vastleggen in `aannames.md`.

### 2026-10-05 – Documentatie-opzet
- **Doel:** bepalen hoe ik het ontwikkeldocument en de UML-diagrammen maak.
- **Wat ik vroeg:** een manier om documentatie en diagrammen bij te houden.
- **Wat de AI voorstelde:** Markdown-bestanden in `docs/` in Git en diagrammen met PlantUML (tekst → diagram).
- **Hoe ik het controleerde:** de AI de GitHub-repositories van de docent laten bekijken (o.a. `pjfmast/ktor-android-incident-demo`). De docent gebruikt één `ontwikkeldocument.md` en PlantUML-bestanden in `docs/puml/`.
- **Mijn beslissing:** het beste van beide: losse hoofdstukken die aansluiten op de portfolio-checklist van Brightspace, en diagrammen volgens de methode van de docent (`.puml` in `docs/puml/`, geëxporteerd naar `.svg`).
- **Les:** een AI-voorstel vergelijken met hoe de docent het zelf doet, gaf een betere en beter te onderbouwen keuze.

### 2026-10-05 – Afspraken over AI-gebruik en dit logboek
- **Doel:** bepalen hoe ik AI inzet en verantwoord.
- **Afspraak:** de AI mag code en tekst voorstellen, maar ik bepaal wat er gebouwd wordt, lees alles door en neem alleen over wat ik kan uitleggen. Dit logboek beschrijft eerlijk wat de AI deed en wat ik deed.
- **Les:** bij het assessment wordt gekeken hoe kritisch en verantwoord AI is gebruikt, niet of er weinig AI is gebruikt.
