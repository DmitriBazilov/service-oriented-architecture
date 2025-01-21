package soa.myts.bazilov.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
data class BandListDto(

    @field:XmlElement(name = "MusicBand")
    var bands: List<BandDto>? = null,

    var pageSize: Int? = null,

    var curentPage: Int? = null,

    var totalPages: Int? = null,
)
