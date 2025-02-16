package com.soa.products.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.soa.products.ejb.domain.MusicStudio
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

@JacksonXmlRootElement(localName = "studio")
data class MusicStudioDto(
    var id: Int? = null,
    var name: String,
    var address: String?,
)

fun MusicStudioDto.toDomain() = MusicStudio(
    id = id ?: 0,
    name = name,
    address = address,
)

fun MusicStudioDto.toSoap() = com.soa.products.generated.MusicStudioDto().let {
    it.id = id
    it.name = name
    it.address = address
    it
}