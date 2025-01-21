package com.soa.products.domain

import com.soa.products.ejb.domain.Band


fun Band.toDto() = BandDto(
    id = id,
    name = name,
    coordinates = coordinates.toDto(),
    numberOfParticipants = numberOfParticipants,
    albumsCount = albumsCount,
    description = description,
    genre = genre,
    studio = studio?.toDto(),
    creationDate = creationDate,
)

fun Band.Coordinates.toDto() = CoordinatesDto(
    x = x,
    y = y
)
