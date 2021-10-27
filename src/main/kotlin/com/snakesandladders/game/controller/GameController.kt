package com.snakesandladders.game.controller

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.PlayerDiceRoll
import com.snakesandladders.game.services.DiceService
import com.snakesandladders.game.services.GameService
import com.snakesandladders.game.services.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("game")
class GameController(private val gameService: GameService,
                     private val userService: PlayerService,
                     private val diceService: DiceService) {


    @GetMapping("/create/new")
    fun createNewGame(): ResponseEntity<Game> {
        val game = gameService.initializeNewGame()
        return  ResponseEntity.ok(game)
    }

    @GetMapping("/{gameId}")
    fun getGameById(@PathVariable(required = true) gameId: String): ResponseEntity<Game> {
        val game = gameService.getGameById(UUID.fromString(gameId))
        return  ResponseEntity.ok(game)
    }

    @GetMapping("/{gameId}/add/palyer/{playerId}")
    fun addPlayerToGame(@PathVariable(required = true) playerId: String,
                        @PathVariable(required = true) gameId: String): ResponseEntity<Game> {
        val game = gameService.getGameById(UUID.fromString(gameId))
        val player = userService.getPlayerById(UUID.fromString(playerId))
        return  ResponseEntity.ok(gameService.addPlayerToGame(player, game))
    }

    @GetMapping("/{gameId}/player/{playerId}/dice/roll")
    fun diceRollForPlayer(@PathVariable(required = true) playerId: String,
                          @PathVariable(required = true) gameId: String): ResponseEntity<PlayerDiceRoll> {
        val rollResult = diceService.roll()
        val game = gameService.getGameById(UUID.fromString(gameId))
        val player = userService.getPlayerById(UUID.fromString(playerId))
        game.players.first { it.player == player }.lastDice = rollResult
        gameService.updateGame(game)

        return  ResponseEntity.ok(PlayerDiceRoll(player.id, rollResult))
    }

    @GetMapping("/{gameId}/player/{playerId}/move/{steps}")
    fun movePlayerInGame(@PathVariable(required = true) playerId: String,
                         @PathVariable(required = true) gameId: String,
                         @PathVariable(required = true) steps: Int): ResponseEntity<Game> {
        val game = gameService.getGameById(UUID.fromString(gameId))
        val player = game.players.first { it.player.id == UUID.fromString(playerId) }
        if(steps == player.lastDice) player.position += steps else throw IllegalArgumentException(" Steps must be same as las dice result: ${player.lastDice}")
        return  ResponseEntity.ok(game)
    }

}