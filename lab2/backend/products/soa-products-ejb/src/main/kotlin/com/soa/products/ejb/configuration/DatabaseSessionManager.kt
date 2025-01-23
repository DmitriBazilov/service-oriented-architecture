package com.soa.products.ejb.configuration

import com.soa.products.ejb.domain.Band
import com.soa.products.ejb.domain.BestGroup
import com.soa.products.ejb.domain.MusicStudio
import jakarta.enterprise.context.ApplicationScoped
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.jboss.ejb3.annotation.Pool
import jakarta.ejb.Stateless

@Stateless
@Pool("band-pool")
open class DatabaseSessionManager {

    companion object {
        private const val CONFIG_NAME = "hibernate.cfg.xml"
    }

    private var sessionFactory: SessionFactory? = null

    init {
        val configuration = Configuration()
            .configure(CONFIG_NAME)
            .addAnnotatedClass(Band::class.java)
            .addAnnotatedClass(MusicStudio::class.java)
            .addAnnotatedClass(BestGroup::class.java)
        StandardServiceRegistryBuilder()
            .applySettings(configuration.properties).build()
        sessionFactory = configuration.buildSessionFactory()
    }

    open fun getSession() = sessionFactory!!.openSession()

    open fun closeSession(session: Session) {
        if (session.isOpen) {
            session.close()
        }
    }
}
