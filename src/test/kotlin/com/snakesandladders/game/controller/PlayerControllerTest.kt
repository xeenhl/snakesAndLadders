package com.snakesandladders.game.controller

import com.snakesandladders.game.TestConstants.NAME
import com.snakesandladders.game.models.NewPlayerDTO
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.toPlayerDTO
import com.snakesandladders.game.services.PlayerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class PlayerControllerTest {

    @Mock
    lateinit var playerService: PlayerService

    @InjectMocks
    lateinit var playerController: PlayerController

    @Test
    fun shouldCreateNewPlayer() {
        val player = Player(UUID.randomUUID(), NAME, mutableSetOf())

        Mockito.`when`(playerService.createUser(NAME)).thenReturn(player)

        val newPlayer = playerController.createNewPlayer(NewPlayerDTO(NAME))

        assertEquals(newPlayer.body?.name, NAME)
    }

    @Test
    fun shouldGetPlayerById() {
        val playerId = UUID.randomUUID()
        val player = Player(playerId, NAME, mutableSetOf())

        Mockito.`when`(playerService.getPlayerById(playerId)).thenReturn(player)

        val newPlayer = playerController.getPlayerById(playerId.toString())

        assertEquals(newPlayer.body, player.toPlayerDTO())
    }
}