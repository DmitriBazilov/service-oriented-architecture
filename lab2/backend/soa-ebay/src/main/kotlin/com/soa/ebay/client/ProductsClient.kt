package com.soa.ebay.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Service
class ProductsClient(
    private val restTemplate: RestTemplate,
    @Value("\${products.url}")
    private val url: String,
) {
    fun decreasePrices(percentage: BigDecimal) {
        restTemplate.put(
            "$url/products/prices?percent=${percentage.toPlainString()}&priceOperation=DECREASE",
            null
        )
    }

    fun increasePrices(percentage: BigDecimal) {
        restTemplate.put(
            "$url/products/prices?percent=${percentage.toPlainString()}&priceOperation=INCREASE",
            null
        )
    }
}
