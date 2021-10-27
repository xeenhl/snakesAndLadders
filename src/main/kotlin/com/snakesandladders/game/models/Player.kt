package com.snakesandladders.game.models

import java.util.*

data class Player(val id: UUID,  val name: String, val games: Set<Game>)

data class NewPlayerDTO( val name: String)

data class PlayerDTO(val id: UUID,  val name: String, val games: Set<UUID>, val wins: Int)
