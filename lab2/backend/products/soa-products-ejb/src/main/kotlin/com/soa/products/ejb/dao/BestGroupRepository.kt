package com.soa.products.ejb.dao

import com.soa.products.ejb.configuration.DatabaseSessionManager
import com.soa.products.ejb.domain.BestGroup
import com.soa.products.ejb.exception.BestGroupOperationException
import com.soa.products.ejb.exception.UnknownError
import jakarta.ejb.Stateless
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.Response.Status
import org.hibernate.Session
import org.jboss.ejb3.annotation.Pool

@Stateless
@Pool("band-pool")
open class BestGroupRepository {

    @Inject
    private lateinit var  databaseSessionManager: DatabaseSessionManager

    open fun saveBestGroup(bestGroup: BestGroup): BestGroup  {
        val session: Session = databaseSessionManager.getSession()
        try {
            session.beginTransaction()
            val g = session.get(BestGroup::class.java, bestGroup)
            if (g != null)
                throw BestGroupOperationException.TwiceRewardException("400: cant reward one group for one genre twice", Status.BAD_REQUEST.statusCode)
            session.persist(bestGroup)

            session.transaction.commit()
            return bestGroup
        } catch (ex: Exception) {
            throw ex
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.transaction.isActive) {
                session.transaction.rollback()
            }
            throw UnknownError("internal server error", Status.INTERNAL_SERVER_ERROR.statusCode)
        } finally {
            databaseSessionManager.closeSession(session)
        }
    }
}
