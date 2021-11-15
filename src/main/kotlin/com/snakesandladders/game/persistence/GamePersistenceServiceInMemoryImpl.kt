package com.snakesandladders.game.persistence

import com.snakesandladders.game.models.Game
import org.springframework.stereotype.Service
import java.util.*

@Service
class GamePersistenceServiceInMemoryImpl(private val games: MutableMap<UUID, Game>) : GamePersistenceService {

    override fun saveGame(game: Game): Game {
        if(!games.containsKey(game.id)) games.put(game.id, game) else throw IllegalArgumentException(" Can't save (new) game as game with id [${game}] exists")
        return games.get(game.id) ?: throw IllegalStateException("There was a critical error during saving new game. Please create new game")
    }

    override fun findGameById(gameId: UUID): Game? {
        return if(games.containsKey(gameId)) games.get(gameId) else throw IllegalArgumentException(" Can't get game with id [${gameId}]")
    }

    override fun updateGame(game: Game): Game {
        if(games.containsKey(game.id)) games.put(game.id, game) else throw IllegalArgumentException(" Can't update the game as game with id [${game}] not exist")
        return games.get(game.id) ?: throw IllegalStateException("There was a critical error during update new game. Please create new game")
    }
}