package com.soa.products.domain

import com.soa.products.ejb.domain.MusicStudio
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "studio")
@XmlAccessorType(XmlAccessType.FIELD)
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