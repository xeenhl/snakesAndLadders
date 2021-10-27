package com.snakesandladders.game.persistence

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class PlayerPersistenceServiceInMemoryImplTest {

    @Mock
    lateinit var players: MutableMap<UUID, Player>

    @InjectMocks
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    val NAME = "Name"

    @Test
    fun shouldSavePlayer() {

        val playerId = UUID.randomUUID()

        val newPlayer = Player(
            playerId,
            NAME,
            mutableSetOf()
        )

        val player = playerPersistenceServiceInMemoryImpl.savePlayer(newPlayer)

        verify(players.put(playerId, player), times(1))
        assertEquals(player, newPlayer)

    }

    @Test
    fun shouldFindPlayerById() {

        val playerId = UUID.randomUUID()

        val savedPlayer = Player(
            playerId,
            NAME,
            mutableSetOf()
        )

        `when`(players.get(playerId)).thenReturn(savedPlayer)

        val game = playerPersistenceServiceInMemoryImpl.findPlayerById(playerId)

        verify(players.get(playerId), times(1))
        assertEquals(game, savedPlayer)

    }
}