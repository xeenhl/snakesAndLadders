package com.snakesandladders.game.persistence

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.UUID


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
            GameStatus.RUNNING
        )

        `when`(games.get(gameId)).thenReturn(game)

        val savedGame = gamePersistenceServiceInMemoryImpl.saveGame(game)

        assertEquals(game, savedGame)
    }

    @Test
    fun shouldFindGameById() {

        val gameId = UUID.randomUUID()

        val savedGame = Game(
            gameId,
            mutableSetOf(),
            GameStatus.RUNNING
        )

        `when`(games.containsKey(gameId)).thenReturn(true)
        `when`(games.get(gameId)).thenReturn(savedGame)

        val game = gamePersistenceServiceInMemoryImpl.findGameById(gameId)

        assertEquals(game, savedGame)

    }

    @Test
    fun shouldUpdateGame() {

        val gameId = UUID.randomUUID()

        val updateGame = Game(
            gameId,
            mutableSetOf(),
            GameStatus.FINISHED
        )

        `when`(games.containsKey(gameId)).thenReturn(true)
        `when`(games.get(gameId)).thenReturn(updateGame)

        val game = gamePersistenceServiceInMemoryImpl.updateGame(updateGame)

        assertEquals(game, updateGame)
    }
}