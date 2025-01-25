package com.soa.ebay.client

//import com.soa.ebay.model.BestGroupDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class BandClient(
    private val restTemplate: RestTemplate,
) {
//    fun rewardBand(bandGroupDto: BestGroupDto) {
//    }

    fun removeParticipants(bandId: Int): ResponseEntity<Unit> {
        return restTemplate.exchange<Unit>(
            "$URL/participant/$bandId",
            HttpMethod.POST,
            HttpEntity(null, HttpHeaders().apply {
                contentType = MediaType.APPLICATION_XML
                accept = listOf(MediaType.ALL)
            })
        )
    }

    companion object {
        private const val URL = "https://api-gateway:9912/bands"
    }
}