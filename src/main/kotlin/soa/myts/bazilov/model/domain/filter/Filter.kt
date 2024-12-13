package soa.myts.bazilov.model.domain.filter

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
    return Filter(field!!, operator!!, value!!)
}

fun String.parse(type: Type): Any? = when (type) {
    Type.INT -> this.toIntOrNull()
    Type.LONG -> this.toLongOrNull()
    Type.DOUBLE -> this.toDoubleOrNull()
    Type.STRING -> this
    Type.LOCAL_DATE -> this.toLocalDateOrNull()
}

fun String.toLocalDateOrNull(): LocalDate? =
    try {
        LocalDate.parse(this)
    } catch (e: DateTimeParseException) {
        null
    }
