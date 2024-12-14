package soa.myts.bazilov.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.hibernate.Session
import soa.myts.bazilov.configuration.DatabaseSessionManager
import soa.myts.bazilov.model.domain.BestGroup

@ApplicationScoped
class BestGroupRepository {

    @Inject
    private lateinit var  databaseSessionManager: DatabaseSessionManager

    fun saveBestGroup(bestGroup: BestGroup): BestGroup?  {
        val session: Session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val g = session.get(bestGroup::class.java, bestGroup)
            if (g != null) return null
            session.persist(bestGroup)

            session.transaction.commit()
            return bestGroup
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
}