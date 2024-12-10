package soa.myts.bazilov.model.domain.filter.field

enum class Type {
    INT,
    LONG,
    DOUBLE,
    STRING,
    LOCAL_DATE,
}

sealed interface Field {
    val domainName: String
    val dbName: String
    val valueType: Type

    data object Id : Field {
        override val dbName: String = "id"
        override val domainName: String = "id"
        override val valueType: Type = Type.INT
    }

    data object Name : Field {
        override val dbName: String = "name"
        override val domainName: String = "name"
        override val valueType: Type = Type.STRING
    }

    data object CreationDate : Field {
        override val dbName: String = "creation_date"
        override val domainName: String = "creationDate"
        override val valueType: Type = Type.LOCAL_DATE
    }

    data object NumberOfParticipants : Field {
        override val dbName: String = "number_of_participants"
        override val domainName: String = "numberOfParticipants"
        override val valueType: Type = Type.LONG
    }

    data object AlbumsCount : Field {
        override val dbName: String = "albumsCount"
        override val domainName: String = "albumsCount"
        override val valueType: Type = Type.LONG
    }

    data object Description : Field {
        override val dbName: String = "description"
        override val domainName: String = "description"
        override val valueType: Type = Type.STRING
    }

    data object X : Field {
        override val dbName: String = "x"
        override val domainName: String = "Coordinates.x"
        override val valueType: Type = Type.DOUBLE
    }

    data object Y : Field {
        override val dbName: String = "y"
        override val domainName: String = "Coordinates.y"
        override val valueType: Type = Type.LONG
    }

    data object Genre : Field {
        override val dbName: String = "genre"
        override val domainName: String = "genre"
        override val valueType: Type = Type.STRING
    }

    data object StudioName : Field {
        override val dbName: String = "music_studio.name"
        override val domainName: String = "Studio.name"
        override val valueType: Type = Type.STRING
    }

    data object StudioAddress : Field {
        override val dbName: String = "studio.address"
        override val domainName: String = "Studio.address"
        override val valueType: Type = Type.STRING
    }

    companion object {
        val instances by lazy {
            Field::class.sealedSubclasses.mapNotNullTo(HashSet()) { it.objectInstance }
        }

        val byDomainName by lazy {
            instances.associateBy { it.domainName }
        }
    }
}

val String.field: Field?
    get() = Field.byDomainName[this]
