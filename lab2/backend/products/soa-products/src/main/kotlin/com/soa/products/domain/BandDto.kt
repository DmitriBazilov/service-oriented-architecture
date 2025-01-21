package com.soa.products.domain

import com.soa.products.ejb.domain.Band
import com.soa.products.ejb.domain.MusicGenre
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import java.time.LocalDate

@XmlRootElement(name = "MusicBand")
@XmlAccessorType(XmlAccessType.FIELD)
data class BandDto(
    var id: Int? = null,
    var name: String? = null,
    @XmlElement(name = "coordinates")
    var coordinates: CoordinatesDto? = null,
    var numberOfParticipants: Long,
    var albumsCount: Long,
    var description: String,
    var genre: MusicGenre,
    @field:XmlJavaTypeAdapter(value = LocalDateXmlAdapter::class)
    var creationDate: LocalDate?,
    @XmlElement(name = "studio")
    var studio: MusicStudioDto?,
)

fun BandDto.toDomain() = Band(
    id = id ?: 0,
    name = name!!,
    coordinates = coordinates!!.toDomain(),
    numberOfParticipants = numberOfParticipants,
    albumsCount = albumsCount,
    description = description,
    genre = genre,
    studio = studio?.toDomain(),
)