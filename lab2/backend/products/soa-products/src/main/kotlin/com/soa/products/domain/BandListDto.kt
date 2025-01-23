package com.soa.products.domain

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@JacksonXmlRootElement
data class BandListDto(

    @JacksonXmlProperty(localName = "bands")
    var bands: List<BandDto>? = null,

    var pageSize: Int? = null,

    var curentPage: Int? = null,

    var totalPages: Int? = null,
)