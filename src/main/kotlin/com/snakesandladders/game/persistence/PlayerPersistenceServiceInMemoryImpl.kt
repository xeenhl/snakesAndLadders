package com.snakesandladders.game.persistence

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.Player
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerPersistenceServiceInMemoryImpl( private val players: MutableMap<UUID, Player>) : PlayerPersistenceService {

   override fun savePlayer(newPlayer: Player): Player {
        if(!players.containsKey(newPlayer.id)) players.put(newPlayer.id, newPlayer) else throw IllegalArgumentException(" Can't save (new) player as player with id [${newPlayer.id}] exists")
        return players.get(newPlayer.id) ?: throw IllegalStateException("There was a critical error during saving new player")
    }

    override fun findPlayerById(playerId: UUID): Player? {
        return if(players.containsKey(playerId)) players.get(playerId) else throw IllegalArgumentException(" Can't get player with id [${playerId}]")
    }
}