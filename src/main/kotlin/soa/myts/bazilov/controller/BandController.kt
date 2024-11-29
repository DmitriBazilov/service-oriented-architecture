package soa.myts.bazilov.controller

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import soa.myts.bazilov.model.dto.BandDto
import soa.myts.bazilov.model.dto.toDomain
import soa.myts.bazilov.service.BandService

@Path("/bands")
class BandController {

    @Inject
    private lateinit var bandService: BandService

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getBands(
        @QueryParam("filter")
        filterList: List<String>
    ): Response {
        val banditos = bandService.getBands()
        return Response.ok().entity(banditos).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun saveBand(band: BandDto): Response {
        val resultBand = bandService.saveBand(band.toDomain())
        return Response.ok().entity(resultBand).build()
    }
}