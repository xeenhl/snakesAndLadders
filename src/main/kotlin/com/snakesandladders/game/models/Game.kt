package com.snakesandladders.game.models

import com.snakesandladders.game.models.DefaultPlayer.DEFAULT_PLAYER
import java.util.UUID

enum class GameStatus {
    RUNNING, FINISHED
}

data class Game(
    val id: UUID,
    val players: MutableSet<PlayerInGame>,
    var status: GameStatus,
    var winner: Player = DEFAULT_PLAYER
)

data class GameDTO(val id: UUID, val players: MutableSet<PlayerInGame>, var status: GameStatus, var winner: Player?)

data class PlayerInGame(val player: Player, var lastDice: Int, var position: Int)

data class PlayerDiceRoll(val playerId: UUID, val result: Int)