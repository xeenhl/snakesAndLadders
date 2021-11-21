package com.snakesandladders.game.controller

import com.snakesandladders.game.models.GameDTO
import com.snakesandladders.game.models.PlayerDiceRoll
import com.snakesandladders.game.models.toGameDTO
import com.snakesandladders.game.services.DiceService
import com.snakesandladders.game.services.GameService
import com.snakesandladders.game.services.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("game")
class GameController(
    private val gameService: GameService,
    private val userService: PlayerService,
    private val diceService: DiceService
) {

    @PostMapping(path = ["/create/new"], produces = ["application/json"])
    fun createNewGame(): ResponseEntity<GameDTO> {
        return ResponseEntity.ok(gameService.initializeNewGame().toGameDTO())
    }

    @GetMapping("/{gameId}")
    fun getGameById(@PathVariable(required = true) gameId: String): ResponseEntity<GameDTO> {
        return ResponseEntity.ok(gameService.getGameById(UUID.fromString(gameId)).toGameDTO())
    }

    @PutMapping("/{gameId}/add/player/{playerId}")
    fun addPlayerToGame(
        @PathVariable(required = true) playerId: String,
        @PathVariable(required = true) gameId: String
    ): ResponseEntity<GameDTO> {
        val game = gameService.getGameById(UUID.fromString(gameId))
        val player = userService.getPlayerById(UUID.fromString(playerId))

        return ResponseEntity.ok(gameService.addPlayerToGame(player, game).toGameDTO())
    }

    @GetMapping("/{gameId}/player/{playerId}/dice/roll")
    fun diceRollForPlayer(
        @PathVariable(required = true) playerId: String,
        @PathVariable(required = true) gameId: String
    ): ResponseEntity<PlayerDiceRoll> {
        val rollResult = diceService.roll()
        val game = gameService.getGameById(UUID.fromString(gameId))
        val player = userService.getPlayerById(UUID.fromString(playerId))

        gameService.updatePlayerDiceRoll(game, player, rollResult)
        gameService.updateGame(game)

        return ResponseEntity.ok(PlayerDiceRoll(player.id, rollResult))
    }

    @PutMapping("/{gameId}/player/{playerId}/move/{steps}")
    fun movePlayerInGame(
        @PathVariable(required = true) playerId: String,
        @PathVariable(required = true) gameId: String,
        @PathVariable(required = true) steps: Int
    ): ResponseEntity<GameDTO> {
        val game = gameService.getGameById(UUID.fromString(gameId))

        gameService.evalStep(game, UUID.fromString(playerId), steps)
        gameService.validateWinner(game)

        return ResponseEntity.ok(gameService.updateGame(game).toGameDTO())
    }

}