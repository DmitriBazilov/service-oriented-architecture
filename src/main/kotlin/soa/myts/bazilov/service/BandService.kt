package soa.myts.bazilov.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import soa.myts.bazilov.model.domain.Band
import soa.myts.bazilov.model.domain.toDto
import soa.myts.bazilov.model.dto.BandDto
import soa.myts.bazilov.model.dto.BandListDto
import soa.myts.bazilov.repository.BandRepository

@ApplicationScoped
class BandService {

    @Inject
    private lateinit var bandRepository: BandRepository

    fun getBands(): BandListDto {
        return BandListDto(bandRepository.getBands().map { it.toDto() })
    }

    fun saveBand(band: Band): BandDto {
        bandRepository.saveBand(band)
        return band.toDto()
    }
}