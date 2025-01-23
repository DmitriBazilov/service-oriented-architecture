package com.soa.products.ejb.service

import com.soa.products.ejb.dao.BandRepository
import com.soa.products.ejb.domain.Band
import com.soa.products.ejb.domain.LimitOffset
import com.soa.products.ejb.domain.MusicGenre
import com.soa.products.ejb.domain.filter.Field
import com.soa.products.ejb.domain.filter.Filter
import com.soa.products.ejb.domain.filter.Operator
import com.soa.products.ejb.domain.filter.SortClause
import com.soa.products.ejb.domain.filter.SortType
import com.soa.products.ejb.domain.filter.filter
import com.soa.products.ejb.domain.filter.sortClause
import jakarta.ejb.Stateless
import jakarta.inject.Inject
import org.jboss.ejb3.annotation.Pool


@Stateless
@Pool("band-pool")
open class BandServiceImpl: BandService {

    @Inject
    private lateinit var bandRepository: BandRepository

    override fun getBands(
        filters: List<String>,
        sortClause: String?,
        page: Int,
        size: Int,
    ): List<Band> {
        val domainFilters = filters.map { it.filter() }
        val domainSortClause = sortClause?.sortClause()
        println("filters: $domainFilters")
        return bandRepository.getBands(
            domainFilters,
            domainSortClause ?: SortClause(Field.Id, SortType.ASC),
            LimitOffset(size, (page - 1) * size)
        )
    }

    override fun saveBand(band: Band): Band {
        bandRepository.saveBand(band)
        return band
    }

    override fun deleteById(id: Int): Int {
        return bandRepository.deleteById(id)
    }

    override fun findById(id: Int): Band? {
        return bandRepository.findById(id)
    }

    override fun findByNameSubstring(nameSubstr: String): List<Band> {
        val substr = "%${nameSubstr.replace("%", "\\%")}%"
        val filter = Filter(Field.Name, Operator.LIKE, substr)
        return bandRepository.getBands(
            listOf(filter),
            SortClause(Field.Id, SortType.ASC),
            null,
        )
    }

    override fun countGenres(genre: MusicGenre): List<Band> {
        val filter = Filter(Field.Genre, Operator.LT, genre)
        return bandRepository.getBands(
            listOf(filter),
            SortClause(Field.Id, SortType.ASC),
            null,
        )
    }

    override fun update(id: Int, band: Band): Band? {
        return bandRepository.update(id, band)
    }

    override fun deleteByStudioId(studioId: Int): Int {
        return bandRepository.deleteByStudioId(studioId)
    }

    override fun removeParticipant(id: Int): BandService.RemoveStatus {
        val band = findById(id) ?: return BandService.RemoveStatus.NOT_FOUND
        band.numberOfParticipants = --band.numberOfParticipants
        if (band.numberOfParticipants < 0) return BandService.RemoveStatus.NUMBER_OF_PARTICIPANTS_IS_ZERO
        val domain = band
        bandRepository.update(id, domain)
        return BandService.RemoveStatus.OK
    }

}
