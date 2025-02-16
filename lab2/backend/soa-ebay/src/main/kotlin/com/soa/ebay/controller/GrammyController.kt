package com.soa.ebay.controller

import com.soa.ebay.client.BandClient
import com.soa.ebay.model.BestGroupDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    path = ["/grammy"],
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

    @PostMapping("/{band-id}/reward/{genre}")
    fun rewardBand(
        @PathVariable("band-id")
        id: Int,
        @PathVariable("genre")
        genre: String,
    ): ResponseEntity<*> {
        return bandClient.rewardBand(BestGroupDto(id, genre))
    }
}

