package com.soa.products.domain

import com.soa.products.ejb.domain.BestGroup


fun BestGroup.toDto() = BestGroupDto(
    bandId,
    genre
)