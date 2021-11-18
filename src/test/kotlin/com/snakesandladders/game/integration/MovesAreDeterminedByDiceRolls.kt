package com.snakesandladders.game.integration

import com.snakesandladders.game.TestConstants.NAME
import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.persistence.GamePersistenceServiceInMemoryImpl
import com.snakesandladders.game.persistence.PlayerPersistenceServiceInMemoryImpl
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovesAreDeterminedByDiceRolls {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    @MockBean
    lateinit var gamePersistenceServiceInMemoryImpl: GamePersistenceServiceInMemoryImpl

//    Given the game is started
//    When the player rolls a die
//    Then the result should be between 1-6 inclusive

    @Test
    fun `Given the game is started When the player rolls a dice Then the result should be between 1-6 inclusive`() {
        val player = Player(
            UUID.randomUUID(),
            "Player",
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(PlayerInGame(
                player, 0, 0
            )),
            GameStatus.RUNNING
        )

        Mockito.`when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        Mockito.`when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.get("/game/${game.id}/player/${player.id}/dice/roll")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
            .andExpect { jsonPath("$.result", CoreMatchers.anyOf(
                CoreMatchers.equalTo(1),
                CoreMatchers.equalTo(2),
                CoreMatchers.equalTo(3),
                CoreMatchers.equalTo(4),
                CoreMatchers.equalTo(5),
                CoreMatchers.equalTo(6)
            )
            ) }
    }

//    Given the player rolls a 4
//    When they move their token
//    Then the token should move 4 spaces

    @Test
    fun `Given the player rolls a 4 When they move their token Then the token should move 4 spaces`() {

        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(
                PlayerInGame(
                player, 4, 0
            )
            ),
            GameStatus.RUNNING
        )

        val updateGame = Game(
            game.id,
            mutableSetOf(
                PlayerInGame(
                player, 4 ,4
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
                                "position": 4
                            }],
                        "status": "${game.status}",
                        "winner": null
                    }
                """)}
            }
    }
}