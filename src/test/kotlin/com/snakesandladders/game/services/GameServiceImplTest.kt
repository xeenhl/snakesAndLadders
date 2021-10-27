package com.snakesandladders.game.services

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.persistence.GamePersistenceService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GameServiceImplTest {

    @Mock
    lateinit var gamePersistenceService: GamePersistenceService

    @InjectMocks
    lateinit var gameServiceImpl: GameServiceImpl

    val NAME = "Name"

    @Test
    fun shouldInitializeNewGame() {
        val savedGame = Game(UUID.randomUUID(), mutableSetOf(), GameStatus.RUNNING, null)
        `when`(gamePersistenceService.saveGame(savedGame)).thenReturn(savedGame)

        val game = gameServiceImpl.initializeNewGame()

        assertEquals(game, savedGame)
    }

    @Test
    fun getGameById() {
        val gameId = UUID.randomUUID()
        val savedGame = Game(gameId, mutableSetOf(), GameStatus.RUNNING, null)
        `when`(gamePersistenceService.findGameById(gameId)).thenReturn(savedGame)

        val game = gameServiceImpl.getGameById(gameId)

        assertEquals(game, savedGame)
    }

    @Test
    fun addPlayerToGame() {
        val gameId = UUID.randomUUID()
        val player = Player(UUID.randomUUID(), NAME, mutableSetOf())
        val savedGame = Game(gameId, mutableSetOf(), GameStatus.RUNNING, null)
        val updatedGame = Game(gameId, mutableSetOf(PlayerInGame(player, 0, 0)), GameStatus.RUNNING, null)
        `when`(gamePersistenceService.findGameById(gameId)).thenReturn(savedGame)
        `when`(gamePersistenceService.updateGame(updatedGame)).thenReturn(updatedGame)

        val game = gameServiceImpl.addPlayerToGame(player, savedGame)

        assertEquals(game, updatedGame)
    }

    @Test
    fun updateGame() {
        val gameId = UUID.randomUUID()
        val player = Player(UUID.randomUUID(), NAME, mutableSetOf())
        val savedGame = Game(gameId, mutableSetOf(PlayerInGame(player, 0, 0)), GameStatus.RUNNING, null)
        val updatedGame = Game(gameId, mutableSetOf(PlayerInGame(player, 4, 0)), GameStatus.RUNNING, null)
        `when`(gamePersistenceService.findGameById(gameId)).thenReturn(savedGame)
        `when`(gamePersistenceService.updateGame(updatedGame)).thenReturn(updatedGame)

        val game = gameServiceImpl.getGameById(gameId)

        assertEquals(game, updatedGame)
    }
}