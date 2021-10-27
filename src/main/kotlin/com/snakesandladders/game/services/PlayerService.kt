package com.snakesandladders.game.services

import com.snakesandladders.game.models.Player
import java.util.*

interface PlayerService {
    abstract fun createUser(name: String): Player
    abstract fun getPlayerById(playerId: UUID): Player

}
