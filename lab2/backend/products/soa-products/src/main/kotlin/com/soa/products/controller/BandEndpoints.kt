package com.soa.products.controller

import com.soa.products.controller.BandEndpoints.Companion.NAMESPACE_URI
import com.soa.products.domain.toDto
import com.soa.products.domain.toSoap
import com.soa.products.ejb.domain.Band
import com.soa.products.ejb.domain.BestGroup
import com.soa.products.ejb.domain.MusicGenre
import com.soa.products.ejb.domain.MusicStudio
import com.soa.products.ejb.exception.BandOperationException
import com.soa.products.ejb.service.BandService
import com.soa.products.ejb.service.BestGroupService
import com.soa.products.generated.*
import jakarta.ws.rs.core.Response.Status
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload

@Endpoint
class BandEndpoints(
    private val bandController: BandController,
    private val bandService: BandService,
    private val bestGroupService: BestGroupService,
) {


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    fun getCountry(@RequestPayload request: GetCountryRequest): GetCountryResponse {
        val response = GetCountryResponse().apply {
            country = Country().apply {
                name = "amerika"
                capital = "london"
                population = 228
            }
        }
        // Fill in the response with your business logic
        // Populate response object here
        return response
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RemoveParticipantRequest")
    @ResponsePayload
    fun removeParticipants(
        @RequestPayload request: RemoveParticipantRequest
    ): RemoveParticipantResponse {
        val status = bandService.removeParticipant(request.bandId)
        val response = when (status) {
            BandService.RemoveStatus.OK -> 200
            BandService.RemoveStatus.NOT_FOUND -> 404
            BandService.RemoveStatus.NUMBER_OF_PARTICIPANTS_IS_ZERO -> 400
        }
        return RemoveParticipantResponse().apply {
            this.status = response
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RewardBandRequest")
    @ResponsePayload
    fun rewardBand(
        @RequestPayload request: RewardBandRequest
    ): RewardBandResponse {
        val bestGroup = bestGroupService.saveBestGroup(
            BestGroup(
                request.bestGroup.bandId,
                MusicGenre.valueOf(request.bestGroup.musicGenre.value())
            )
        )
        return RewardBandResponse().apply {
            this.bestGroup = com.soa.products.generated.BestGroup().apply {
                bandId = bestGroup.bandId
                musicGenre = com.soa.products.generated.MusicGenre.valueOf(bestGroup.genre.name)
            }
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetBandsRequest")
    @ResponsePayload
    fun getBands(
        @RequestPayload request: GetBandsRequest
    ): GetBandsResponse {
        val filters = request.filter
        val sortClause = request.sortClause
        val page = request.page
        val size = request.size
        page?.let {
            if (it.longValueExact() < 1) throw BandOperationException.BandOperationParamsException("negative page number", Status.BAD_REQUEST.statusCode)
        }
        size?.let {
            if (it.longValueExact() < 1) throw BandOperationException.BandOperationParamsException("negative page size", Status.BAD_REQUEST.statusCode)
        }
        val banditos = bandService.getBands(
            filters ?: emptyList(), sortClause, page?.intValueExact() ?: 1, size?.intValueExact() ?: 10
        ).map { it.toDto() }
        return GetBandsResponse().apply {
            val b = bands
            b.addAll(banditos.map { it.toSoap() })
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CreateBandRequest")
    @ResponsePayload
    fun createBand(
        @RequestPayload request: CreateBandRequest
    ): CreateBandResponse {
        val domainBand = request.band.toDomain()
        val savedBand = bandService.saveBand(domainBand)
        return CreateBandResponse().apply {
            band = savedBand.toDto().toSoap()
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteBandRequest")
    fun deleteBand(
        @RequestPayload request: DeleteBandRequest
    ) {
        val cnt = bandService.deleteById(request.bandId)
        if (cnt == 0) {
            throw BandOperationException.NotFoundBandException("not found band with id = ${request.bandId}", Status.NOT_FOUND.statusCode)
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetBandByIdRequest")
    @ResponsePayload
    fun getBandById(
        @RequestPayload request: GetBandByIdRequest,
    ): GetBandByIdResponse {
        val band = bandService.findById(request.bandId)
        return band?.let {
            return GetBandByIdResponse().apply {
                this.band = band.toDto().toSoap()
            }
        } ?: throw BandOperationException.NotFoundBandException("not found band with id ${request.bandId}", Status.NOT_FOUND.statusCode)
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetBandsByNameSubstrRequest")
    @ResponsePayload
    fun getBandsByNameSubstr(
        @RequestPayload request: GetBandsByNameSubstrRequest,
    ): GetBandsByNameSubstrResponse {
        if (request.nameSubstr.isEmpty()) {
            throw BandOperationException.BandOperationParamsException("name substr should be not empty", Status.BAD_REQUEST.statusCode)
        }
        val bands = bandService.findByNameSubstring(request.nameSubstr)
        return GetBandsByNameSubstrResponse().apply {
            this.bands.addAll(bands.map { it.toDto().toSoap() })
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetCountWhereGenreLowerRequest")
    @ResponsePayload
    fun getCountWhereGenreLower(
        @RequestPayload request: GetCountWhereGenreLowerRequest,
    ): GetCountWhereGenreLowerResponse {
        val bands = bandService.countGenres(MusicGenre.valueOf(request.musicGenre.value()))
        return GetCountWhereGenreLowerResponse().apply {
            this.count = bands.size
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateBandRequest")
    @ResponsePayload
    fun updateBand(
        @RequestPayload request: UpdateBandRequest
    ): UpdateBandResponse {
        request.band.id = request.bandId
        val updatedBand = bandService.update(request.bandId, request.band.toDomain())
            ?: throw BandOperationException.NotFoundBandException("not found band with id = ${request.bandId}", Status.NOT_FOUND.statusCode)
        return UpdateBandResponse().apply {
            band = updatedBand.toDto().toSoap()
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteByStudioIdRequest")
    fun deleteBandByStudioId(
        @RequestPayload request: DeleteByStudioIdRequest
    ) {
        val cnt = bandService.deleteByStudioId(request.studioId)
        if (cnt == 0) {
            throw BandOperationException.NotFoundBandException("not found band with studioId ${request.studioId}", Status.NOT_FOUND.statusCode)
        }
    }

    companion object {
        private const val NAMESPACE_URI = "http://example.com/schema"
    }
}

private fun BandDto.toDomain() = Band(
    id = id ?: 0,
    name = name!!,
    coordinates = coordinates!!.toDomain(),
    numberOfParticipants = numberOfParticipants,
    albumsCount = albumsCount,
    description = description,
    genre = MusicGenre.valueOf(genre.value()),
    studio = studio?.toDomain(),
)

private fun CoordinatesDto.toDomain() = Band.Coordinates(
    x = x,
    y = y!!,
)

private fun MusicStudioDto.toDomain() = MusicStudio(
    id = id ?: 0,
    name = name,
    address = address,
)