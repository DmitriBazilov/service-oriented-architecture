package soa.myts.bazilov.service

import jakarta.enterprise.context.ApplicationScoped
import soa.myts.bazilov.model.dto.BandDto
import soa.myts.bazilov.model.dto.BandListDto

@ApplicationScoped
class BandService {

    fun getBands(): BandListDto {

        return BandListDto(
            listOf(
                BandDto(
                    id = 228,
                )
            )
        )
    }
}