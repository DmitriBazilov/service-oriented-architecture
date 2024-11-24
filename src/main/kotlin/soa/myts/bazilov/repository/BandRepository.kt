package soa.myts.bazilov.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.hibernate.Session
import soa.myts.bazilov.configuration.DatabaseSessionManager
import soa.myts.bazilov.model.domain.Band

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

    fun getBands(): List<Band> {
        val session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val criteria = session.criteriaBuilder.createQuery(Band::class.java)
            criteria.from(Band::class.java)
            return session.createQuery(criteria).resultList
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