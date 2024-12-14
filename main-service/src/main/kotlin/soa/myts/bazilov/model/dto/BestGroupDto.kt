package soa.myts.bazilov.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement
import soa.myts.bazilov.model.domain.BestGroup
import soa.myts.bazilov.model.domain.MusicGenre

@XmlRootElement(name = "bestGroup")
@XmlAccessorType(XmlAccessType.FIELD)
data class BestGroupDto(
    var bandId: Int,
    var musicGenre: MusicGenre
)

fun BestGroupDto.toDomain() = BestGroup(
    bandId,
    musicGenre,
)