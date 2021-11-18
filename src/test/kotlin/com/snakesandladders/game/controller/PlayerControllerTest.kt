package com.snakesandladders.game.controller

import com.snakesandladders.game.models.*
import com.snakesandladders.game.services.PlayerService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class PlayerControllerTest {

    @Mock
    lateinit var playerService: PlayerService

    @InjectMocks
    lateinit var playerController: PlayerController

    @Test
    fun shouldCreateNewPlayer() {
        val player = Player(UUID.randomUUID(), Companion.NAME, mutableSetOf())

        Mockito.`when`(playerService.createUser(Companion.NAME)).thenReturn(player)

        val newPlayer = playerController.createNewPlayer(NewPlayerDTO(Companion.NAME));

        assertEquals(newPlayer.body?.name, Companion.NAME)
    }

    @Test
    fun shouldGetPlayerById() {
        val playerId = UUID.randomUUID()
        val player = Player(playerId, Companion.NAME, mutableSetOf())

        Mockito.`when`(playerService.getPlayerById(playerId)).thenReturn(player)

        val newPlayer = playerController.getPlayerById(playerId.toString());

        assertEquals(newPlayer.body, player.toPlayerDTO())
    }

    companion object {
        const val NAME = "name"
    }
}