package soa.myts.bazilov.controller

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import soa.myts.bazilov.model.domain.toDto
import soa.myts.bazilov.model.dto.BestGroupDto
import soa.myts.bazilov.model.dto.toDomain
import soa.myts.bazilov.service.BestGroupService

@Path("/best-group")
class BestGroupController {

    @Inject
    private lateinit var bestGroupService: BestGroupService

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    fun saveBestGroup(bestGroup: BestGroupDto): Response {
        return bestGroupService.saveBestGroup(bestGroup.toDomain())?.let {
            Response.ok().entity(it.toDto()).build()
        } ?: Response.status(400).build()
    }
}