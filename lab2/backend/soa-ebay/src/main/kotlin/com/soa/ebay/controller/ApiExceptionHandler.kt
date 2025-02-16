package com.soa.ebay.controller

import com.soa.ebay.client.RestTemplateErrorHandler
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(RestTemplateErrorHandler.WebException::class)
    fun handleException(e: RestTemplateErrorHandler.WebException) = ResponseEntity
        .status(e.status)
        .body(e.message)
}
