package com.soa.products.ejb.service

import com.soa.products.ejb.domain.Band
import com.soa.products.ejb.domain.MusicGenre
import jakarta.ejb.Remote

@Remote
interface BandService {
    fun getBands(
        filters: List<String> = emptyList(),
        sortClause: String? = null,
        page: Int = 1,
        size: Int = 10,
    ): List<Band>
    fun saveBand(band: Band): Band
    fun deleteById(id: Int): Int
    fun findById(id: Int): Band?
    fun findByNameSubstring(nameSubstr: String): List<Band>
    fun countGenres(genre: MusicGenre): List<Band>
    fun update(id: Int, band: Band): Band?
    fun deleteByStudioId(studioId: Int): Int
    fun removeParticipant(id: Int): RemoveStatus

    enum class RemoveStatus {
        OK,
        NOT_FOUND,
        NUMBER_OF_PARTICIPANTS_IS_ZERO
    }
}