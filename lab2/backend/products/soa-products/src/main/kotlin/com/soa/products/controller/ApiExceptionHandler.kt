package com.soa.products.controller

import com.soa.products.ejb.exception.BandOperationException
import com.soa.products.ejb.exception.BestGroupOperationException
import com.soa.products.ejb.exception.FilterException
import com.soa.products.ejb.exception.ProductWithMinPartNumberNotFound
import com.soa.products.ejb.exception.PersonDuplicationException
import com.soa.products.ejb.exception.PersonNotFoundException
import com.soa.products.ejb.exception.ProductDuplicationException
import com.soa.products.ejb.exception.ProductNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant

@ControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(FilterException::class)
    fun handleFilterException(e: FilterException) = ResponseEntity
        .status(e.status)
        .body(e.message)

    @ExceptionHandler(BandOperationException::class)
    fun bandOperationException(e: BandOperationException) = ResponseEntity
        .status(e.status)
        .body(e.message)

    @ExceptionHandler(BestGroupOperationException::class)
    fun bestGroupOperationException(e: BestGroupOperationException) = ResponseEntity
        .status(e.status)
        .body(e.message)
}
