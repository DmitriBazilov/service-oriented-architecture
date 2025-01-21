package com.soa.products.domain

import com.soa.products.ejb.domain.BestGroup
import com.soa.products.ejb.domain.MusicGenre
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

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