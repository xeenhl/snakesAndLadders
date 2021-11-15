package com.snakesandladders.game.controller

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.PlayerDiceRoll
import com.snakesandladders.game.services.DiceService
import com.snakesandladders.game.services.GameService
import com.snakesandladders.game.services.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("game")
class GameController(private val gameService: GameService,
                     private val userService: PlayerService,
                     private val diceService: DiceService) {


    @PostMapping(path = ["/create/new"], produces = ["application/json"] )
    fun createNewGame(): ResponseEntity<Game> {
        return  ResponseEntity.ok(gameService.initializeNewGame())
    }

    @GetMapping("/{gameId}")
    fun getGameById(@PathVariable(required = true) gameId: String): ResponseEntity<Game> {
        return  ResponseEntity.ok(gameService.getGameById(UUID.fromString(gameId)))
    }

    @PutMapping("/{gameId}/add/player/{playerId}")
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

        gameService.updatePlayerDiceRoll(game, player, rollResult)
        gameService.updateGame(game)

        return  ResponseEntity.ok(PlayerDiceRoll(player.id, rollResult))
    }

    @PutMapping("/{gameId}/player/{playerId}/move/{steps}")
    fun movePlayerInGame(@PathVariable(required = true) playerId: String,
                         @PathVariable(required = true) gameId: String,
                         @PathVariable(required = true) steps: Int): ResponseEntity<Game> {
        val game = gameService.getGameById(UUID.fromString(gameId))

        gameService.evalStep(game, UUID.fromString(playerId), steps)
        gameService.validateWinner(game)

        return  ResponseEntity.ok(gameService.updateGame(game))
    }

}