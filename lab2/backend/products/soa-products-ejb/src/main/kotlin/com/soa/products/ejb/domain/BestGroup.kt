package com.soa.products.ejb.domain


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "best_groups")
class BestGroup (

    @Id
    @Column(name = "band_id")
    var bandId: Int,

    @Id
    @Enumerated(EnumType.STRING)
    var genre: MusicGenre,
)
