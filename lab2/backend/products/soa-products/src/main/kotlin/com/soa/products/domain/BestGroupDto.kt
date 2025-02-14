package com.soa.products.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.soa.products.ejb.domain.BestGroup
import com.soa.products.ejb.domain.MusicGenre
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

@JacksonXmlRootElement(localName = "bestGroup")
data class BestGroupDto(
    var bandId: Int,
    var musicGenre: MusicGenre
)

fun BestGroupDto.toDomain() = BestGroup(
    bandId,
    musicGenre,
)