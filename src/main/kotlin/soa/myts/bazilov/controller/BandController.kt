package soa.myts.bazilov.controller

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import soa.myts.bazilov.service.BandService

@Path("/bands")
class BandController {

    @Inject
    private lateinit var bandService: BandService

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getBands(): Response {
        val banditos = bandService.getBands()
        return Response.ok().entity(banditos).build()
    }
}