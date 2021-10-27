package com.snakesandladders.game.services

import com.snakesandladders.game.models.Player
import java.util.*

interface PlayerService {
    fun createUser(name: String): Player
    fun getPlayerById(playerId: UUID): Player

}
