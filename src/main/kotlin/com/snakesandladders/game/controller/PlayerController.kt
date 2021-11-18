package com.snakesandladders.game.controller

import com.snakesandladders.game.models.NewPlayerDTO
import com.snakesandladders.game.models.PlayerDTO
import com.snakesandladders.game.models.toPlayerDTO
import com.snakesandladders.game.services.PlayerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("player")
class PlayerController(private val playerService: PlayerService) {


    @PostMapping("/new")
    fun createNewPlayer(@RequestBody player: NewPlayerDTO): ResponseEntity<PlayerDTO> {

        val newPlayer = playerService.createUser(player.name)
        return ResponseEntity.ok(newPlayer.toPlayerDTO())

    }

    @GetMapping("/{playerId}")
    fun getPlayerById(@PathVariable(required = true) playerId: String): ResponseEntity<PlayerDTO> {

        val player =  playerService.getPlayerById(UUID.fromString(playerId))
        return ResponseEntity.ok(player.toPlayerDTO())

    }


}