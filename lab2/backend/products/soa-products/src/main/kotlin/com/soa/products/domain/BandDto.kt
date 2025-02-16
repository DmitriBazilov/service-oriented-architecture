package com.soa.products.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.soa.products.ejb.domain.Band
import com.soa.products.ejb.domain.MusicGenre
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import java.time.LocalDate
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

@JacksonXmlRootElement(localName = "MusicBand")
data class BandDto(
    var id: Int? = null,
    var name: String? = null,
    @JacksonXmlProperty(localName = "coordinates")
    var coordinates: CoordinatesDto? = null,
    var numberOfParticipants: Long,
    var albumsCount: Long,
    var description: String,
    var genre: MusicGenre,
    var creationDate: LocalDate?,
    @JacksonXmlProperty(localName = "studio")
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

fun BandDto.toSoap() = com.soa.products.generated.BandDto().let {
    it.id = id
    it.name = name
    it.coordinates = coordinates?.toSoap()
    it.numberOfParticipants = numberOfParticipants
    it.albumsCount = albumsCount
    it.description = description
    it.genre = com.soa.products.generated.MusicGenre.valueOf(genre.name)
    it.creationDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(creationDate.toString())
    it.studio = studio?.toSoap()
    it
}