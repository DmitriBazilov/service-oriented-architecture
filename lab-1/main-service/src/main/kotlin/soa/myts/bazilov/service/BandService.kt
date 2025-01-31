package soa.myts.bazilov.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import soa.myts.bazilov.model.domain.Band
import soa.myts.bazilov.model.domain.LimitOffset
import soa.myts.bazilov.model.domain.MusicGenre
import soa.myts.bazilov.model.domain.filter.Field
import soa.myts.bazilov.model.domain.filter.Filter
import soa.myts.bazilov.model.domain.filter.Operator
import soa.myts.bazilov.model.domain.filter.SortClause
import soa.myts.bazilov.model.domain.filter.SortType
import soa.myts.bazilov.model.domain.filter.filter
import soa.myts.bazilov.model.domain.filter.sortClause
import soa.myts.bazilov.model.domain.toDto
import soa.myts.bazilov.model.dto.BandDto
import soa.myts.bazilov.model.dto.BandListDto
import soa.myts.bazilov.model.dto.toDomain
import soa.myts.bazilov.repository.BandRepository
import kotlin.time.Duration.Companion.seconds

@ApplicationScoped
class BandService {

    @Inject
    private lateinit var bandRepository: BandRepository

    fun getBands(
        filters: List<String> = emptyList(),
        sortClause: String? = null,
        page: Int = 1,
        size: Int = 10,
    ): BandListDto {
        val domainFilters = filters.map { it.filter() }
        val domainSortClause = sortClause?.sortClause()
        println("filters: $domainFilters")
        return BandListDto(
            bandRepository.getBands(
                domainFilters,
                domainSortClause ?: SortClause(Field.Id, SortType.ASC),
                LimitOffset(size, (page - 1) * size)
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

    fun findByNameSubstring(nameSubstr: String): BandListDto {
        val substr = "%${nameSubstr.replace("%", "\\%")}%"
        val filter = Filter(Field.Name, Operator.LIKE, substr)
        return BandListDto(
            bandRepository.getBands(
                listOf(filter),
                SortClause(Field.Id, SortType.ASC),
                null,
            ). map { it.toDto() }
        )
    }

    fun countGenres(genre: MusicGenre): BandListDto {
        val filter = Filter(Field.Genre, Operator.LT, genre)
        return BandListDto(
            bandRepository.getBands(
                listOf(filter),
                SortClause(Field.Id, SortType.ASC),
                null,
            ). map { it.toDto() }
        )
    }

    fun update(id: Int, band: Band): Band? {
        return bandRepository.update(id, band)
    }

    fun deleteByStudioId(studioId: Int): Int {
        return bandRepository.deleteByStudioId(studioId)
    }

    fun removeParticipant(id: Int): RemoveStatus {
        val band = findById(id) ?: return RemoveStatus.NOT_FOUND
        band.numberOfParticipants = --band.numberOfParticipants
        if (band.numberOfParticipants < 0) return RemoveStatus.NUMBER_OF_PARTICIPANTS_IS_ZERO
        val domain = band.toDomain()
        bandRepository.update(id, domain)
        return RemoveStatus.OK
    }

    enum class RemoveStatus {
        OK,
        NOT_FOUND,
        NUMBER_OF_PARTICIPANTS_IS_ZERO
    }
}