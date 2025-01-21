package com.soa.products.controller

import com.soa.products.domain.BandDto
import com.soa.products.domain.BandListDto
import com.soa.products.domain.toDomain
import com.soa.products.domain.toDto
import com.soa.products.ejb.domain.MusicGenre
import com.soa.products.ejb.exception.BandOperationException
import com.soa.products.ejb.service.BandService
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
import jakarta.ws.rs.core.Response.Status
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["/bands"],
    produces = ["application/xml"],
    consumes = ["application/xml"]
)
class BandController(
    private val bandService: BandService
) {

    @GetMapping
    fun getBands(
        @RequestParam("filter")
        filterList: List<String>,
        @RequestParam("sort")
        sortClause: String?,
        @RequestParam("page")
        page: Int?,
        @RequestParam("size")
        size: Int?,
    ): ResponseEntity<*> {
        page?.let {
            if (it < 1) throw BandOperationException.BandOperationParamsException("negative page number", Status.BAD_REQUEST.statusCode)
        }
        size?.let {
            if (it < 1) throw BandOperationException.BandOperationParamsException("negative page size", Status.BAD_REQUEST.statusCode)
        }
        val banditos = bandService.getBands(filterList, sortClause, page ?: 1, size ?: 10).map { it.toDto() }
        return ResponseEntity.ok(BandListDto(banditos, size, page, ))
    }

    @PostMapping
    fun saveBand(@RequestBody band: BandDto): Response {
        val resultBand = bandService.saveBand(band.toDomain())
        return Response.ok().entity(resultBand).build()
    }

    @DeleteMapping("/{id}")
    fun deleteBand(
        @PathVariable("id")
        id: Int
    ): Response {
        val cnt = bandService.deleteById(id)
        if (cnt == 0) {
            throw BandOperationException.NotFoundBandException("not found band with id = $id", Status.NOT_FOUND.statusCode)
        }
        return Response.ok().build()
    }

    @GetMapping("/{band-id}")
    fun getById(
        @PathVariable("band-id")
        id: Int
    ): Response {
        val band = bandService.findById(id)
        val response = band?.let {
            Response.ok().entity(it).build()
        } ?: throw BandOperationException.NotFoundBandException("not found band with id = $id", Status.NOT_FOUND.statusCode)
        return response
    }

    @GetMapping("/name/{name-substr}")
    fun getByNameSubstring(
        @PathVariable("name-substr")
        nameSubstring: String
    ): Response {
        if (nameSubstring.isEmpty())
            throw BandOperationException.BandOperationParamsException("substr can not be empty", Status.BAD_REQUEST.statusCode)
        val bands = bandService.findByNameSubstring(nameSubstring)
        return Response.ok().entity(bands).build()
    }

    @GetMapping("/genre/count")
    fun getCountWhereGenreLower(
        @RequestParam("genre")
        genre: MusicGenre
    ): Response {
        val bands = bandService.countGenres(genre).size
        return Response.ok().entity(bands).build()
    }

    @PatchMapping("/{band-id}")
    fun update(
        @RequestParam("band-id")
        bandId: Int,
        @RequestBody
        band: BandDto,
    ): Response {
        band.id = bandId
        val updatedBand = bandService.update(bandId, band.toDomain())
            ?: throw BandOperationException.NotFoundBandException("not found band with id = $bandId", Status.NOT_FOUND.statusCode)
        return Response.ok().entity(
            updatedBand.toDto()
        ).build()
    }

    @DeleteMapping
    fun deleteByStudioId(
        @RequestParam("studioId")
        studioId: Int
    ): Response {
        val cnt = bandService.deleteByStudioId(studioId)
        if (cnt == 0) {
            throw BandOperationException.NotFoundBandException("not found band with studioId $studioId", Status.NOT_FOUND.statusCode)
        }
        return Response.ok().build()
    }

    @PostMapping("/participant/{id}")
    fun removeParticipant(
        @PathVariable("id")
        id: Int
    ): Response {
        val status = bandService.removeParticipant(id)
        return when (status) {
            BandService.RemoveStatus.OK -> Response.ok().build()
            BandService.RemoveStatus.NOT_FOUND -> Response.status(404).build()
            BandService.RemoveStatus.NUMBER_OF_PARTICIPANTS_IS_ZERO -> Response.status(400).build()
        }
    }
}