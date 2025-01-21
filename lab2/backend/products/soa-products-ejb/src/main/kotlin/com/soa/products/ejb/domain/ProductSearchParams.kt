package com.soa.products.ejb.domain

import com.soa.products.ejb.domain.search.FilterParam
import com.soa.products.ejb.domain.search.SortParam
import java.io.Serializable

class ProductSearchParams private constructor(
    val filterParams: List<FilterParam>,
    val sortParams: List<SortParam>,

    val limit: Int,
    val offset: Int,
) : Serializable {
    data class Builder(
        private var filterParams: List<FilterParam> = listOf(),
        private var sortParams: List<SortParam> = listOf(),
        private var limit: Int = 10,
        private var offset: Int = 0
    ) {
        fun filterParams(params: List<FilterParam>) = apply { this.filterParams = params }
        fun sortParams(params: List<SortParam>) = apply { this.sortParams = params }
        fun limit(limit: Int) = apply {
            require(limit in 1..1000) { "Limit must be non-negative and be less than 1000" }
            this.limit = limit
        }

        fun offset(offset: Int) = apply {
            require(offset >= 0) { "Offset must be non-negative" }
            this.offset = offset
        }

        fun build() = ProductSearchParams(
            filterParams,
            sortParams,
            limit,
            offset,
        )
    }
}
