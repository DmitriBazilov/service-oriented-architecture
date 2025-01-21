package com.soa.products.ejb.domain.filter

import com.soa.products.ejb.domain.MusicGenre
import com.soa.products.ejb.exception.FilterException
import jakarta.ws.rs.core.Response
import java.time.LocalDate
import java.time.format.DateTimeParseException


class Filter(
    val field: Field,
    val operator: Operator,
    val value: Any,
)

fun String.filter(): Filter {
    val field = substringBefore('[').field
    println("fieldName: ${substringBefore('[')}")
    val operator = substringBefore(']').substringAfter('[').operator
    val value = field?.let { f ->
        val t = this.substringAfter('=')
        println("stringValue = $t")
        return@let t.parse(f.valueType)
    }
    println("$field, $operator, $value")

   return Filter(
        field ?: throw FilterException.UnsupportedFieldException("not supported field", Response.Status.BAD_REQUEST.statusCode),
        operator ?: throw FilterException.UnsupportedOperatorException("not supported operator", Response.Status.BAD_REQUEST.statusCode),
        value ?: throw FilterException.ValueParseException("cant parse value. Unsupported type", Response.Status.BAD_REQUEST.statusCode),
    )
}

fun String.parse(type: Type): Any? = when (type) {
    Type.INT -> this.toIntOrNull()
    Type.LONG -> this.toLongOrNull()
    Type.DOUBLE -> this.toDoubleOrNull()
    Type.STRING -> this
    Type.LOCAL_DATE -> this.toLocalDateOrNull()
    Type.MUSIC_GENRE -> MusicGenre.valueOf(this)
}

fun String.toLocalDateOrNull(): LocalDate? =
    try {
        LocalDate.parse(this)
    } catch (e: DateTimeParseException) {
        null
    }