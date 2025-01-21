package com.soa.ebay.client

import com.soa.ebay.model.BestGroupDto
import jakarta.ws.rs.core.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.RestTemplate

class BandClient(
    private val restTemplate: RestTemplate,
    @Value("\${band.url}")
    private val url: String,
) {
    fun rewardBand(bandGroupDto: BestGroupDto) {
        restTemplate.postForEntity(
            "$url/best-group",
            bandGroupDto,
            Response::class.java
        )
    }

    fun removeParticipants(bandId: Int) {
        restTemplate.postForEntity(
            "$url/$bandId/participants/remove",
            null,
            Response::class.java
        )
    }
}