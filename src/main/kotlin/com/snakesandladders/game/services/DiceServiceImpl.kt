package com.snakesandladders.game.services

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class DiceServiceImpl: DiceService {

    override fun roll(): Int {
        return Random.nextInt(1, 7)
    }

}