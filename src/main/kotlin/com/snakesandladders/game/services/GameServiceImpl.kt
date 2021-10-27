package com.snakesandladders.game.services

import com.snakesandladders.game.exception.GameNotFoundException
import com.snakesandladders.game.models.*
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
        return gamePersistenceService.findGameById(gameId) ?: throw GameNotFoundException("User with id [$gameId] not found")
    }

    override fun addPlayerToGame(player: Player, game: Game): Game {
        game.players.add(
            PlayerInGame(
                player,
                0,
                0
            )
        )
        return gamePersistenceService.updateGame(game)
    }

    override fun updateGame(game: Game): Game {
        return gamePersistenceService.updateGame(game)
    }
}