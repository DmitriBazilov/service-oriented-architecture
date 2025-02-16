package com.soa.ebay.client

//import com.soa.ebay.model.BestGroupDto
import com.soa.ebay.model.BestGroupDto
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.net.URI

@Component
class BandClient(
    private val restTemplate: RestTemplate,
) {

    fun removeParticipants(bandId: Int): ResponseEntity<Unit> {
        return restTemplate.exchange<Unit>(
            "$MULE_URL/$bandId/participants/remove",
            HttpMethod.POST,
            HttpEntity(null, HttpHeaders().apply {
                contentType = MediaType.APPLICATION_XML
                accept = listOf(MediaType.ALL)
            })
        )
    }

    fun rewardBand(rewardDto: BestGroupDto): ResponseEntity<BestGroupDto> {
        return restTemplate.postForEntity(
            "$BEST_GROUP_URL",
            HttpEntity(
                rewardDto,
                HttpHeaders().apply {
                    contentType = MediaType.APPLICATION_XML
                    accept = listOf(MediaType.ALL)
                }
            ),
            BestGroupDto::class.java
        )
    }

    companion object {
        private const val URL = "https://api-gateway:9912/bands"
        private const val MULE_URL = "http://host.docker.internal:8081/"
        private const val BEST_GROUP_URL = "https://api-gateway:9912/best-group"
    }
}