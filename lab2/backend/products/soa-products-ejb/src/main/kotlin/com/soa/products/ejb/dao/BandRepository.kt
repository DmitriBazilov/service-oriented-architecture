package com.soa.products.ejb.dao

import com.soa.products.ejb.configuration.DatabaseSessionManager
import com.soa.products.ejb.domain.Band
import com.soa.products.ejb.domain.LimitOffset
import com.soa.products.ejb.domain.MusicGenre
import com.soa.products.ejb.domain.MusicStudio
import com.soa.products.ejb.domain.filter.Field
import com.soa.products.ejb.domain.filter.Filter
import com.soa.products.ejb.domain.filter.Operator
import com.soa.products.ejb.domain.filter.SortClause
import com.soa.products.ejb.domain.filter.SortType
import com.soa.products.ejb.domain.filter.Type
import com.soa.products.ejb.exception.FilterException
import com.soa.products.ejb.exception.UnknownError
import jakarta.ejb.Stateless
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Order
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.ws.rs.core.Response
import org.hibernate.Session
import org.jboss.ejb3.annotation.Pool
import java.time.LocalDate

@Stateless
@Pool("band-pool")
open class BandRepository {

    @Inject
    private lateinit var  databaseSessionManager: DatabaseSessionManager

    open fun saveBand(band: Band) {
        val session: Session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()

            session.persist(band)

            session.transaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw UnknownError("internal server error", Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        } finally {
            databaseSessionManager.closeSession(session)
        }
    }

    open fun getBands(filters: List<Filter>, sortClause: SortClause, limitOffset: LimitOffset?): List<Band> {
        val session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val criteriaBuilder = session.criteriaBuilder
            var criteriaQuery = criteriaBuilder.createQuery(Band::class.java)
            val root = criteriaQuery.from(Band::class.java)
            val join = root.join<Band, MusicStudio>("studio")
            val orderName = sortClause.field.dbName.split(".").last()
            val order = when (sortClause.field) {
                Field.X, Field.Y -> sortClause.toOrder(
                    criteriaBuilder,
                    root.get<Any>(sortClause.field.dbName.split(".")[0]),
                    orderName,
                )
                Field.StudioName, Field.StudioAddress -> sortClause.toOrder(
                    criteriaBuilder,
                    join,
                    orderName
                )
                else -> sortClause.toOrder(
                    criteriaBuilder,
                    root,
                    orderName,
                )
            }
            val predicates = filters.map { filter ->
                val name = filter.field.dbName.split(".").last()
                val predicate = when (filter.field) {
                    is Field.X, Field.Y -> filter.toPredicate(
                        criteriaBuilder,
                        root.get<Any>(filter.field.dbName.split(".")[0]),
                        name,
                    )
                    is Field.StudioName, Field.StudioAddress -> filter.toPredicate(criteriaBuilder, join, name)
                    else -> filter.toPredicate(criteriaBuilder, root, name)
                }
                if (predicate == null) {
                    throw FilterException.UnsupportedOperatorForValueTypeException(
                        "unsupported operator ${filter.operator.domainOp} for type ${filter.field.valueType}",
                        Response.Status.BAD_REQUEST.statusCode
                    )
                }
                return@map predicate
            }
            val whereClause = criteriaBuilder.and(*predicates.toTypedArray())
            criteriaQuery.orderBy(order)
            criteriaQuery = criteriaQuery.where(whereClause)

            val query = session.createQuery(criteriaQuery)
            limitOffset?.let {
                query.firstResult = it.offset
                query.maxResults = it.limit
            }
            return query.resultList
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw UnknownError("internal server error", Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        } finally {
            if (session.isOpen) {
                databaseSessionManager.closeSession(session)
            }
        }
    }

    open fun deleteById(id: Int): Int {
        val session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val query = session.createQuery("delete Band where id = :id")
            query.setParameter("id", id)

            return query.executeUpdate()

        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw UnknownError("internal server error", Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        } finally {
            if (session.isOpen) {
                databaseSessionManager.closeSession(session)
            }
        }
    }

    open fun findById(id: Int): Band? {
        val session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val query = session.createQuery("from Band where id = :id")
            query.setParameter("id", id)

            return query.list().firstOrNull() as Band?

        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw UnknownError("internal server error", Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        } finally {
            if (session.isOpen) {
                databaseSessionManager.closeSession(session)
            }
        }
    }

    open fun update(id: Int, band: Band): Band? {
        val session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            session.get(Band::class.java, id) ?: return null
            val res = session.merge(band)
            session.transaction.commit()
            return res
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw UnknownError("internal server error", Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        } finally {
            if (session.isOpen) {
                databaseSessionManager.closeSession(session)
            }
        }
    }

