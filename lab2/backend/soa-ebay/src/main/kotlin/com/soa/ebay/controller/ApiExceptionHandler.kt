package com.soa.ebay.controller

import generated.soa.ebay.dto.ApiErrorTo
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

@ControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(
        value = [
            IllegalArgumentException::class,
            ConstraintViolationException::class,
            MethodArgumentNotValidException::class]
    )
    fun onConstraintViolation(e: Exception): ResponseEntity<ApiErrorTo> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiErrorTo(Instant.now().toString(), e.message))
}
