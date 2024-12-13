package soa.myts.bazilov.controller

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import soa.myts.bazilov.model.domain.MusicGenre
import soa.myts.bazilov.model.domain.filter.Field
import soa.myts.bazilov.model.domain.filter.Filter
import soa.myts.bazilov.model.domain.filter.Operator
import soa.myts.bazilov.model.domain.toDto
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

    @GET
    @Path("name/{name-substr}")
    fun getByNameSubstring(
        @PathParam("name-substr")
        nameSubstring: String
    ): Response {
        val bands = bandService.findByNameSubstring(nameSubstring)
        return Response.ok().entity(bands).build()
    }

    @GET
    @Path("genre/count")
    fun getCountWhereGenreLower(
        @QueryParam("genre")
        genre: MusicGenre
    ): Response {
        val bands = bandService.countGenres(genre).bands?.size
        return Response.ok().entity(bands).build()
    }

    @PATCH
    @Path("{band-id}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun update(
        @PathParam("band-id")
        bandId: Int,
        band: BandDto,
    ): Response {
        band.id = bandId
        val updatedBand = bandService.update(bandId, band.toDomain())
        return Response.ok().entity(
            updatedBand?.toDto() ?: "not found band with id $bandId"
        ).build()
    }

    @DELETE
    fun deleteByStudioId(
        @QueryParam("studioId")
        studioId: Int
    ): Response {
        val cnt = bandService.deleteByStudioId(studioId)
        if (cnt == 0) {
            return Response.status(400).entity("not found band with studioId $studioId").build()
        }
        return Response.ok().build()
    }
}

/*
TODO
pagination
second service
error handling
 */