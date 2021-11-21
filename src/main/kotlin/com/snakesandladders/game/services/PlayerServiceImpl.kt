package com.snakesandladders.game.services

import com.snakesandladders.game.exception.PlayerNotFoundException
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.persistence.PlayerPersistenceService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PlayerServiceImpl(private val playerPersistenceService: PlayerPersistenceService): PlayerService {

    override fun createUser(name: String): Player {
        return playerPersistenceService.savePlayer(
            Player(
                UUID.randomUUID(),
                name,
                mutableSetOf()
            )
        )
    }

    override fun getPlayerById(playerId: UUID): Player {
        return playerPersistenceService.findPlayerById(playerId) ?: throw PlayerNotFoundException("User with id [$playerId] not found")
    }

}