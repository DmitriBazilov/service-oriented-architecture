package com.soa.products.controller

import com.soa.products.domain.BandDto
import com.soa.products.domain.BandListDto
import com.soa.products.domain.toDomain
import com.soa.products.domain.toDto
import com.soa.products.ejb.domain.MusicGenre
import com.soa.products.ejb.exception.BandOperationException
import com.soa.products.ejb.service.BandService
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
import kotlin.math.ceil

@RestController
@RequestMapping(
    path = ["/bands"],
    produces = ["application/xml"],
    consumes = ["application/xml"]
)
class BandController(
    private val bandService: BandService,
) {

    @GetMapping
    fun getBands(
        @RequestParam(name = "filter", required = false)
        filterList: List<String>?,
        @RequestParam(name = "sort", required = false)
        sortClause: String?,
        @RequestParam(name = "page", required = false)
        page: Int?,
        @RequestParam(name = "size", required = false)
        size: Int?,
    ): ResponseEntity<*> {
        page?.let {
            if (it < 1) throw BandOperationException.BandOperationParamsException("negative page number", Status.BAD_REQUEST.statusCode)
        }
        size?.let {
            if (it < 1) throw BandOperationException.BandOperationParamsException("negative page size", Status.BAD_REQUEST.statusCode)
        }
        val banditos = bandService.getBands(filterList ?: emptyList(), sortClause, page ?: 1, size ?: 10).map { it.toDto() }
        return ResponseEntity.ok(BandListDto(banditos, size, page, ceil(banditos.size.toDouble() / (size ?: 10)).toInt()))
    }

    @PostMapping
    fun saveBand(@RequestBody band: BandDto): ResponseEntity<*> {
        val resultBand = bandService.saveBand(band.toDomain())
        return ResponseEntity.ok(resultBand.toDto())
    }

    @DeleteMapping("/{id}")
    fun deleteBand(
        @PathVariable("id")
        id: Int
    ): ResponseEntity<Unit> {
        val cnt = bandService.deleteById(id)
        if (cnt == 0) {
            throw BandOperationException.NotFoundBandException("not found band with id = $id", Status.NOT_FOUND.statusCode)
        }
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{band-id}")
    fun getById(
        @PathVariable("band-id")
        id: Int
    ): ResponseEntity<*> {
        val band = bandService.findById(id)
        val response = band?.let {
            ResponseEntity.ok(it.toDto())
        } ?: throw BandOperationException.NotFoundBandException("not found band with id = $id", Status.NOT_FOUND.statusCode)
        return response
    }

    @GetMapping("/name/{name-substr}")
    fun getByNameSubstring(
        @PathVariable("name-substr")
        nameSubstring: String
    ): ResponseEntity<*> {
        if (nameSubstring.isEmpty())
            throw BandOperationException.BandOperationParamsException("substr can not be empty", Status.BAD_REQUEST.statusCode)
        val bands = bandService.findByNameSubstring(nameSubstring).map { it.toDto() }
        return ResponseEntity.ok(BandListDto(bands))
    }

    @GetMapping("/genre/count")
    fun getCountWhereGenreLower(
        @RequestParam("genre")
        genre: MusicGenre
    ): ResponseEntity<*> {
        val bands = bandService.countGenres(genre).size
        return ResponseEntity.ok(bands)
    }

    @PatchMapping("/{band-id}")
    fun update(
        @PathVariable("band-id")
        bandId: Int,
        @RequestBody
        band: BandDto,
    ): ResponseEntity<*> {
        band.id = bandId
        val updatedBand = bandService.update(bandId, band.toDomain())
            ?: throw BandOperationException.NotFoundBandException("not found band with id = $bandId", Status.NOT_FOUND.statusCode)
        return ResponseEntity.ok(updatedBand.toDto())
    }

    @DeleteMapping
    fun deleteByStudioId(
        @RequestParam("studioId")
        studioId: Int
    ): ResponseEntity<Unit> {
        val cnt = bandService.deleteByStudioId(studioId)
        if (cnt == 0) {
            throw BandOperationException.NotFoundBandException("not found band with studioId $studioId", Status.NOT_FOUND.statusCode)
        }
        return ResponseEntity.ok().build()
    }

    @PostMapping("/participant/{id}")
    fun removeParticipant(
        @PathVariable("id")
        id: Int
    ): ResponseEntity<Unit> {
        val status = bandService.removeParticipant(id)
        return when (status) {
            BandService.RemoveStatus.OK -> ResponseEntity.ok().build()
            BandService.RemoveStatus.NOT_FOUND -> ResponseEntity.status(404).build()
            BandService.RemoveStatus.NUMBER_OF_PARTICIPANTS_IS_ZERO -> ResponseEntity.status(400).build()
        }
    }
}