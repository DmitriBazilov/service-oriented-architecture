package com.soa.products.domain

import jakarta.xml.bind.annotation.adapters.XmlAdapter
import java.time.LocalDate

class LocalDateXmlAdapter : XmlAdapter<String, LocalDate>() {

    override fun unmarshal(date: String?): LocalDate? = date?.let { LocalDate.parse(it) }

    override fun marshal(date: LocalDate?): String? = date?.toString()
}
