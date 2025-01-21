package com.soa.products.ejb.domain.search

import com.soa.products.ejb.utils.SortOrder
import java.io.Serializable

data class SortParam(
    val searchField: SearchField,
    val sortOrder: SortOrder
) : Serializable {
    init {
        require(searchField != SearchField.COORDINATES) { "COORDINATES are not suitable for sorting" }
    }
}
