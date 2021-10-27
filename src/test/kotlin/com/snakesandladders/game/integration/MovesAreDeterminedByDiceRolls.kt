package com.snakesandladders.game.integration

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovesAreDeterminedByDiceRolls {

    @Autowired
    private lateinit var mockMvc: MockMvc

//    Given the game is started
//    When the player rolls a die
//    Then the result should be between 1-6 inclusive

    @Test
    fun `Given the game is started When the player rolls a dice Then the result should be between 1-6 inclusive`() {
        throw NotImplementedError()
    }

//    Given the player rolls a 4
//    When they move their token
//    Then the token should move 4 spaces

    @Test
    fun `Given the player rolls a 4 When they move their token Then the token should move 4 spaces`() {
        throw NotImplementedError()
    }
}