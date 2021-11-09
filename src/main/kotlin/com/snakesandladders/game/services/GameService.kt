package com.snakesandladders.game.services

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.Player
import java.util.*

interface GameService {
    fun initializeNewGame(): Game
    fun getGameById(gameId: UUID): Game
    fun addPlayerToGame(player: Player, game: Game): Game
    fun updateGame(game: Game): Game
    fun validateWinner(game: Game)

}
