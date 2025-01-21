package soa.myts.bazilov.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response
import soa.myts.bazilov.model.domain.BestGroup
import soa.myts.bazilov.model.dto.WebException
import soa.myts.bazilov.repository.BestGroupRepository
import soa.myts.bazilov.model.dto.Response as ResponseDto

@ApplicationScoped
class BestGroupService {

    @Inject
    private lateinit var bestGroupRepository: BestGroupRepository

    @Inject
    private lateinit var bandService: BandService


    fun saveBestGroup(bestGroup: BestGroup): BestGroup? {
        bandService.findById(bestGroup.bandId)
            ?: throw WebException(
                ResponseDto("Group with ${bestGroup.bandId} does not exists"),
                Response.Status.NOT_FOUND,
            )
        return bestGroupRepository.saveBestGroup(bestGroup)
    }

}