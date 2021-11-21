package com.snakesandladders.game.models

import com.snakesandladders.game.models.DefaultPlayer.DEFAULT_PLAYER

fun Player.toPlayerDTO(): PlayerDTO = PlayerDTO(
    id,
    name,
    games.map { it.id }.toSet(),
    games.filter { it.status == GameStatus.FINISHED }.filter { it.winner.id == id }.count()
)

fun Game.toGameDTO(): GameDTO = GameDTO(
    id,
    players,
    status,
    if(winner != DEFAULT_PLAYER) winner else null
)