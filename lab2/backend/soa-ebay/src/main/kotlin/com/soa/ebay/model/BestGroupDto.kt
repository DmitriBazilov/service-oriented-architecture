package com.soa.ebay.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "bestGroup")
data class BestGroupDto(
    var bandId: Int,
    var musicGenre: String,
)