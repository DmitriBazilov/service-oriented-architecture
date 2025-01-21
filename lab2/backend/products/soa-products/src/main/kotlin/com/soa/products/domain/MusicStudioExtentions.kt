package com.soa.products.domain

import com.soa.products.ejb.domain.MusicStudio

fun MusicStudio.toDto() = MusicStudioDto(
    id = id,
    name = name,
    address = address,
)