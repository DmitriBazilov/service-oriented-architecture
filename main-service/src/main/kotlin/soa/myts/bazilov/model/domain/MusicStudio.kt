package soa.myts.bazilov.model.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import soa.myts.bazilov.model.dto.MusicStudioDto

@Entity
@Table(name = "music_studio")
class MusicStudio(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    var name: String,
    var address: String?
)

fun MusicStudio.toDto() = MusicStudioDto(
    id = id,
    name = name,
    address = address,
)
