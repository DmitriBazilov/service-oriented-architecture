package com.soa.products.ejb.service;

import com.soa.products.ejb.domain.PriceOperation
import com.soa.products.ejb.domain.Product
import com.soa.products.ejb.domain.ProductSearchParams
import com.soa.products.ejb.service.command.CreateProductCommand
import com.soa.products.ejb.service.command.UpdateProductCommand
import jakarta.ejb.Remote;
import java.math.BigDecimal

@Remote
interface ProductService {
    fun createProduct(createUpdateProductCommand: CreateProductCommand): Long
    fun getProduct(id: Long): Product
    fun deleteProduct(id: Long)
    fun getTotalPrice(): Long
    fun getProductWithMinPartNumber(): Product
    fun getProductsWithManufacturerCostLessThan(cost: Long, page: Int, size: Int): List<Product>
    fun updateProduct(updateProductCommand: UpdateProductCommand)
    fun getProducts(productSearchParams: ProductSearchParams): List<Product>
    fun updatePrices(priceOperation: PriceOperation, percentage: BigDecimal)
}
