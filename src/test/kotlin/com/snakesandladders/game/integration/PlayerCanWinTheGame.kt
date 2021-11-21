package com.snakesandladders.game.integration

import com.snakesandladders.game.TestConstants.NAME
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
import org.springframework.test.web.servlet.put
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerCanWinTheGame {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    @MockBean
    lateinit var gamePersistenceServiceInMemoryImpl: GamePersistenceServiceInMemoryImpl

    companion object {
        const val DICE_ROLL_RESULT = 3
        const val OVERDRAFT_ROLL_RESULT = 4
        const val START_POSITION = 97
        const val FINAL_POSITION = 100
    }

//    Given the token is on square 97
//    When the token is moved 3 spaces
//    Then the token is on square 100
//    And the player has won the game

    @Test
    fun `Given the token is on square 97 When the token is moved 3 spaces Then the token is on square 100 And the player has won the game`() {
        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(
                PlayerInGame(
                    player, DICE_ROLL_RESULT, START_POSITION
                )
            ),
            GameStatus.RUNNING
        )

        val updateGame = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                    player, DICE_ROLL_RESULT, FINAL_POSITION
                )
            ),
            GameStatus.FINISHED,
            player
        )

        Mockito.`when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        Mockito.`when`(gamePersistenceServiceInMemoryImpl.updateGame(any())).thenReturn(updateGame)
        Mockito.`when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.put("/game/${game.id}/player/${player.id}/move/3")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        """
                    {
                        "id": "${updateGame.id}",
                        "players": [
                            {
                            "player" : {
                                "id": "${player.id}",
                                "name": "${player.name}",
                                "games": []
                                },
                                "lastDice": $DICE_ROLL_RESULT,
                                "position": $FINAL_POSITION
                            }],
                        "status": "${updateGame.status}",
                        "winner": {
                            id: "${updateGame.winner.id}",
                            name: "${updateGame.winner.name}",
                            games: ${updateGame.winner.games}
                        }
                    }
                """
                    )
                }
            }
    }

//    Given the token is on square 97
//    When the token is moved 4 spaces
//    Then the token is on square 97
//    And the player has not won the game

    @Test
    fun `Given the token is on square 97 When the token is moved 4 spaces Then the token is on square 97 And the player has not won the game`() {
        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(
                PlayerInGame(
                    player, OVERDRAFT_ROLL_RESULT, START_POSITION
                )
            ),
            GameStatus.RUNNING
        )

        val updateGame = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                    player, OVERDRAFT_ROLL_RESULT, START_POSITION
                )
            ),
            GameStatus.RUNNING
        )

        Mockito.`when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        Mockito.`when`(gamePersistenceServiceInMemoryImpl.updateGame(any())).thenReturn(updateGame)
        Mockito.`when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.put("/game/${game.id}/player/${player.id}/move/4")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(
                        """
                    {
                        "id": "${updateGame.id}",
                        "players": [
                            {
                            "player" : {
                                "id": "${player.id}",
                                "name": "${player.name}",
                                "games": []
                                },
                                "lastDice": $OVERDRAFT_ROLL_RESULT,
                                "position": $START_POSITION
                            }],
                        "status": "${updateGame.status}",
                        "winner": null
                        }
                    }
                """
                    )
                }
            }
    }

}