package com.snakesandladders.game

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.Player
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import java.util.*
import kotlin.collections.HashMap

@SpringBootApplication
@EnableAspectJAutoProxy
class SnakesAndLaddersApplication

fun main(args: Array<String>) {
    runApplication<SnakesAndLaddersApplication>(*args)
}

@Bean
fun players(): MutableMap<UUID, Player> = HashMap()

@Bean
fun games(): MutableMap<UUID, Game> = HashMap()
