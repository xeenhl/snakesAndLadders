package com.snakesandladders.game.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

internal class DiceServiceImplTest {

    val diceServiceImpl = DiceServiceImpl()

    @Test
    fun shouldGenerateRandomIntFromOneToSixOnDiceRoll() {
        val result = diceServiceImpl.roll()
        assertTrue(result >= 1 && result <= 6)
    }

}