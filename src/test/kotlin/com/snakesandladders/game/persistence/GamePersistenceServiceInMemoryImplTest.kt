package com.snakesandladders.game.persistence

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GamePersistenceServiceInMemoryImplTest {

    @Mock
    lateinit var games: MutableMap<UUID, Game>

    @InjectMocks
    lateinit var gamePersistenceServiceInMemoryImpl: GamePersistenceServiceInMemoryImpl

    @Test
    fun saveGame() {
        val gameId = UUID.randomUUID()
        val game = Game(
            gameId,
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )

        val savedGame = gamePersistenceServiceInMemoryImpl.saveGame(game)

        verify(games.put(gameId, game), times(1))
        assertEquals(game, savedGame)
    }

    @Test
    fun shouldFindGameById() {

        val gameId = UUID.randomUUID()

        val savedGame = Game(
            gameId,
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )

        `when`(games.get(gameId)).thenReturn(savedGame)

        val game = gamePersistenceServiceInMemoryImpl.findGameById(gameId)

        verify(games.get(gameId), times(1))
        assertEquals(game, savedGame)

    }

    @Test
    fun shouldUpdateGame() {

        val gameId = UUID.randomUUID()

        val savedGame = Game(
            gameId,
            mutableSetOf(),
            GameStatus.RUNNING,
            null
        )

        val updateGame = Game(
            gameId,
            mutableSetOf(),
            GameStatus.FINISHED,
            null
        )

        `when`(games.get(gameId)).thenReturn(savedGame)

        val game = gamePersistenceServiceInMemoryImpl.findGameById(gameId)

        verify(games.get(gameId), times(1))
        verify(games.put(gameId, updateGame), times(1))
        assertEquals(game, updateGame)
    }
}