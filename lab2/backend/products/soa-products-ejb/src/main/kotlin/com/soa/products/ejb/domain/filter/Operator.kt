package com.soa.products.ejb.domain.filter

enum class Operator(
    val domainOp: String,
    val dbOp: String,
) {
    EQ("eq", "="),
    NEQ("neq", "!="),
    LT("lt", "<"),
    LTE("lte", "<="),
    GT("gt", ">"),
    GTE("gte", ">="),
    LIKE("~", "like"),
}

val String.operator: Operator?
    get() = Operator.entries.associateBy { it.domainOp }[this]