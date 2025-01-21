package com.soa.products.ejb.domain.filter

import com.soa.products.ejb.exception.FilterException
import jakarta.ws.rs.core.Response


class SortClause(
    val field: Field,
    val sortType: SortType,
)

enum class SortType(
    val domainName: String,
) {
    ASC("asc"),
    DESC("desc"),
}

fun String.sortClause(): SortClause? {

    val field = substringBefore('[').field
    val sortType = substringBefore(']').substringAfter('[').sortType
    return sortType?.let {
        field?.let {
            it1 -> SortClause(it1, it)
        } ?: throw FilterException.UnsupportedFieldException("not supported field", Response.Status.BAD_REQUEST.statusCode)
    } ?: throw FilterException.UnsupportedSortTypeException("not supported sort type", Response.Status.BAD_REQUEST.statusCode)
}

val String.sortType: SortType?
    get() = SortType.entries.associateBy { it.domainName }[this]
