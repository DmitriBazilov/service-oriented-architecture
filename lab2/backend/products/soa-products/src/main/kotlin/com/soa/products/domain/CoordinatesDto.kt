package com.soa.products.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.soa.products.ejb.domain.Band
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

@JacksonXmlRootElement(localName = "coordinates")
data class CoordinatesDto(
    val x: Double,
    val y: Long? = null,
)

fun CoordinatesDto.toDomain() = Band.Coordinates(
    x = x,
    y = y!!
)