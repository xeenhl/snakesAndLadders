package com.snakesandladders.game.models

import java.util.*

enum class GameStatus {
    RUNNING, FINISHED
}

data class Game(val id: UUID, val players: MutableSet<PlayerInGame>, var status: GameStatus, var winner: Player = getDefaultPlayer())

data class PlayerInGame(val player: Player, var lastDice: Int, var position: Int)

data class PlayerDiceRoll(val playerId: UUID, val result: Int)

fun getDefaultPlayer() : Player = Player(UUID.randomUUID(), "", emptySet())