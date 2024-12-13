package soa.myts.bazilov.model.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import soa.myts.bazilov.model.dto.BandDto
import soa.myts.bazilov.model.dto.CoordinatesDto
import java.time.LocalDate

@Entity
@Table(name = "music_bands")
class Band(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    var name: String,

    @Embedded
    var coordinates: Coordinates,

    @Column(name = "creation_date")
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    var creationDate: LocalDate = LocalDate.now(),

    @Column(name = "number_of_participants")
    var numberOfParticipants: Long,

    @Column(name = "albums_count")
    var albumsCount: Long,

    @Column(name = "description")
    var description: String,

    @Enumerated(EnumType.STRING)
    var genre: MusicGenre,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "music_studio_id")
    var studio: MusicStudio?,
) {
    @Embeddable
    data class Coordinates(
        var x: Double,
        var y: Long?
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
