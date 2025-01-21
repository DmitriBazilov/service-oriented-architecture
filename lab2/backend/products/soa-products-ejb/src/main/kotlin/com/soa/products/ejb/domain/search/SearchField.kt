package com.soa.products.ejb.domain.search

import com.soa.products.ejb.domain.Coordinates
import com.soa.products.ejb.domain.UnitOfMeasure
import java.time.LocalDate

private val defaultOperators = setOf(
    ComparisonOperator.EQUAL,
    ComparisonOperator.GREATER,
    ComparisonOperator.GREATER_OR_EQUAL,
    ComparisonOperator.LESS,
    ComparisonOperator.LESS_OR_EQUAL
)

enum class SearchField(
    val supportedOperators: Set<ComparisonOperator>,
    val parseFunction: (String) -> Any,
) {
    ID(defaultOperators, String::toLong),
    NAME(defaultOperators, { it }),
    COORDINATES(
        setOf(ComparisonOperator.SAME),
        {
            val params = it.split(":")
            Coordinates(params[0].toInt(), params[1].toInt())
        }
    ),
    CREATION_DATE(defaultOperators, { LocalDate.parse(it) }),
    PRICE(defaultOperators, String::toLong),
    PART_NUMBER(defaultOperators, { it }),
    MANUFACTURER_COST(defaultOperators, String::toLong),
    UNIT_OF_MEASURE(setOf(ComparisonOperator.EQUAL), { UnitOfMeasure.valueOf(it) }),
    OWNER_PASSPORT_ID(defaultOperators, { it }),
}
