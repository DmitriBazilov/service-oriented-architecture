package com.soa.products.ejb.domain

import java.io.Serializable

data class Person(
    val name: String,
    val passportID: String,
    val eyeColor: Color?,
    val hairColor: Color?,
) : Serializable

enum class Color {
    YELLOW,
    WHITE,
    BROWN,
    GREEN,
    RED,
    BLACK,
    ORANGE,
}
