package com.snakesandladders.game.integration

import com.snakesandladders.game.models.Player
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
import org.springframework.test.web.servlet.post
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerEndpointTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    @Test
    fun `should create new player`() {

        val player = Player(
            UUID.randomUUID(),
            "Player01",
            mutableSetOf()
        )

        Mockito.`when`(playerPersistenceServiceInMemoryImpl.savePlayer(any())).thenReturn(player)

        mockMvc.post("/player/new") {
            contentType = MediaType.APPLICATION_JSON
            content  = """
                        { "name": "Player01" }
                    """
        }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "${player.id}",
                        "name": "Player01",
                        "games": [],
                        "wins": 0
                    }
                """)}
        }
    }

    @Test
    fun `should return existing player by id`() {

        val player = Player(
            UUID.randomUUID(),
            "Player01",
            mutableSetOf()
        )

        Mockito.`when`(playerPersistenceServiceInMemoryImpl.findPlayerById(player.id)).thenReturn(player)

        mockMvc.get("/player/${player.id}")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "${player.id}",
                        "name": "Player01",
                        "games": [],
                        "wins": 0
                    }
                    """)}
            }
    }

    @Test
    fun `should return 404 for not existing player by id`() {

        val id = UUID.randomUUID()

        mockMvc.get("/player/$id")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "User with id [${id}] not found"
                    } 
                    """)}
            }
    }

}