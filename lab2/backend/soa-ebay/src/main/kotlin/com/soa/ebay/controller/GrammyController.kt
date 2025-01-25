package com.soa.ebay.controller

import com.soa.ebay.client.BandClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    path = ["/bands"],
    produces = ["application/xml"],
    consumes = ["application/xml"]
)
class GrammyController(
    private val bandClient: BandClient,
) {

    @PostMapping("/{band-id}/participants/remove")
    fun removeParticipant(
        @PathVariable("band-id")
        id: Int
    ): ResponseEntity<*> {
        return bandClient.removeParticipants(id)
    }

//    @POST
//    @Path("{band-id}/reward/{genre}")
//    fun rewardBand(
//        @PathParam("band-id")
//        bandId: Int,
//        @PathParam("genre")
//        genre: String
//    ): Response {
//        val client = ClientBuilder.newClient()
//        val bestGroupDto = BestGroupDto(bandId, genre)
//        return client
//            .target("$MAIN_BAND_SERVICE_URL/best-group")
//            .request(MediaType.APPLICATION_XML)
//            .post(Entity.xml(bestGroupDto))
//
//    }
//
//    @POST
//    fun test(): Response {
//        return Response.ok().build()
//    }
//
//    companion object {
//        private const val MAIN_BAND_SERVICE_URL = "http://localhost:13337/main-service-1.0-SNAPSHOT"
//    }
}

