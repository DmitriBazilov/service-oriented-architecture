package com.soa.products.domain

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
data class Response(
    val message: String,
)
