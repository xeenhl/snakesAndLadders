package com.snakesandladders.game.persistence

import com.snakesandladders.game.models.Player
import java.util.UUID

interface PlayerPersistenceService {
    fun savePlayer(newPlayer: Player): Player
    fun findPlayerById(playerId: UUID): Player?

}
