package soa.myts.bazilov.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import soa.myts.bazilov.model.domain.BestGroup
import soa.myts.bazilov.repository.BestGroupRepository

@ApplicationScoped
class BestGroupService {

    @Inject
    private lateinit var bestGroupRepository: BestGroupRepository

    fun saveBestGroup(bestGroup: BestGroup): BestGroup? {
        return bestGroupRepository.saveBestGroup(bestGroup)
    }

}