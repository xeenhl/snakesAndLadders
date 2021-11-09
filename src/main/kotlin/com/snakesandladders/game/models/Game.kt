package com.snakesandladders.game.models

import com.fasterxml.jackson.annotation.JsonView
import java.util.*

enum class GameStatus {
    RUNNING, FINISHED
}

data class Game(val id: UUID, val players: MutableSet<PlayerInGame>, var status: GameStatus, var winner: Player?)

data class PlayerInGame(val player: Player, var lastDice: Int, var position: Int)

data class PlayerDiceRoll(val playerId: UUID, val result: Int)