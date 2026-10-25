# 5. Installatie en gebruik

## 5.1 Vereisten

| Wat | Toelichting |
|---|---|
| Windows, macOS of Linux | De applicatie draait lokaal op de eigen computer (NFE-08). |
| Internetverbinding bij de eerste start | De scripts `kotlin.bat` (Windows) en `kotlin` (macOS/Linux) in de hoofdmap downloaden bij de eerste keer zelf de Kotlin Toolchain en een Java-runtime. Een aparte installatie van Java of Gradle is niet nodig. |
| IntelliJ IDEA (aanbevolen) | Voor het lezen van de code en het uitvoeren van de `.http`-bestanden met de ingebouwde HTTP Client. |

Er is geen aparte databaseserver nodig: de H2-database draait mee in de applicatie.

## 5.2 Project starten

Open een terminal in de hoofdmap van het project.

| Actie | Windows | macOS/Linux |
|---|---|---|
| Server starten | `.\kotlin.bat run -m server` | `./kotlin run -m server` |
| Tests van de server uitvoeren | `.\kotlin.bat test -m server` | `./kotlin test -m server` |

De server draait daarna op **http://localhost:8080**. Alle endpoints beginnen met `/api` (zie hoofdstuk 3.2). Stoppen gaat met `Ctrl+C`.

**Instellingen.** De instellingen staan in `server/resources/application.conf`. Elke instelling kan worden overschreven met een omgevingsvariabele:

| Omgevingsvariabele | Betekenis | Standaard |
|---|---|---|
| `PORT` | Poort van de server | `8080` |
| `HOST` | Adres waarop de server luistert | `0.0.0.0` |
| `JWT_SECRET` | Geheime sleutel voor de tokens, minstens 32 tekens | Een sleutel voor de eigen computer |
| `JWT_ISSUER` | Uitgever van de tokens | `http://localhost:8080` |
| `JWT_AUDIENCE` | Voor wie de tokens bedoeld zijn | `rentmycar-api` |

Zonder `JWT_SECRET` start de server met de sleutel uit `application.conf` en toont hij een waarschuwing. Dat is goed genoeg voor de eigen computer, maar niet voor een echte server, omdat die sleutel in git staat (zie hoofdstuk 3.4). Een eigen sleutel instellen kan zo (Windows PowerShell):

```powershell
$env:JWT_SECRET = "een-lange-geheime-sleutel-van-minstens-32-tekens"
.\kotlin.bat run -m server
```

**Database.** De gegevens worden opgeslagen in de map `data/` (`data/rentmycar.mv.db`) en blijven bewaard na een herstart. Deze map staat niet in git. Wie opnieuw wil beginnen met alleen de testdata, stopt de server, verwijdert de map `data/` en start de server opnieuw.

## 5.3 Testdata en testaccounts

Bij de start controleert de server of de database leeg is. Is dat zo, dan vult `DemoData.kt` de database met realistische testdata (NFE-02).

**Testaccounts** (wachtwoord voor alle accounts: `Welkom123!`):

| Naam | E-mailadres | Rol in de testdata |
|---|---|---|
| Eva de Vries | `eva@example.com` | Eigenaar van de Volkswagen, de Peugeot en de Tesla |
| Sanne Jansen | `sanne@example.com` | Eigenaar van de Dacia, de Kia, de Renault en de Toyota |
| Daan Bakker | `daan@example.com` | Huurder met twee reserveringen |

**Auto's:**

| Id | Auto | Soort | Plaats | Uurtarief | Eigenaar |
|---|---|---|---|---|---|
| 1 | Volkswagen Golf | ICE (benzine) | Breda | € 6,00 | Eva |
| 2 | Peugeot 308 SW | ICE (diesel) | Breda | € 5,50 | Eva |
| 3 | Dacia Jogger | ICE (LPG) | Tilburg | € 5,00 | Sanne |
| 4 | Tesla Model 3 | BEV | Breda | € 8,50 | Eva |
| 5 | Kia EV6 | BEV | Tilburg | € 9,00 | Sanne |
| 6 | Renault Zoe | BEV | Den Bosch | € 4,50 | Sanne |
| 7 | Toyota Mirai | FCEV | Den Bosch | € 10,00 | Sanne |

**Reserveringen van Daan** (de tijden worden berekend vanaf het moment dat de testdata wordt aangemaakt):
- de Volkswagen Golf over twee dagen, 4 uur, status `APPROVED`;
- de Tesla over drie dagen, 3 uur, status `REQUESTED`.

## 5.4 De API gebruiken

De API wordt getest en gedemonstreerd met drie `.http`-bestanden in `server/test/http-requests/`:

| Bestand | Inhoud |
|---|---|
| `visitor-flow.http` | Auto's zoeken en bekijken zonder in te loggen |
| `owner-flow.http` | Als nieuwe eigenaar registreren, inloggen, een auto aanbieden, wijzigen, de kosten bekijken, een aanvraag goedkeuren en de auto verwijderen |
| `renter-flow.http` | Als Daan inloggen, een auto zoeken, een reservering aanvragen, bekijken en annuleren |

**Werkwijze in IntelliJ:**
1. Start de server (zie 5.2).
2. Open een `.http`-bestand en klik op het groene pijltje naast een verzoek.
3. Voer eerst het inlogverzoek uit. IntelliJ bewaart het token automatisch en gebruikt het bij de volgende verzoeken.
4. Een token is 15 minuten geldig. Krijg je daarna een 401, voer dan het inlogverzoek opnieuw uit.

**Let op:**
- Inloggen en registreren mogen maximaal 10 keer per minuut. Daarna geeft de server 429; wacht dan een minuut.
- `owner-flow.http` registreert `fleur@example.com` met kenteken `V-111-FS`. Bij een tweede keer uitvoeren bestaan die al (409). Verwijder dan de map `data/` of pas het e-mailadres en het kenteken aan.

**Voorbeeld met curl** (zonder IntelliJ):

```bash
# Alle elektrische auto's in Breda
curl "http://localhost:8080/api/cars?type=BEV&city=Breda"

# Inloggen als Daan
curl -X POST http://localhost:8080/api/users/login -H "Content-Type: application/json" -d "{\"email\":\"daan@example.com\",\"password\":\"Welkom123!\"}"
```
