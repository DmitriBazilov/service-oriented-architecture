package soa.myts.bazilov.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement
import soa.myts.bazilov.model.domain.Band

@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
data class CoordinatesDto(
    val x: Double,
    val y: Long? = null,
)

fun CoordinatesDto.toDomain() = Band.Coordinates(
    x = x,
    y = y!!
)
