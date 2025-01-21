package com.soa.products.ejb.domain.search

import java.io.Serializable

class FilterParam(
    val searchField: SearchField,
    val comparisonOperator: ComparisonOperator,
    rawValue: String,
) : Serializable {
    val value: Any

    init {
        require(comparisonOperator in searchField.supportedOperators) {
            "Given operator=$comparisonOperator not allowed for ${this::class.simpleName} param, " +
                "allowed operators=${searchField.supportedOperators}"
        }

        try {
            value = searchField.parseFunction(rawValue)
        } catch (e: Exception) {
            throw IllegalArgumentException("Wrong value type provided for $searchField", e)
        }
    }
}
