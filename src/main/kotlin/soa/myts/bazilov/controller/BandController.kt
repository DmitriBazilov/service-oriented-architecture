package soa.myts.bazilov.controller

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
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
        filterList: List<String>,
        @QueryParam("sort")
        sortClause: String?,
    ): Response {
        val banditos = bandService.getBands(filterList, sortClause)
        return Response.ok().entity(banditos).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun saveBand(band: BandDto): Response {
        val resultBand = bandService.saveBand(band.toDomain())
        return Response.ok().entity(resultBand).build()
    }

    @DELETE
    @Path("{id}")
    fun deleteBand(
        @PathParam("id")
        id: Int
    ): Response {
        val cnt = bandService.deleteById(id)
        if (cnt == 0) {
            return Response.status(400).entity("not found band with id $id").build()
        }
        return Response.ok().build()
    }

    @GET
    @Path("{band-id}")
    fun getById(
        @PathParam("band-id")
        id: Int
    ): Response {
        val band = bandService.findById(id)
        val response = band?.let {
            Response.ok().entity(it).build()
        } ?: Response.status(400).entity("not found band with id $id").build()
        return response
    }
}