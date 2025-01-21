package com.soa.products.ejb.service

import com.soa.products.ejb.domain.BestGroup
import jakarta.ejb.Remote

@Remote
interface BestGroupService {
    fun saveBestGroup(bestGroup: BestGroup): BestGroup
}