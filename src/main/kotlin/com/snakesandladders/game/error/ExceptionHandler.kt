package com.snakesandladders.game.error

import com.snakesandladders.game.exception.GameNotFoundException
import com.snakesandladders.game.exception.PlayerNotFoundException
import com.snakesandladders.game.models.ErrorDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [GameNotFoundException::class, PlayerNotFoundException::class])
    fun errorHandler(exception: Exception): ResponseEntity<ErrorDTO> = ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        ErrorDTO(exception.message ?: "")
    )

}