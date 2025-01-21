package com.soa.products.ejb.exception

class ProductNotFoundException(id: Long) : RuntimeException("Product not found with id=$id")

class ProductDuplicationException(partNumber: String) :
    RuntimeException("Product already exists with partNumber=$partNumber")

class ProductWithMinPartNumberNotFound(): RuntimeException("No product with a minimum partNumber found")
