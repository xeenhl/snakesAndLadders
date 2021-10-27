package com.snakesandladders.game.integration

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.persistence.GamePersistenceServiceInMemoryImpl
import com.snakesandladders.game.persistence.GamePersistenceServiceInMemoryImplTest
import com.snakesandladders.game.persistence.PlayerPersistenceServiceInMemoryImpl
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameEndpointTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    @MockBean
    lateinit var gamePersistenceServiceInMemoryImpl: GamePersistenceServiceInMemoryImpl

    private val NAME = "Name"


    @Test
    fun `should create new game`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )

        `when`(gamePersistenceServiceInMemoryImpl.saveGame(any())).thenReturn(game)

        mockMvc.get("/game/create/new")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(""" 
                    {
                        "id": "${game.id}",
                        "players": [],
                        "status": "${game.status}",
                        "winner": ${game.winner} 
                    }
                """)}}
    }

    @Test
    fun `should return existing game by id`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(any())).thenReturn(game)

        mockMvc.get("/game/${game.id}")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "${game.id}",
                        "players": [],
                        "status": "${game.status}",
                        "winner": ${game.winner} 
                    }
                """)}
            }
    }

    @Test
    fun `should return 404 for not existing game by id`() {

        mockMvc.get("/game/404-game-id")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "Game [404-game-id] not exists",
                    }
                """)}
            }
    }

    @Test
    fun `should add existing player to existing game by ids`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )

        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        val updateGame = Game(
            UUID.randomUUID(),
            mutableSetOf(PlayerInGame(
                player, 0 ,0
            )),
            GameStatus.RUNNING,
            null
        )

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        `when`(gamePersistenceServiceInMemoryImpl.updateGame(any())).thenReturn(updateGame)
        `when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.get("/game/${game.id}/add/player/${player.id}")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "${game.id}",
                        "players": [
                            {
                            "id": "${player.id}",
                            "position": 0,
                            }],
                        "status": "${game.status}",
                        "winner": ${game.winner}
                    }
                """)}
            }
    }

    @Test
    fun `should return error if adding not existing user to existing game by id`() {

        mockMvc.get("/game/existing-game-id/add/player/404-user-id")
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "can't add not existing player [404-user-id] to game [existing-game-id]",
                    }
                """)}
            }
    }

    @Test
    fun `should return error if adding existing user to not existing game by id`() {

        mockMvc.get("/game/404-game-id/add/player/existing-player-id")
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "can't add player [404-game-id] to not existing game [existing-player-id]",
                    }
                """)}
            }
    }

    @Test
    fun `should move player by defined steps `() {

        mockMvc.get("/game/existing-game-id/player/existing-player-id/move/3")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "existing-game-id",
                        "players": [
                            {
                            "id": "existing-player-id",
                            "position": 3
                            }],
                        "status": "running",
                        "winner": ""
                    }
                """)}
            }
    }

    @Test
    fun `should return error if moving not existing player to existing game by id`() {

        mockMvc.get("/game/existing-game-id/player/404-player-id/move/3")
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "can't move not existing player [404-user-id] in game [existing-game-id]",
                    }
                """)}
            }
    }

    @Test
    fun `should return error if moving existing player to not existing game by id`() {

        mockMvc.get("/game/404-game-id/player/existing-player-id/move/3")
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "can't move player [existing-player-id] in game [404-game-id] as no such game exists",
                    }
                """)}
            }
    }

    @Test
    fun `should roll the dice for existing player in existing game`() {

        mockMvc.get("/game/existing-game-id/player/existing-player-id/dice/roll")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "player": "existing-player-id",
                        "dice": 4
                    }
                """)}
            }
    }
}