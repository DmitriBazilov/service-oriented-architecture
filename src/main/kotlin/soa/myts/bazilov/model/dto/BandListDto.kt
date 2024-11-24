package soa.myts.bazilov.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlElementWrapper
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "bands")
@XmlAccessorType(XmlAccessType.FIELD)
data class BandListDto(

    @XmlElement(name = "band")
    var bands: List<BandDto>? = null,
)
