package com.soa.ebay.controller

import com.soa.ebay.logging.model.BestGroupDto
import com.soa.ebay.model.BestGroupDto
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response


@Path("/band")
class GrammyController {

    @POST
    @Path("{band-id}/participants/remove")
    fun removeParticipant(
        @PathParam("band-id")
        id: Int
    ): Response {
        val client = ClientBuilder.newClient()

        return client
            .target("$MAIN_BAND_SERVICE_URL/bands/participant/$id")
            .request(MediaType.APPLICATION_XML)
            .post(Entity.text(""))
    }

    @POST
    @Path("{band-id}/reward/{genre}")
    fun rewardBand(
        @PathParam("band-id")
        bandId: Int,
        @PathParam("genre")
        genre: String
    ): Response {
        val client = ClientBuilder.newClient()
        val bestGroupDto = BestGroupDto(bandId, genre)
        return client
            .target("$MAIN_BAND_SERVICE_URL/best-group")
            .request(MediaType.APPLICATION_XML)
            .post(Entity.xml(bestGroupDto))

    }

    @POST
    fun test(): Response {
        return Response.ok().build()
    }

    companion object {
        private const val MAIN_BAND_SERVICE_URL = "http://localhost:13337/main-service-1.0-SNAPSHOT"
    }
}

