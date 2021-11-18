package com.snakesandladders.game.services


import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class DiceServiceImplTest {

    val diceServiceImpl = DiceServiceImpl()

    companion object {
        const val MIN_DICE_VALUE = 1
        const val MAX_DICE_VALUE = 6
    }

    @Test
    fun shouldGenerateRandomIntFromOneToSixOnDiceRoll() {
        val result = diceServiceImpl.roll()
        assertTrue(result in MIN_DICE_VALUE..MAX_DICE_VALUE)
    }

}