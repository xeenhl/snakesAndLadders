package com.snakesandladders.game.controller

import com.snakesandladders.game.models.Game
import com.snakesandladders.game.models.GameStatus
import com.snakesandladders.game.models.Player
import com.snakesandladders.game.models.PlayerInGame
import com.snakesandladders.game.services.DiceService
import com.snakesandladders.game.services.GameService
import com.snakesandladders.game.services.PlayerService

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class GameControllerTest {


    @Mock
    lateinit var gameService: GameService
    @Mock
    lateinit var playerService: PlayerService
    @Mock
    lateinit var diceService: DiceService

    @InjectMocks
    lateinit var gameController: GameController

    @Test
    fun shouldCreateNewGame() {

        val newGame = Game(UUID.randomUUID(), mutableSetOf(), GameStatus.RUNNING, null)

        `when`(gameService.initializeNewGame()).thenReturn(newGame)

        val game = gameController.createNewGame()

        assertEquals(newGame, game.body)
    }

    @Test
    fun shouldGetGameById() {

        val gameId = UUID.randomUUID()
        val newGame = Game(gameId, mutableSetOf(), GameStatus.RUNNING, null)

        `when`(gameService.getGameById(gameId)).thenReturn(newGame)

        val game = gameController.getGameById(gameId.toString())

        assertEquals(newGame, game.body)
    }

    @Test
    fun shouldAddPlayerToGame() {
        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val game = Game(gameId, mutableSetOf(), GameStatus.RUNNING, null)
        val player = Player(playerId, "Name", mutableSetOf())

        `when`(gameService.getGameById(gameId)).thenReturn(game)
        `when`(playerService.getPlayerById(playerId)).thenReturn(player)
        game.players.add(PlayerInGame(player, 0, 0))
        `when`(gameService.addPlayerToGame(player, game)).thenReturn(game)

        val updatedGame = gameController.addPlayerToGame(playerId.toString(), gameId.toString())

        assertTrue(updatedGame.body?.players?.map { it.player.id }?.contains(playerId) ?: false)
    }

    @Test
    fun shouldRollDiceForPlayer() {

        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val player = Player(playerId, "Name", mutableSetOf())
        val game = Game(gameId, mutableSetOf(PlayerInGame(player, 0, 0)), GameStatus.RUNNING, null)

        `when`(gameService.getGameById(gameId)).thenReturn(game)
        `when`(playerService.getPlayerById(playerId)).thenReturn(player)
        `when`(diceService.roll()).thenReturn(2)

        val result = gameController.diceRollForPlayer(playerId.toString(), gameId.toString())

        assertEquals(playerId, result.body?.playerId)
        assertEquals(2, result.body?.result)
    }

    @Test
    fun shouldMovePlayerInGame() {

        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val player = Player(playerId, "Name", mutableSetOf())
        val game = Game(gameId, mutableSetOf(PlayerInGame(player, 2, 0)), GameStatus.RUNNING, null)
        val updatedGame = Game(gameId, mutableSetOf(PlayerInGame(player, 2, 2)), GameStatus.RUNNING, null)

        `when`(gameService.getGameById(gameId)).thenReturn(game)
        `when`(gameService.updateGame(any())).thenReturn(updatedGame)

        val result = gameController.movePlayerInGame(playerId.toString(), gameId.toString(), 2)

        assertEquals(result.body?.players?.first { it.player == player }?.position, 2)
    }
}