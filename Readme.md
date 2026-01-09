# Inleiding

Je hebt in de vorige opdracht de `AlbumEntity`, `ArtistEntity` en `StockEntity` toegevoegd aan je domeinmodel. Je hebt voor deze entiteiten ook de complete architectuur gebouwd, inclusief DTO's en mappers. Belangrijker nog is dat je relaties hebt gelegd tussen de entiteiten en je functionaliteit hebt ingebouwd om met die relaties te kunnen werken.

In deze opdracht ga je testen schrijven om de kwaliteit van je API te garanderen.

Heb je de opdracht van vorige week niet gemaakt of niet af gekregen, dan kun je de [voorbeeld uitwerkingen](https://github.com/hogeschoolnovi/backend-springboot-vinylshop-relaties-uitwerkingen) clonen.


# Opdrachtbeschrijving

Je gaat in deze opdracht verder werken aan de opdracht die je vorige week gemaakt hebt.

Je schrijft **minstens 10 unittesten** voor de `AlbumService` waarbij je een **line coverage** van **100%** weet te behalen.

Daarnaast schrijf je **2 integratie testen** voor het Genre domein. Schrijf in ieder geval een test voor de `POST /genres`.

Optioneel mag je ook unittesten voor een van de andere service klassen (Waarbij je tevens een line-coverage van 100% weet te behalen): 
- `GenreService`
- `PublisherService`
- `StockService`
- `ArtistService`


# Randvoorwaarden

- Je voldoet aan de randvoorwaarden van de vorige opdracht.
- Je hebt de `AlbumService`-klasse voor 100% (line coverage) getest.
- Je hebt 2 integratietesten, waarvan ten minste `POST /genres`.
- Je hebt de juiste dependencies
- Je tests hebben goede, beschrijvende namen.

# Stappenplan

## Stap 1 (Unittesten)
Er zijn verschillende manieren om de unittesten te implementeren, maar er zijn een aantal vaste elementen. 
- Zet de juiste annotatie boven je testklasse
- Zet de juiste annotatie boven je tests
- Gebruik `@Mock` of `Mockito.mock()` om mocks te maken van alle afhankelijkheden van de klasse onder test.
- Gebruik `@InjectMocks` of roep zelf de constructor aan.
- Gebruik de AAA-structuur
### Arrange
- Maak stubbings (when...thenReturn...) voor alle relevante methode aanroepen van afhankelijkheden (zoals repositories of mappers) in de methode onder test. Maak geen onnodige stubbings, want daar gaat Mockito over klagen.
- Initialiseer alle data die je nodig hebt voor de test, zowel input data als (verwachte) output data.
- Gebruik een `setup` methode (met de juiste annotatie erboven) voor data die je in meerdere tests moet initialiseren.
### Assert
- Gebruik de assertions uit de Assertions library van JUnit.
- Denk aan de `equals` en `hashcode` wanneer je complete objecten met elkaar vergelijkt.
- Gebruik `verify` om te testen hoe vaak een bepaalde methode of stub wordt aangeroepen tijdens je test.

## Stap 2 (Profiel)
Maak een test-profiel aan in je test.resources map.
In dit profiel zorg je er onder andere voor dat je gebruik maakt van een test-database.
Zorg dat je de juiste dependency in je `pom.xml` hebt staan.

## Stap 3 (Integratietesten)
Ook de integratietesten kun je op verschillende manieren schrijven, maar er is een algemene structuur die je kunt handhaven:
- Gebruik de juiste annotaties boven je test-klasse (3 annotaties)
- Gebruik de juiste annotatie boven je test-methodes.
### arrange
- Gebruik geen mocks, maar `@Autowired` om de echte Beans te gebruiken (denk ook aan de `MockMvc`).
- Zet alle benodigde test-data klaar in de `Arrange-fase` van je test-methode, een `setup-methode` of een `data.sql`.
### act
De act-fase en de assert-fase van een integratietest lijken een beetje met elkaar verwoven, maar er is duidelijke grens.
- Gebruik `mockMvc.perform` om je test uit te voeren op een bepaald endpoint.
- Denk eraan dat je alle input meegeeft zoals je dat in postman zou doen, dus ook eventuele headers en JSON-data.
### assert
De assert-fase kun je op twee manieren uitvoeren:
- Gebruik `.andExpect` om te testen of je de verwachte response data terug krijgt. Maak hierbij gebruik van `MockMvcResultMatchers.jsonPath` en SPEL om de JSON inhoud van de response-body te testen.
- Gebruik `andReturn` om de HTTP-response waardes te testen via de Assertions-library. Test de response-body met de Assertions-library door deze met een `ObjectMapper` om te zetten in een DTO.
