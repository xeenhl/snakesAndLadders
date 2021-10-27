package com.snakesandladders.game.models

fun Player.toPlayerDTO() : PlayerDTO = PlayerDTO(
        id,
        name,
        games.map { it.id }.toSet(),
        games.filter { it.status == GameStatus.FINISHED }.filter { it.winner?.id == id }.count()
)