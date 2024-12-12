package soa.myts.bazilov.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import soa.myts.bazilov.infrastructure.LocalDateXmlAdapter
import soa.myts.bazilov.model.domain.Band
import soa.myts.bazilov.model.domain.MusicGenre
import java.time.LocalDate

@XmlRootElement(name = "band")
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
