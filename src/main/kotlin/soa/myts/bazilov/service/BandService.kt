package soa.myts.bazilov.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import soa.myts.bazilov.model.domain.Band
import soa.myts.bazilov.model.domain.filter.Field
import soa.myts.bazilov.model.domain.filter.SortClause
import soa.myts.bazilov.model.domain.filter.SortType
import soa.myts.bazilov.model.domain.filter.filter
import soa.myts.bazilov.model.domain.filter.sortClause
import soa.myts.bazilov.model.domain.toDto
import soa.myts.bazilov.model.dto.BandDto
import soa.myts.bazilov.model.dto.BandListDto
import soa.myts.bazilov.repository.BandRepository

@ApplicationScoped
class BandService {

    @Inject
    private lateinit var bandRepository: BandRepository

    fun getBands(
        filters: List<String> = emptyList(),
        sortClause: String?,
    ): BandListDto {
        val domainFilters = filters.map { it.filter() }
        val domainSortClause = sortClause?.sortClause()
        println("filters: $domainFilters")
        return BandListDto(
            bandRepository.getBands(
                domainFilters,
                domainSortClause ?: SortClause(Field.Id, SortType.ASC)
            ).map {
                it.toDto()
            }
        )
    }

    fun saveBand(band: Band): BandDto {
        bandRepository.saveBand(band)
        return band.toDto()
    }

    fun deleteById(id: Int): Int {
        return bandRepository.deleteById(id)
    }

    fun findById(id: Int): BandDto? {
        return bandRepository.findById(id)?.toDto()
    }
}