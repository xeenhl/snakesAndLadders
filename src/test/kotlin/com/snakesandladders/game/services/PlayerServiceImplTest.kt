package com.snakesandladders.game.services

import com.snakesandladders.game.TestConstants.NAME
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.persistence.PlayerPersistenceService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class PlayerServiceImplTest {

    @Mock
    lateinit var playerPersistenceService: PlayerPersistenceService

    @InjectMocks
    lateinit var playerServiceImpl: PlayerServiceImpl


    @Test
    fun shouldCreateUser() {

        val newPlayer = Player(UUID.randomUUID(), NAME, mutableSetOf())

        `when`(playerPersistenceService.savePlayer(any())).thenReturn(newPlayer)

        val player = playerServiceImpl.createUser(NAME)

        assertEquals(player, newPlayer)

    }

    @Test
    fun shouldGetPlayerById() {
        val playerId = UUID.randomUUID()
        val savedPlayer = Player(playerId, NAME, mutableSetOf())

        `when`(playerPersistenceService.findPlayerById(playerId)).thenReturn(savedPlayer)
        val player = playerServiceImpl.getPlayerById(playerId)

        assertEquals(player, savedPlayer)
    }
}