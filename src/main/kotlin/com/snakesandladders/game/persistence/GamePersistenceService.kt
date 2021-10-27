package com.snakesandladders.game.persistence

import com.snakesandladders.game.models.Game
import java.util.*

interface GamePersistenceService {
    fun saveGame(game: Game): Game
    fun findGameById(gameId: UUID): Game
    fun updateGame(game: Game): Game

}
