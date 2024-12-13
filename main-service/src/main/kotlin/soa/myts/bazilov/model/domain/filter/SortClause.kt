package soa.myts.bazilov.model.domain.filter

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
    println("SORT CLAUSE: ${field?.domainName}, ${sortType?.domainName}")
    return sortType?.let { field?.let { it1 -> SortClause(it1, it) } }
}

val String.sortType: SortType?
    get() = SortType.entries.associateBy { it.domainName }[this]
