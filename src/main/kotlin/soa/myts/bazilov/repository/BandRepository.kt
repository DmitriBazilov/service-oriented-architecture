package soa.myts.bazilov.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.hibernate.Session
import soa.myts.bazilov.configuration.DatabaseSessionManager
import soa.myts.bazilov.model.domain.Band
import soa.myts.bazilov.model.domain.filter.Filter
import soa.myts.bazilov.model.domain.filter.Operator
import soa.myts.bazilov.model.domain.filter.field.Type
import java.time.LocalDate

@ApplicationScoped
class BandRepository {

    @Inject
    private lateinit var  databaseSessionManager: DatabaseSessionManager

    fun saveBand(band: Band) {
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
            throw e
        } finally {
            databaseSessionManager.closeSession(session)
        }
    }

    fun getBands(filters: List<Filter>): List<Band> {
        val session = databaseSessionManager.getSession()
        try {
            //todo codegen and getting
            session.beginTransaction()
            val criteriaBuilder = session.criteriaBuilder
            var criteriaQuery = criteriaBuilder.createQuery(Band::class.java)
            val root = criteriaQuery.from(Band::class.java)

            val predicates = filters.map { filter ->
                filter.toPredicate(criteriaBuilder, root)
            }
            val whereClause = criteriaBuilder.and(*predicates.toTypedArray())
            criteriaQuery = criteriaQuery.where(whereClause)

            return session.createQuery(criteriaQuery).resultList
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw e
        } finally {
            if (session.isOpen) {
                databaseSessionManager.closeSession(session)
            }
        }
    }
}

fun Filter.toPredicate(cb: CriteriaBuilder, root: Root<Band>) = when (this.field.valueType) {
    Type.INT -> toPredicateInt(cb, root)
    Type.LONG -> toPredicateLong(cb, root)
    Type.DOUBLE -> toPredicateDouble(cb, root)
    Type.STRING -> toPredicateString(cb, root)
    Type.LOCAL_DATE -> toPredicateDate(cb, root)
}

private fun Filter.toPredicateInt(cb: CriteriaBuilder, root: Root<Band>): Predicate? {
    val name = this.field.dbName
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

private fun Filter.toPredicateLong(cb: CriteriaBuilder, root: Root<Band>): Predicate? {
    val name = this.field.dbName
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

private fun Filter.toPredicateString(cb: CriteriaBuilder, root: Root<Band>): Predicate? {
    val name = this.field.dbName
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<String>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<String>(name), this.value)
        Operator.LIKE -> cb.like(root.get(name), this.value as String)
        else -> null
    }
}

private fun Filter.toPredicateDouble(cb: CriteriaBuilder, root: Root<Band>): Predicate? {
    val name = this.field.dbName
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

private fun Filter.toPredicateDate(cb: CriteriaBuilder, root: Root<Band>): Predicate? {
    val name = this.field.dbName
    return when (this.operator) {
        Operator.EQ -> cb.equal(root.get<LocalDate>(name), this.value)
        Operator.NEQ -> cb.notEqual(root.get<LocalDate>(name), this.value)
        Operator.LT -> cb.lessThan(root.get(name), this.value as LocalDate)
        Operator.LTE -> cb.lessThan(root.get(name), this.value as LocalDate)
        Operator.GT -> cb.lessThan(root.get(name), this.value as LocalDate)
        Operator.GTE -> cb.lessThan(root.get(name), this.value as LocalDate)
        else -> null
    }
}
