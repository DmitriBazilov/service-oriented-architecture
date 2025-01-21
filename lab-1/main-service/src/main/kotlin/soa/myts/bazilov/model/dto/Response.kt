package soa.myts.bazilov.model.dto

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
data class Response(
    val message: String,
)
