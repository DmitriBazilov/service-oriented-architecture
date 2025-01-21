package soa.myts.bazilov.repository

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response.Status
import org.hibernate.Session
import soa.myts.bazilov.configuration.DatabaseSessionManager
import soa.myts.bazilov.model.domain.BestGroup
import soa.myts.bazilov.model.dto.Response
import soa.myts.bazilov.model.dto.WebException

@ApplicationScoped
class BestGroupRepository {

    @Inject
    private lateinit var  databaseSessionManager: DatabaseSessionManager

    fun saveBestGroup(bestGroup: BestGroup): BestGroup?  {
        val session: Session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val g = session.get(BestGroup::class.java, bestGroup)
            if (g != null) throw WebException(Response("cant reward one group for one genre twice"), Status.BAD_REQUEST)
            session.persist(bestGroup)

            session.transaction.commit()
            return bestGroup
        } catch (ex: WebException) {
            throw ex
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw WebException(Response("internal server error"), Status.INTERNAL_SERVER_ERROR)
        } finally {
            databaseSessionManager.closeSession(session)
        }
    }
}