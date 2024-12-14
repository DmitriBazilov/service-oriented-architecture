package soa.myts.bazilov.model.domain.filter

import jakarta.ws.rs.core.Response.Status
import soa.myts.bazilov.model.domain.MusicGenre
import soa.myts.bazilov.model.dto.Response
import soa.myts.bazilov.model.dto.WebException
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
        field ?: throw WebException(Response("not supported field"), Status.BAD_REQUEST),
        operator ?: throw WebException(Response("not supported operator"), Status.BAD_REQUEST),
        value ?: throw WebException(Response("cant parse value. Unsupported type"), Status.BAD_REQUEST),
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
