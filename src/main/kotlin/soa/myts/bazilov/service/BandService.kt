package soa.myts.bazilov.service

import jakarta.enterprise.context.ApplicationScoped
import soa.myts.bazilov.model.Band

@ApplicationScoped
class BandService {

    fun getBands(): List<Band> {

        return listOf(
            Band(
                228,
            )
        )
    }
}