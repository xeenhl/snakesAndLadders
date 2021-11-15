package com.snakesandladders.game.integration

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.persistence.GamePersistenceServiceInMemoryImpl
import com.snakesandladders.game.persistence.PlayerPersistenceServiceInMemoryImpl
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenCanMoveAcrossTheBoardCases {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    @MockBean
    lateinit var gamePersistenceServiceInMemoryImpl: GamePersistenceServiceInMemoryImpl

//    Given the game is started
//    When the token is placed on the board
//    Then the token is on square 1

    @Test
    fun `Given the game is started When the token is placed on the board Then the token is on square 1`() {
        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )

        val player = Player(
            UUID.randomUUID(),
            "Player",
            mutableSetOf()
        )

        val updateGame = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                player, 0 ,1
            )
            ),
            GameStatus.RUNNING,
            null
        )

        Mockito.`when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        Mockito.`when`(gamePersistenceServiceInMemoryImpl.updateGame(updateGame)).thenReturn(updateGame)
        Mockito.`when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.put("/game/${game.id}/add/player/${player.id}")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "${game.id}",
                        "players": [
                            {
                            "player" : {
                                "id": "${player.id}",
                                "name": "${player.name}",
                                "games": []
                                },
                                "lastDice": 0,
                                "position": 1
                            }],
                        "status": "${game.status}",
                        "winner": ${game.winner}
                    }
                """)}
            }
    }

//    Given the token is on square 1
//    When the token is moved 3 spaces
//    Then the token is on square 4

    @Test
    fun `Given the token is on square 1  When the token is moved 3 spaces Then the token is on square 4`() {

        val player = Player(
            UUID.randomUUID(),
            "Player",
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(
                PlayerInGame(
                    player, 3 ,1
                )
            ),
            GameStatus.RUNNING,
            null
        )

        val updateGame = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                    player, 3 ,4
                )
            ),
            GameStatus.RUNNING,
            null
        )

        Mockito.`when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        Mockito.`when`(gamePersistenceServiceInMemoryImpl.updateGame(updateGame)).thenReturn(updateGame)
        Mockito.`when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.put("/game/${game.id}/player/${player.id}/move/3")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "${game.id}",
                        "players": [
                            {
                            "player" : {
                                "id": "${player.id}",
                                "name": "${player.name}",
                                "games": []
                                },
                                "lastDice": 3,
                                "position": 4
                            }],
                        "status": "${game.status}",
                        "winner": ${game.winner}
                    }
                """)}
            }
    }

//    Given the token is on square 1
//    When the token is moved 3 spaces
//    And then it is moved 4 spaces
//    Then the token is on square 8
//
    @Test
    fun `Given the token is on square 1 When the token is moved 3 spaces And then it is moved 4 spaces Then the token is on square 8`() {

        val player = Player(
            UUID.randomUUID(),
            "Player",
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(
                PlayerInGame(
                    player, 3 ,1
                )
            ),
            GameStatus.RUNNING,
            null
        )

        val updateGameFirst = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                    player, 3 ,4
                )
            ),
            GameStatus.RUNNING,
            null
        )

        val updateGameFirstAfterDice = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                    player, 4 ,4
                )
            ),
            GameStatus.RUNNING,
            null
        )

        val updateGameSecond = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                    player, 4 ,8
                )
            ),
            GameStatus.RUNNING,
            null
        )

        Mockito.`when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game, updateGameFirstAfterDice)
        Mockito.`when`(gamePersistenceServiceInMemoryImpl.updateGame(updateGameFirst)).thenReturn(updateGameFirst)
        Mockito.`when`(gamePersistenceServiceInMemoryImpl.updateGame(updateGameSecond)).thenReturn(updateGameSecond)
        Mockito.`when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.put("/game/${game.id}/player/${player.id}/move/3")
        mockMvc.put("/game/${game.id}/player/${player.id}/move/4")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                        {
                            "id": "${game.id}",
                            "players": [
                                {
                                "player" : {
                                    "id": "${player.id}",
                                    "name": "${player.name}",
                                    "games": []
                                    },
                                    "lastDice": 4,
                                    "position": 8
                                }],
                            "status": "${game.status}",
                            "winner": ${game.winner}
                        }
                    """)}
            }
    }

}