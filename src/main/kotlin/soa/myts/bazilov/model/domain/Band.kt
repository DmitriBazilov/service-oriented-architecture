package soa.myts.bazilov.model.domain

import jakarta.persistence.*

@Entity
@Table(name = "music_bands")
data class Band(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,
)
