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