    open fun deleteByStudioId(studioId: Int): Int {
        val session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val query = session.createQuery("delete Band where studio.id = :studioId")
            query.setParameter("studioId", studioId)
            return query.executeUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw UnknownError("internal server error", Response.Status.INTERNAL_SERVER_ERROR.statusCode)
        } finally {
            if (session.isOpen) {
                databaseSessionManager.closeSession(session)
            }
        }
    }
}

private fun Band.mergeUpdate(band: Band) {
    this.name = band.name
    this.genre = band.genre
    this.albumsCount = band.albumsCount
    this.numberOfParticipants = band.numberOfParticipants
    this.description = band.description
    this.coordinates.x = band.coordinates.x
    this.coordinates.y = band.coordinates.y
    band.studio?.let {
        this.studio?.name = it.name
        this.studio?.address = it.address
    }
}

private fun Filter.toPredicate(cb: CriteriaBuilder, root: Path<*>, name: String) = when (this.field.valueType) {
    Type.INT -> toPredicateInt(cb, root, name)
    Type.LONG -> toPredicateLong(cb, root, name)
    Type.DOUBLE -> toPredicateDouble(cb, root, name)
    Type.STRING -> toPredicateString(cb, root, name)
    Type.LOCAL_DATE -> toPredicateDate(cb, root, name)
    Type.MUSIC_GENRE -> toPredicateMusicGenre(cb, root, name)
}

private fun Filter.toPredicateInt(cb: CriteriaBuilder, root: Path<*>, name: String): Predicate? {
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<Int>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<Int>(name), this.value)
        Operator.LT -> cb.lt(root.get(name), this.value as Int)
        Operator.LTE -> cb.le(root.get(name), this.value as Int)
        Operator.GT -> cb.gt(root.get(name), this.value as Int)
        Operator.GTE -> cb.ge(root.get(name), this.value as Int)
        else -> null
    }
}

private fun Filter.toPredicateLong(cb: CriteriaBuilder, root: Path<*>, name: String): Predicate? {
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<Long>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<Long>(name), this.value)
        Operator.LT -> cb.lt(root.get(name), this.value as Long)
        Operator.LTE -> cb.le(root.get(name), this.value as Long)
        Operator.GT -> cb.gt(root.get(name), this.value as Long)
        Operator.GTE -> cb.ge(root.get(name), this.value as Long)
        else -> null
    }
}

private fun Filter.toPredicateString(cb: CriteriaBuilder, root: Path<*>, name: String): Predicate? {
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<String>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<String>(name), this.value)
        Operator.LIKE -> cb.like(root.get(name), this.value as String)
        Operator.LT -> cb.lessThan(root.get(name), this.value as String)
        Operator.LTE -> cb.lessThanOrEqualTo(root.get(name), this.value as String)
        Operator.GT -> cb.greaterThan(root.get(name), this.value as String)
        Operator.GTE -> cb.greaterThanOrEqualTo(root.get(name), this.value as String)
    }
}

private fun Filter.toPredicateMusicGenre(cb: CriteriaBuilder, root: Path<*>, name: String): Predicate? {
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<MusicGenre>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<MusicGenre>(name), this.value)
        Operator.LT -> cb.lessThan(root.get(name), this.value as MusicGenre)
        Operator.LTE -> cb.lessThanOrEqualTo(root.get(name), this.value as MusicGenre)
        Operator.GT -> cb.greaterThan(root.get(name), this.value as MusicGenre)
        Operator.GTE -> cb.greaterThanOrEqualTo(root.get(name), this.value as MusicGenre)
        Operator.LIKE -> null
    }
}

private fun Filter.toPredicateDouble(cb: CriteriaBuilder, root: Path<*>, name: String): Predicate? {
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<Double>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<Double>(name), this.value)
        Operator.LT -> cb.lt(root.get(name), this.value as Double)
        Operator.LTE -> cb.le(root.get(name), this.value as Double)
        Operator.GT -> cb.gt(root.get(name), this.value as Double)
        Operator.GTE -> cb.ge(root.get(name), this.value as Double)
        else -> null
    }
}

private fun Filter.toPredicateDate(cb: CriteriaBuilder, root: Path<*>, name: String): Predicate? {
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<LocalDate>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<LocalDate>(name), this.value)
        Operator.LT -> cb.lessThan(root.get(name), this.value as LocalDate)
        Operator.LTE -> cb.lessThanOrEqualTo(root.get(name), this.value as LocalDate)
        Operator.GT -> cb.greaterThan(root.get(name), this.value as LocalDate)
        Operator.GTE -> cb.greaterThanOrEqualTo(root.get(name), this.value as LocalDate)
        else -> null
    }
}

private fun SortClause.toOrder(cb: CriteriaBuilder, root: Path<*>, name: String): Order? {
    return when (this.sortType) {
        SortType.ASC -> cb.asc(root.get<Any>(name))
        SortType.DESC -> cb.desc(root.get<Any>(name))
    }
}
