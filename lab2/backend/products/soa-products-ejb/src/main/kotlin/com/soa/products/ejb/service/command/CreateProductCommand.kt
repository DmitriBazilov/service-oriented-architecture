package com.soa.products.ejb.service.command

import com.soa.products.ejb.domain.Coordinates
import com.soa.products.ejb.domain.UnitOfMeasure
import java.io.Serializable

data class CreateProductCommand(
    val name: String,
    val coordinates: Coordinates,
    val price: Long,
    val manufacturerCost: Long,
    val unitOfMeasure: UnitOfMeasure,
    val partNumber: String?,
    val ownerPassportId: String?,
) : Serializable
