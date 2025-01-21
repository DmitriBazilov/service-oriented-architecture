package com.soa.products.ejb.service.command

import com.soa.products.ejb.domain.Color
import java.io.Serializable

data class CreatePersonCommand(
    val name: String,
    val passportID: String,
    val eyeColor: Color?,
    val hairColor: Color?,
) : Serializable
