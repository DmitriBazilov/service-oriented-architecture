package com.soa.products.ejb.service

import com.soa.products.ejb.dao.PersonDao
import com.soa.products.ejb.dao.ProductDao
import com.soa.products.ejb.service.command.CreateProductCommand
import com.soa.products.ejb.service.command.UpdateProductCommand
import com.soa.products.ejb.domain.PriceOperation
import com.soa.products.ejb.domain.Product
import com.soa.products.ejb.domain.ProductSearchParams
import com.soa.products.ejb.exception.ProductWithMinPartNumberNotFound
import com.soa.products.ejb.exception.PersonNotFoundException
import com.soa.products.ejb.exception.ProductNotFoundException
import jakarta.ejb.Stateless
import jakarta.inject.Inject
import org.jboss.ejb3.annotation.Pool
import java.math.BigDecimal

@Stateless
@Pool("products-pool")
open class ProductServiceImpl : ProductService {
    @Inject
    private lateinit var personDao: PersonDao

    @Inject
    private lateinit var productDao: ProductDao

    override fun createProduct(createUpdateProductCommand: CreateProductCommand): Long {
        createUpdateProductCommand.ownerPassportId?.let {
            if (!personDao.existsById(it)) {
                throw PersonNotFoundException(it)
            }
        }

        return productDao.insert(createUpdateProductCommand)
    }

    override fun getProduct(id: Long): Product = productDao.findById(id) ?: throw ProductNotFoundException(id)

    override fun deleteProduct(id: Long) {
        if (!productDao.deleteById(id)) {
            throw ProductNotFoundException(id)
        }
    }

    override fun getTotalPrice(): Long {
        return productDao.getTotalPrice()
    }

    override fun getProductWithMinPartNumber(): Product {
        return productDao.findProductWithMinPartNumber()
            ?: throw ProductWithMinPartNumberNotFound()
    }

    override fun getProductsWithManufacturerCostLessThan(cost: Long, page: Int, size: Int): List<Product> {
        val offset = (page - 1) * size
        return productDao.findProductsWithManufacturerCostLessThan(cost, offset, size)
    }

    override fun updateProduct(updateProductCommand: UpdateProductCommand) {
        updateProductCommand.ownerPassportId?.let {
            if (!personDao.existsById(it)) {
                throw PersonNotFoundException(it)
            }
        }

        if (!productDao.update(updateProductCommand)) {
            throw ProductNotFoundException(updateProductCommand.id)
        }
    }

    override fun getProducts(productSearchParams: ProductSearchParams): List<Product> = productDao.find(productSearchParams)

    override fun updatePrices(priceOperation: PriceOperation, percentage: BigDecimal) {
        val coef = when (priceOperation) {
            PriceOperation.INCREASE -> BigDecimal.ONE.plus(percentage.divide(BigDecimal(100)))
            PriceOperation.DECREASE -> {
                if (percentage > BigDecimal(100)) {
                    throw IllegalArgumentException("Decrease percent must be less than 100")
                }

                BigDecimal.ONE.minus(percentage.divide(BigDecimal(100)))
            }
        }

        productDao.updateProductPrices(coef)
    }
}
