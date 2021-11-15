package com.snakesandladders.game.services

import com.snakesandladders.game.exception.GameNotFoundException
import com.snakesandladders.game.exception.PlayerNotFoundException
import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.persistence.GamePersistenceService
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameServiceImpl(private val gamePersistenceService: GamePersistenceService) : GameService {

    override fun initializeNewGame(): Game {
        val newGame = Game(
            UUID.randomUUID(),
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )
        return gamePersistenceService.saveGame(newGame)
    }

    override fun getGameById(gameId: UUID): Game {
        return gamePersistenceService.findGameById(gameId) ?: throw GameNotFoundException("Game with id [$gameId] not found")
    }

    override fun addPlayerToGame(player: Player, game: Game): Game {
        game.players.add(
            PlayerInGame(
                player,
                0,
                1
            )
        )
        return gamePersistenceService.updateGame(game)
    }

    override fun updateGame(game: Game): Game {
        return gamePersistenceService.updateGame(game)
    }

    override fun validateWinner(game: Game) {
        val player = game.players.firstOrNull { it.position >= 100 }
        if(player != null) {
            game.winner = player.player
            game.status = GameStatus.FINISHED
        }
    }

    override fun evalStep(game: Game, playerId: UUID?, steps: Int) {
        val player = game.players.firstOrNull { it.player.id == playerId } ?: throw PlayerNotFoundException("Can't get player with id [${playerId}]")
        if(steps == player.lastDice) player.position += steps else throw IllegalArgumentException(" Steps must be same as las dice result: ${player.lastDice}")
        if(player.position > 100) player.position -= steps
    }

    override fun updatePlayerDiceRoll(game: Game, player: Player, rollResult: Int) {
        game.players.firstOrNull() { it.player == player }?.lastDice = rollResult
    }
}