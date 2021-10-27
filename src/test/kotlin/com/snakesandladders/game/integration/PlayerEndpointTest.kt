package com.snakesandladders.game.integration

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerEndpointTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should create new player`() {

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
                        "id": "new-player-id",
                        "name": "Player01",
                        "games": [],
                        "wins": 0
                    }
                """)}
        }
    }

    @Test
    fun `should return existing player by id`() {

        mockMvc.get("/player/existing-player-id")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "id": "new-player-id",
                        "name": "Player01",
                        "games": [],
                        "wins": 0
                    }
                    """)}
            }
    }

    @Test
    fun `should return 404 for not existing player by id`() {

        mockMvc.get("/player/404-player-id")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json("""
                    {
                        "error": "Player with id [404-player-id] not found"
                    } 
                    """)}
            }
    }

}