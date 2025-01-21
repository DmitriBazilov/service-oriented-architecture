package com.soa.products.ejb.service

import com.soa.products.ejb.dao.BestGroupRepository
import com.soa.products.ejb.domain.BestGroup
import com.soa.products.ejb.exception.BandOperationException
import com.soa.products.ejb.exception.BestGroupOperationException
import jakarta.ejb.Stateless
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import org.jboss.ejb3.annotation.Pool

@Stateless
@Pool("band-pool")
class BestGroupServiceImpl: BestGroupService {

    @Inject
    private lateinit var bestGroupRepository: BestGroupRepository

    @Inject
    private lateinit var bandService: BandService


    override fun saveBestGroup(bestGroup: BestGroup): BestGroup {
        bandService.findById(bestGroup.bandId)
            ?: throw BandOperationException.NotFoundBandException(
                "Group with ${bestGroup.bandId} does not exists",
                Response.Status.NOT_FOUND.statusCode,
            )
        return bestGroupRepository.saveBestGroup(bestGroup)
    }

}
