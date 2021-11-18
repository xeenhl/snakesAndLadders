package com.snakesandladders.game.services

import com.snakesandladders.game.TestConstants.NAME
import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.persistence.GamePersistenceService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class GameServiceImplTest {

    @Mock
    lateinit var gamePersistenceService: GamePersistenceService

    @InjectMocks
    lateinit var gameServiceImpl: GameServiceImpl


    @Test
    fun shouldInitializeNewGame() {
        val savedGame = Game(UUID.randomUUID(), mutableSetOf(), GameStatus.RUNNING)
        `when`(gamePersistenceService.saveGame(any())).thenReturn(savedGame)

        val game = gameServiceImpl.initializeNewGame()

        assertEquals(game.players, savedGame.players)
        assertEquals(game.status, savedGame.status)
        assertEquals(game.winner, savedGame.winner)
    }

    @Test
    fun getGameById() {
        val gameId = UUID.randomUUID()
        val savedGame = Game(gameId, mutableSetOf(), GameStatus.RUNNING)
        `when`(gamePersistenceService.findGameById(gameId)).thenReturn(savedGame)

        val game = gameServiceImpl.getGameById(gameId)

        assertEquals(game, savedGame)
    }

    @Test
    fun addPlayerToGame() {
        val gameId = UUID.randomUUID()
        val player = Player(UUID.randomUUID(), NAME, mutableSetOf())
        val savedGame = Game(gameId, mutableSetOf(), GameStatus.RUNNING)
        val updatedGame = Game(gameId, mutableSetOf(PlayerInGame(player, 0, 0)), GameStatus.RUNNING)
        `when`(gamePersistenceService.updateGame(any())).thenReturn(updatedGame)

        val game = gameServiceImpl.addPlayerToGame(player, savedGame)

        assertEquals(game, updatedGame)
    }

    @Test
    fun updateGame() {
        val player = Player(UUID.randomUUID(), NAME, mutableSetOf())
        val updatedGame = Game(UUID.randomUUID(), mutableSetOf(PlayerInGame(player, 4, 0)), GameStatus.RUNNING)
        `when`(gamePersistenceService.updateGame(updatedGame)).thenReturn(updatedGame)

        val game = gameServiceImpl.updateGame(updatedGame)

        assertEquals(game, updatedGame)
    }

    @Test
    fun evalStep() {
        val player = Player(UUID.randomUUID(), NAME, mutableSetOf())
        val game = Game(UUID.randomUUID(), mutableSetOf(PlayerInGame(player, 4, 0)), GameStatus.RUNNING)

        gameServiceImpl.evalStep(game, player.id, 4)

        assertEquals(game.players.elementAt(0).position, 4)
    }

    @Test
    fun updatePlayerDiceRoll() {
        val player = Player(UUID.randomUUID(), NAME, mutableSetOf())
        val game = Game(UUID.randomUUID(), mutableSetOf(PlayerInGame(player, 0, 0)), GameStatus.RUNNING)

        gameServiceImpl.updatePlayerDiceRoll(game, player, 4)

        assertEquals(game.players.elementAt(0).lastDice, 4)
    }
}