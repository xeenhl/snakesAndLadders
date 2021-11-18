package com.snakesandladders.game.controller

import com.snakesandladders.game.TestConstants.NAME
import com.snakesandladders.game.models.*
import com.snakesandladders.game.services.DiceService
import com.snakesandladders.game.services.GameService
import com.snakesandladders.game.services.PlayerService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.UUID

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

    companion object {
        const val INITIAL_DICE_ROLL = 0
        const val INITIAL_POSITION = 0
        const val DICE_ROLL_RESULT = 2
        const val UPDATED_POSITION = 2
    }

    @Test
    fun shouldCreateNewGame() {

        val newGame = Game(UUID.randomUUID(), mutableSetOf(), GameStatus.RUNNING)

        `when`(gameService.initializeNewGame()).thenReturn(newGame)

        val game = gameController.createNewGame()

        assertEquals(newGame.toGameDTO(), game.body)
    }

    @Test
    fun shouldGetGameById() {

        val gameId = UUID.randomUUID()
        val newGame = Game(gameId, mutableSetOf(), GameStatus.RUNNING)

        `when`(gameService.getGameById(gameId)).thenReturn(newGame)

        val game = gameController.getGameById(gameId.toString())

        assertEquals(newGame.toGameDTO(), game.body)
    }

    @Test
    fun shouldAddPlayerToGame() {
        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val game = Game(gameId, mutableSetOf(), GameStatus.RUNNING)
        val player = Player(playerId, NAME, mutableSetOf())

        `when`(gameService.getGameById(gameId)).thenReturn(game)
        `when`(playerService.getPlayerById(playerId)).thenReturn(player)
        game.players.add(PlayerInGame(player, INITIAL_DICE_ROLL, INITIAL_POSITION))
        `when`(gameService.addPlayerToGame(player, game)).thenReturn(game)

        val updatedGame = gameController.addPlayerToGame(playerId.toString(), gameId.toString())

        assertTrue(updatedGame.body?.players?.map { it.player.id }?.contains(playerId) ?: false)
    }

    @Test
    fun shouldRollDiceForPlayer() {

        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val player = Player(playerId, NAME, mutableSetOf())
        val game = Game(gameId, mutableSetOf(PlayerInGame(player, INITIAL_DICE_ROLL, INITIAL_POSITION)), GameStatus.RUNNING)

        `when`(gameService.getGameById(gameId)).thenReturn(game)
        `when`(playerService.getPlayerById(playerId)).thenReturn(player)
        `when`(diceService.roll()).thenReturn(DICE_ROLL_RESULT)

        val result = gameController.diceRollForPlayer(playerId.toString(), gameId.toString())

        assertEquals(playerId, result.body?.playerId)
        assertEquals(DICE_ROLL_RESULT, result.body?.result)
    }

    @Test
    fun shouldMovePlayerInGame() {

        val gameId = UUID.randomUUID()
        val playerId = UUID.randomUUID()
        val player = Player(playerId, NAME, mutableSetOf())
        val game = Game(gameId, mutableSetOf(PlayerInGame(player, DICE_ROLL_RESULT, INITIAL_POSITION)), GameStatus.RUNNING)
        val updatedGame = Game(gameId, mutableSetOf(PlayerInGame(player, DICE_ROLL_RESULT, UPDATED_POSITION)), GameStatus.RUNNING)

        `when`(gameService.getGameById(gameId)).thenReturn(game)
        `when`(gameService.updateGame(any())).thenReturn(updatedGame)

        val result = gameController.movePlayerInGame(playerId.toString(), gameId.toString(), UPDATED_POSITION)

        assertEquals(result.body?.players?.first { it.player == player }?.position, UPDATED_POSITION)
    }
}