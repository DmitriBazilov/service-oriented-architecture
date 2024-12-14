package soa.myts.bazilov.model.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import soa.myts.bazilov.model.domain.filter.Field
import soa.myts.bazilov.model.dto.BestGroupDto

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

fun BestGroup.toDto() = BestGroupDto(
    bandId,
    genre
)