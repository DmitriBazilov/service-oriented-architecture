package soa.myts.bazilov.model.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import soa.myts.bazilov.model.dto.BandDto
import soa.myts.bazilov.model.dto.CoordinatesDto
import java.time.LocalDate

@Entity
@Table(name = "music_bands")
data class Band(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String,

    @Embedded
    val coordinates: Coordinates,

    @Column(name = "creation_date")
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    val creationDate: LocalDate = LocalDate.now(),

    @Column(name = "number_of_participants")
    val numberOfParticipants: Long,

    @Column(name = "albums_count")
    val albumsCount: Long,

    @Column(name = "description")
    val description: String,

    @Enumerated(EnumType.STRING)
    val genre: MusicGenre,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "music_studio_id")
    var studio: MusicStudio?,
) {
    @Embeddable
    data class Coordinates(
        val x: Double,
        val y: Long?
    )
}

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
