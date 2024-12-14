package soa.myts.bazilov.model.domain.filter

import jakarta.ws.rs.core.Response.Status
import soa.myts.bazilov.model.dto.Response
import soa.myts.bazilov.model.dto.WebException

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
        field?.let { it1 -> SortClause(it1, it) }
            ?: throw WebException(Response("unsupported field"), Status.BAD_REQUEST)
    } ?: throw WebException(Response("unsupported sort type"), Status.BAD_REQUEST)
}

val String.sortType: SortType?
    get() = SortType.entries.associateBy { it.domainName }[this]
