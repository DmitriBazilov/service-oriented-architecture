package com.soa.products.ejb.dao

import com.soa.products.ejb.exception.PersonDuplicationException
import com.soa.products.ejb.service.command.CreatePersonCommand
import jakarta.annotation.Resource
import jakarta.ejb.Stateless
import org.jboss.ejb3.annotation.Pool
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

@Stateless
@Pool("products-pool")
open class PersonDao {

    @Resource(lookup = "java:/PostgresDS")
    private lateinit var dataSource: DataSource

    open fun insert(createPersonCommand: CreatePersonCommand): String {
        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = dataSource.connection
            val preparedStatement = connection.prepareStatement(INSERT_PERSON)
            preparedStatement.setString(1, createPersonCommand.name)
            preparedStatement.setString(2, createPersonCommand.passportID)
            preparedStatement.setString(3, createPersonCommand.eyeColor?.name)
            preparedStatement.setString(4, createPersonCommand.hairColor?.name)

            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return resultSet.getString("passportID")
            } else {
                throw IllegalStateException("Failed to retrieve the inserted person passportID")
            }
        } catch (e: SQLException) {
            if (e.sqlState == "23505") { // PostgreSQL code for unique violation
                throw PersonDuplicationException(createPersonCommand.passportID)
            }
            throw e
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    open fun existsById(passportID: String): Boolean {
        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = dataSource.connection
            val preparedStatement = connection.prepareStatement(EXISTS_PERSON)
            preparedStatement.setString(1, passportID)

            resultSet = preparedStatement.executeQuery()
            return resultSet.next() && resultSet.getBoolean(1)
        } catch (e: SQLException) {
            throw IllegalStateException("Failed to check existence of person by passportId", e)
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    companion object {
        private const val INSERT_PERSON = """
            INSERT INTO person (name, passportID, eyeColor, hairColor)
            VALUES (?, ?, ?::color, ?::color)
            RETURNING passportID;
        """

        private const val EXISTS_PERSON = """
            SELECT EXISTS(SELECT 1 FROM person WHERE passportID = ?)
        """
    }
}
