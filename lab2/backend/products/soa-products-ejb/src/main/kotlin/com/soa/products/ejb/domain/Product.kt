package com.soa.products.ejb.domain

import java.io.Serializable
import java.time.LocalDate

data class Product(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val creationDate: LocalDate,
    val price: Long,
    val partNumber: String?,
    val manufacturerCost: Long,
    val unitOfMeasure: UnitOfMeasure,
    val owner: Person?,
) : Serializable

data class Coordinates(
    val x: Int,
    val y: Int,
) : Serializable

enum class UnitOfMeasure {
    KILOGRAMS,
    METERS,
    MILLILITERS,
    MILLIGRAMS,
}
