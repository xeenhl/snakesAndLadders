package com.snakesandladders.game.integration

import com.snakesandladders.game.TestConstants.NAME
import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.persistence.GamePersistenceServiceInMemoryImpl
import com.snakesandladders.game.persistence.GamePersistenceServiceInMemoryImplTest
import com.snakesandladders.game.persistence.PlayerPersistenceServiceInMemoryImpl
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.kotlin.any
import org.mockito.kotlin.isA
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.util.UUID


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameEndpointTest() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    @MockBean
    lateinit var gamePersistenceServiceInMemoryImpl: GamePersistenceServiceInMemoryImpl


    @Test
    fun `should create new game`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING
        )

        `when`(gamePersistenceServiceInMemoryImpl.saveGame(any())).thenReturn(game)

        mockMvc.post("/game/create/new")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(""" 
                    {
                        "id": "${game.id}",
                        "players": [],
                        "status": "${game.status}",
                        "winner": null
                    }
                """)}}
    }

    @Test
    fun `should return existing game by id`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING
        )

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)

        mockMvc.get("/game/${game.id}")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "${game.id}",
                        "players": [],
                        "status": "${game.status}",
                        "winner": null 
                    }
                """)}
            }
    }

    @Test
    fun `should return 404 for not existing game by id`() {

        val id = UUID.randomUUID()

        mockMvc.get("/game/${id}")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "Game with id [${id}] not found"
                    }
                """)}
            }
    }

    @Test
    fun `should add existing player to existing game by ids`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING
        )

        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        val updateGame = Game(
            game.id,
            mutableSetOf(PlayerInGame(
                player, 0 ,0
            )),
            GameStatus.RUNNING
        )

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        `when`(gamePersistenceServiceInMemoryImpl.updateGame(any())).thenReturn(updateGame)
        `when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

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
                                "position": 0
                            }],
                        "status": "${game.status}",
                        "winner": null
                    }
                """)}
            }
    }

    @Test
    fun `should return error if adding not existing user to existing game by id`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING
        )

        val userid = UUID.randomUUID()

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)

        mockMvc.put("/game/${game.id}/add/player/$userid")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "User with id [$userid] not found"
                    }
                """)}
            }
    }

    @Test
    fun `should return error if adding existing user to not existing game by id`() {

        val gameid = UUID.randomUUID()

        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        `when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.put("/game/$gameid/add/player/${player.id}")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "Game with id [${gameid}] not found"
                    }
                """)}
            }
    }

    @Test
    fun `should move player by defined steps `() {

        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(PlayerInGame(
                player, 3, 0
            )),
            GameStatus.RUNNING
        )

        val updateGame = Game(
            game.id,
            mutableSetOf(PlayerInGame(
                player, 3 ,3
            )),
            GameStatus.RUNNING
        )

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        `when`(gamePersistenceServiceInMemoryImpl.updateGame(any())).thenReturn(updateGame)
        `when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

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
                                "position": 3
                            }],
                        "status": "${game.status}",
                        "winner": null
                    }
                """)}
            }
    }

    @Test
    fun `should return error if moving not existing player to existing game by id`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING
        )

        val playerId = UUID.randomUUID()

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)

        mockMvc.put("/game/${game.id}/player/$playerId/move/3")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "Can't get player with id [$playerId]"
                    }
                """)}
            }
    }

    @Test
    fun `should return error if moving existing player to not existing game by id`() {

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING
        )

        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        `when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.put("/game/${game.id}/player/${player.id}/move/3")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "Can't get player with id [${player.id}]"
                    }
                """)}
            }
    }

    @Test
    fun `should roll the dice for existing player in existing game`() {

        val player = Player(
            UUID.randomUUID(),
            NAME,
            mutableSetOf()
        )

        val game = Game(
            UUID.randomUUID(),
            mutableSetOf(PlayerInGame(
                player, 0, 0
            )),
            GameStatus.RUNNING
        )

        `when`(gamePersistenceServiceInMemoryImpl.findGameById(game.id)).thenReturn(game)
        `when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.get("/game/${game.id}/player/${player.id}/dice/roll")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
            .andExpect { jsonPath("$.result", anyOf(
                equalTo(1),
                equalTo(2),
                equalTo(3),
                equalTo(4),
                equalTo(5),
                equalTo(6)
            )) }
    }
}