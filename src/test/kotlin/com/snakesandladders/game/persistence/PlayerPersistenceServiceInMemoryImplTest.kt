package com.snakesandladders.game.persistence

import com.snakesandladders.game.TestConstants.NAME
import com.snakesandladders.game.models.Player
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class PlayerPersistenceServiceInMemoryImplTest {

    @Mock
    lateinit var players: MutableMap<UUID, Player>

    @InjectMocks
    lateinit var playerPersistenceServiceInMemoryImpl: PlayerPersistenceServiceInMemoryImpl

    @Test
    fun shouldSavePlayer() {

        val playerId = UUID.randomUUID()

        val newPlayer = Player(
            playerId,
            NAME,
            mutableSetOf()
        )

        `when`(players.get(playerId)).thenReturn(newPlayer)
        val player = playerPersistenceServiceInMemoryImpl.savePlayer(newPlayer)

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

        `when`(players.containsKey(playerId)).thenReturn(true)
        `when`(players.get(playerId)).thenReturn(savedPlayer)

        val game = playerPersistenceServiceInMemoryImpl.findPlayerById(playerId)

        assertEquals(game, savedPlayer)

    }
}