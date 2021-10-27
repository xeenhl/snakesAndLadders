package com.snakesandladders.game.models

import java.util.*

enum class GameStatus {
    RUNNING, FINISHED
}

data class Game(val id: UUID, val players: MutableSet<PlayerInGame>, val status: GameStatus, var winner: Player?)

data class PlayerInGame(val player: Player, var lastDice: Int, var position: Int)

data class PlayerDiceRoll(val playerId: UUID, val result: Int)