# Snakes And Ladders

Snakes and ladders is turn based game Rest API

### Rest Api

following endpoint are provided to api consumer:

1) GET: /game/create/new
    - creates and save a game in RUNNING status without any players
2) GET: /game/{gameId}
    - returns game by id, or 404 if no game found
3) GET: /game/{gameId}/add/player/{playerId}
    - add existing player by id to the game
4) GET: /game/{gameId}/player/{playerId}/dice/roll
    - roll a dice for player in game
5) GET: /game/{gameId}/player/{playerId}/move/{steps}
    - move player to provided steps that match last dice roll result
6) POST: /player
    - add new player to the system.
    - Request body model: { "name": "Player_name" }
7) GET: /player/{playerId}
    - returns player data

### Guides

Project uses gradle 7.2 and Java 11 kotlin
use following to run gradle commands:

1) ./gradlew  build - to build the project
2) ./gradlew  test -  to execute tests
3) ./gradlew bootRun - to run Application

