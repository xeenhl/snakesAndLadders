package com.snakesandladders.game.models

import java.util.UUID

object DefaultPlayer {
    const val DEFAULT_PLAYER_ID = "b0d6294b-140c-4101-af1b-a310211261f9"
    const val name = ""
    val DEFAULT_PLAYER = Player(UUID.fromString(DEFAULT_PLAYER_ID), name, emptySet())
}