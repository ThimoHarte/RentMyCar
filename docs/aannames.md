# Aannames

In dit document staan de vaste waarden en formules die de applicatie gebruikt voor de berekeningen. Prijzen veranderen; de waarden hieronder gelden op de peildatum. In de code staan ze op één plek, zodat ze makkelijk kunnen worden aangepast.

Peildatum: 5 oktober 2026.

## Formules

**Kosten per kilometer** (gebruikt bij FE-08 en de TCO)

> kosten per km = verbruik per 100 km × energieprijs ÷ 100

De formule is voor alle soorten auto's gelijk, maar elke soort heeft een eigen verbruik en energieprijs. Het verbruik vult de eigenaar in; de energieprijzen staan hieronder.

**TCO per jaar** (FE-09)

> TCO = afschrijving + verzekering + wegenbelasting + onderhoud + energiekosten
>
> afschrijving = aanschafprijs × 15%
>
> energiekosten = 15.000 km × kosten per km

**Geschatte huurprijs** (FE-13)

> huurprijs = uurtarief × aantal uren + km-tarief × verwachte kilometers

## Energieprijzen

| Energie | Prijs | Bron |
|---|---|---|
| Benzine (Euro95 / E10) | € 2,42 per liter | Gemiddelde prijs volgens de ANWB ([anwb.nl](https://www.anwb.nl/auto/brandstof/benzineprijs)), oktober 2026 |
| Diesel (B7) | € 2,50 per liter | Gemiddelde prijs volgens de ANWB ([anwb.nl](https://www.anwb.nl/auto/brandstof/benzineprijs)), oktober 2026 |
| LPG | € 0,99 per liter | Mediaan van alle pompen ([hoeveelbenzine.nl](https://hoeveelbenzine.nl/lpg-prijs-vandaag)), 5 oktober 2026 |
| Elektriciteit | € 0,34 per kWh | Mix van 70% thuis laden (€ 0,28) en 30% publiek laden (€ 0,49) ([joulo.nl](https://joulo.nl/blog/wat-kost-opladen-elektrische-auto-nederland)), 2026 |
| Waterstof | € 21,00 per kg | Gemiddelde pompprijs; prijzen liggen tussen € 16,95 en € 25 per kg ([anwb.nl](https://www.anwb.nl/auto/brandstof/waterstof-tanken), [veel.nl](https://veel.nl/waar-kun-je-waterstof-tanken-in-nederland/)), 2026 |

**Keuze bij elektriciteit:** de prijs per kWh hangt sterk af van waar je laadt. Ik ga uit van een mix van vooral thuis laden en soms publiek laden. Snelladen laat ik buiten beschouwing.

## Voorbeeldverbruik

Het verbruik vult de eigenaar per auto in. Voor de testdata en de voorbeelden gebruik ik deze gangbare waarden:

| Soort | Voorbeeldverbruik | Kosten per km | Bron verbruik |
|---|---|---|---|
| ICE benzine | 6 liter / 100 km | € 0,145 | eigen aanname, gangbaar voor een middenklasser |
| ICE diesel | 5 liter / 100 km | € 0,125 | eigen aanname |
| ICE LPG | 8 liter / 100 km | € 0,079 | eigen aanname |
| BEV | 17 kWh / 100 km | € 0,058 | [joulo.nl](https://joulo.nl/blog/wat-kost-opladen-elektrische-auto-nederland) |
| FCEV | 1 kg / 100 km | € 0,210 | [anwb.nl](https://www.anwb.nl/auto/brandstof/waterstof-tanken) |

## TCO-bedragen per jaar

Uitgangspunt: een middenklasse auto en 15.000 km per jaar.

| Onderdeel | ICE | BEV | FCEV | Bron / toelichting |
|---|---|---|---|---|
| Afschrijving | 15% van aanschafprijs | 15% van aanschafprijs | 15% van aanschafprijs | Vereenvoudiging: een vast percentage per jaar |
| Verzekering | € 800 | € 1.100 | € 1.100 | Schatting. Een elektrische auto is € 240 – € 360 per jaar duurder om te verzekeren ([anwb.nl](https://www.anwb.nl/auto/elektrisch-rijden/kosten/elektrische-auto-vs-benzine-vs-hybride)). Voor FCEV neem ik hetzelfde aan als voor BEV. |
| Wegenbelasting | € 1.000 | € 710 | € 710 | Middenklasser: benzine ca. € 83 per maand, elektrisch ca. € 59 per maand. Elektrische en waterstofauto's krijgen in 2026 30% korting ([evtrader.com](https://evtrader.com/nl-nl/kennisbank/subsidies-en-belasting/motorrijtuigenbelasting-elektrische-auto-2026)). |
| Onderhoud | € 650 | € 385 | € 600 | Gemiddelden voor een middenklasser ([echteautokosten.nl](https://echteautokosten.nl/auto-onderhoud-kosten-2026/)). Voor FCEV geen betrouwbare bron gevonden; eigen schatting. |
| Energie | 15.000 × kosten per km | 15.000 × kosten per km | 15.000 × kosten per km | Zie energieprijzen |

**Vereenvoudigingen:**
- Voor ICE gebruik ik één bedrag voor de wegenbelasting. In werkelijkheid betalen diesel- en LPG-auto's een toeslag en hangt het bedrag af van het gewicht en de provincie.
- Afschrijving is in werkelijkheid in de eerste jaren hoger dan later. Een vast percentage is eenvoudiger en voor deze casus voldoende.

## Rekenvoorbeelden

**Benzineauto** van € 30.000, verbruik 6 liter / 100 km:

| Onderdeel | Bedrag |
|---|---|
| Afschrijving (15% × € 30.000) | € 4.500 |
| Verzekering | € 800 |
| Wegenbelasting | € 1.000 |
| Onderhoud | € 650 |
| Energie (15.000 × € 0,1452) | € 2.178 |
| **TCO per jaar** | **€ 9.128** (≈ € 0,61 per km) |

**Elektrische auto (BEV)** van € 35.000, verbruik 17 kWh / 100 km:

| Onderdeel | Bedrag |
|---|---|
| Afschrijving (15% × € 35.000) | € 5.250 |
| Verzekering | € 1.100 |
| Wegenbelasting | € 710 |
| Onderhoud | € 385 |
| Energie (15.000 × € 0,0578) | € 867 |
| **TCO per jaar** | **€ 8.312** (≈ € 0,55 per km) |

**Huurprijs:** een auto met een uurtarief van € 6,00 en een km-tarief van € 0,25, gehuurd voor 4 uur met 80 verwachte kilometers:

> 4 × € 6,00 + 80 × € 0,25 = € 24,00 + € 20,00 = **€ 44,00**

Deze rekenvoorbeelden worden ook gebruikt als testgevallen voor de unit tests (hoofdstuk 4).
