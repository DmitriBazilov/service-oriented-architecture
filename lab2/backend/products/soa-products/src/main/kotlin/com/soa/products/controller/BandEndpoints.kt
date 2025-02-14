package com.soa.products.controller

import com.soa.products.ejb.service.BandService
import com.soa.products.generated.Country
import com.soa.products.generated.GetCountryRequest
import com.soa.products.generated.GetCountryResponse
import com.soa.products.generated.RemoveParticipantRequest
import com.soa.products.generated.RemoveParticipantResponse
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload

@Endpoint
class BandEndpoints(
    private val bandService: BandService,
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

    companion object {
        private const val NAMESPACE_URI = "http://example.com/schema"
    }
}