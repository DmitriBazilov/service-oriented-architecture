package com.soa.products.controller

import com.soa.products.domain.BestGroupDto
import com.soa.products.domain.toDomain
import com.soa.products.domain.toDto
import com.soa.products.ejb.service.BestGroupService
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(
    path = ["/best-group"],
    produces = ["application/xml"],
    consumes = ["application/xml"]
)
class BestGroupController(
    private val bestGroupService: BestGroupService,
) {

    @PostMapping
    fun saveBestGroup(bestGroup: BestGroupDto): Response {
        return bestGroupService.saveBestGroup(bestGroup.toDomain()).let {
            Response.ok().entity(it.toDto()).build()
        }
    }
}