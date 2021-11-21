package com.snakesandladders.game.services

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.Player
import java.util.UUID

interface GameService {

    fun initializeNewGame(): Game
    fun getGameById(gameId: UUID): Game
    fun addPlayerToGame(player: Player, game: Game): Game
    fun updateGame(game: Game): Game
    fun validateWinner(game: Game)
    fun evalStep(game: Game, fromString: UUID?, steps: Int)
    fun updatePlayerDiceRoll(game: Game, player: Player, rollResult: Int)

}
