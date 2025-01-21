package com.soa.products.ejb.domain.filter

enum class Type {
    INT,
    LONG,
    DOUBLE,
    STRING,
    LOCAL_DATE,
    MUSIC_GENRE,
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
        override val dbName: String = "creationDate"
        override val domainName: String = "creationDate"
        override val valueType: Type = Type.LOCAL_DATE
    }

    data object NumberOfParticipants : Field {
        override val dbName: String = "numberOfParticipants"
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
        override val dbName: String = "coordinates.x"
        override val domainName: String = "coordinate.x"
        override val valueType: Type = Type.DOUBLE
    }

    data object Y : Field {
        override val dbName: String = "coordinates.y"
        override val domainName: String = "coordinate.y"
        override val valueType: Type = Type.LONG
    }

    data object Genre : Field {
        override val dbName: String = "genre"
        override val domainName: String = "genre"
        override val valueType: Type = Type.MUSIC_GENRE
    }

    data object StudioName : Field {
        override val dbName: String = "name"
        override val domainName: String = "studio.name"
        override val valueType: Type = Type.STRING
    }

    data object StudioAddress : Field {
        override val dbName: String = "address"
        override val domainName: String = "studio.address"
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