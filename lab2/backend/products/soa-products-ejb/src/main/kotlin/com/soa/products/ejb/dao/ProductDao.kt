package com.soa.products.ejb.dao

import com.soa.products.ejb.domain.Color
import com.soa.products.ejb.domain.Coordinates
import com.soa.products.ejb.domain.Person
import com.soa.products.ejb.domain.Product
import com.soa.products.ejb.domain.ProductSearchParams
import com.soa.products.ejb.domain.UnitOfMeasure
import com.soa.products.ejb.domain.search.ComparisonOperator
import com.soa.products.ejb.domain.search.SearchField
import com.soa.products.ejb.exception.ProductDuplicationException
import com.soa.products.ejb.service.command.CreateProductCommand
import com.soa.products.ejb.service.command.UpdateProductCommand
import jakarta.annotation.Resource
import jakarta.ejb.Stateless
import org.jboss.ejb3.annotation.Pool
import java.math.BigDecimal
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

@Stateless
@Pool("products-pool")
open class ProductDao {

    @Resource(lookup = "java:/PostgresDS")
    private lateinit var dataSource: DataSource

    open fun insert(command: CreateProductCommand): Long {
        val sql = """
        INSERT INTO product (
            name, coordinates, creationDate, price, manufacturerCost, 
            unitOfMeasure, partNumber, ownerPassportID
        ) VALUES (?, point(?, ?), ?, ?, ?, ?::unit_of_measure, ?, ?)
        RETURNING id;
        """.trimIndent()

        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = dataSource.connection

            val preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, command.name)
            preparedStatement.setInt(2, command.coordinates.x)
            preparedStatement.setInt(3, command.coordinates.y)
            preparedStatement.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()))
            preparedStatement.setLong(5, command.price)
            preparedStatement.setLong(6, command.manufacturerCost)
            preparedStatement.setString(7, command.unitOfMeasure.name)
            preparedStatement.setString(8, command.partNumber)
            preparedStatement.setString(9, command.ownerPassportId)

            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return resultSet.getLong(1)
            } else {
                throw IllegalStateException("Failed to retrieve the inserted product ID")
            }
        } catch (e: SQLException) {
            if (e.sqlState == "23505") { // PostgreSQL code for unique violation
                command.partNumber?.let {
                    throw ProductDuplicationException(it)
                }
            }
            throw e
        } finally {
            resultSet?.close()
            connection?.close()
        }
    }

    open fun findById(id: Long): Product? {
        val sql = """
            SELECT 
                p.id as p_id, p.name as p_name, p.coordinates, p.creationDate, p.price, 
                p.partNumber, p.manufacturerCost, p.unitOfMeasure, p.ownerPassportID, 
                pe.passportID, pe.name as pe_name,
                
            pe.eyeColor, pe.hairColor
            FROM product p
            LEFT JOIN person pe ON p.ownerPassportID = pe.passportID
            WHERE p.id = ?
        """.trimIndent()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setLong(1, id)
                preparedStatement.executeQuery().use { rs ->
                    return if (rs.next()) {
                        constructProduct(rs)
                    } else {
                        null
                    }
                }
            }
        }
    }

    open fun deleteById(id: Long): Boolean {
        val sql = "DELETE FROM Product WHERE id = ?"

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setLong(1, id)
                return preparedStatement.executeUpdate() > 0
            }
        }
    }

    open fun update(command: UpdateProductCommand): Boolean {
        val sql = """
            UPDATE product SET
                name = ?,
                coordinates = point(?, ?),
                price = ?,
                manufacturerCost = ?,
                unitOfMeasure = ?::unit_of_measure,
                partNumber = ?,
                ownerPassportId = ?
            WHERE id = ?
        """.trimIndent()

        var connection: Connection? = null

        try {
            connection = dataSource.connection

            val preparedStatement = connection.prepareStatement(sql)

            preparedStatement.setString(1, command.name)
            preparedStatement.setInt(2, command.coordinates.x)
            preparedStatement.setInt(3, command.coordinates.y)
            preparedStatement.setLong(4, command.price)
            preparedStatement.setLong(5, command.manufacturerCost)
            preparedStatement.setString(6, command.unitOfMeasure.name)
            preparedStatement.setString(7, command.partNumber)
            preparedStatement.setString(8, command.ownerPassportId)
            preparedStatement.setLong(9, command.id)

            return preparedStatement.executeUpdate() > 0
        } catch (e: SQLException) {
            if (e.sqlState == "23505") { // PostgreSQL code for unique violation
                command.partNumber?.let {
                    throw ProductDuplicationException(it)
                }
            }
            throw e
        } finally {
            connection?.close()
        }
    }

    open fun getTotalPrice(): Long {
        val sql = "SELECT COALESCE(SUM(price), 0) FROM Product"

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.executeQuery().use { rs ->
                    return if (rs.next()) {
                        rs.getLong(1)
                    } else {
                        0L
                    }
                }
            }
        }
    }

    open fun findProductWithMinPartNumber(): Product? {
        val sql = """
            SELECT 
                p.id as p_id, p.name as p_name, p.coordinates, p.creationDate, p.price, 
                p.partNumber, p.manufacturerCost, p.unitOfMeasure, p.ownerPassportID, 
                pe.passportID, pe.name as pe_name, pe.eyeColor, pe.hairColor
            FROM product p
            LEFT JOIN person pe ON p.ownerPassportID = pe.passportID
            WHERE p.partNumber = (SELECT MIN(partNumber) FROM product)
        """.trimIndent()

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.executeQuery().use { rs ->
                    return if (rs.next()) {
                        constructProduct(rs)
                    } else {
                        null
                    }
                }
            }
        }
    }

    open fun findProductsWithManufacturerCostLessThan(cost: Long, offset: Int, limit: Int): List<Product> {
        val sql = """
            SELECT 
                p.id as p_id, p.name as p_name, p.coordinates, p.creationDate, p.price, 
                p.partNumber, p.manufacturerCost, p.unitOfMeasure, p.ownerPassportID, 
                pe.passportID, pe.name as pe_name, pe.eyeColor, pe.hairColor
            FROM product p
            LEFT JOIN person pe ON p.ownerPassportID = pe.passportID
            WHERE p.manufacturerCost < ?
            ORDER BY p.manufacturerCost
            LIMIT ? OFFSET ?
        """.trimIndent()

        val products = mutableListOf<Product>()
        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setLong(1, cost)
                preparedStatement.setInt(2, limit)
                preparedStatement.setInt(3, offset)

                preparedStatement.executeQuery().use { rs ->
                    while (rs.next()) {
                        products.add(constructProduct(rs))
                    }
                }
            }
        }
        return products
    }

    open fun find(params: ProductSearchParams): List<Product> {
        val sql = buildSelectQueryAndExtendParameterSource(params)

        val products = mutableListOf<Product>()
        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                setParameters(preparedStatement, params)

                preparedStatement.executeQuery().use { rs ->
                    while (rs.next()) {
                        products.add(constructProduct(rs))
                    }
                }
            }
        }
        return products
    }

    open fun updateProductPrices(coef: BigDecimal) {
        val sql = "UPDATE product SET price = price * ?"

        dataSource.connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.setBigDecimal(1, coef)
                preparedStatement.executeUpdate()
            }
        }
    }

    private fun constructProduct(rs: ResultSet): Product {
        val passportID = rs.getString("passportID")
        val owner = passportID?.let {
            Person(
                passportID = it,
                name = rs.getString("pe_name"),
                eyeColor = rs.getString("eyeColor")?.let { color -> Color.valueOf(color) },
                hairColor = rs.getString("hairColor")?.let { color -> Color.valueOf(color) }
            )
        }

        val coords = rs.getString("coordinates").removeSurrounding("(", ")").split(",").let {
            Coordinates(it[0].toDouble().toInt(), it[1].toDouble().toInt())
        }

        return Product(
            id = rs.getLong("p_id"),
            name = rs.getString("p_name"),
            coordinates = coords,
            creationDate = rs.getDate("creationDate").toLocalDate(),
            price = rs.getLong("price"),
            partNumber = rs.getString("partNumber"),
            manufacturerCost = rs.getLong("manufacturerCost"),
            unitOfMeasure = UnitOfMeasure.valueOf(rs.getString("unitOfMeasure")),
            owner = owner
        )
    }

    private fun buildSelectQueryAndExtendParameterSource(params: ProductSearchParams): String {
        val conditions = params.filterParams.map {
            val fieldName = SEARCH_FIELD_AND_DB_FIELD_NAME_MAPPING[it.searchField]!!
            val paramPlaceholder = when (it.searchField) {
                SearchField.COORDINATES -> "point(?, ?)"
                else -> "?"
            }

            buildCondition(fieldName, it.comparisonOperator, paramPlaceholder)
        }

        val wherePart = if (conditions.isEmpty()) "" else conditions.joinToString(" AND ", "WHERE ", "")
        val sortPart = if (params.sortParams.isEmpty()) "" else params.sortParams.joinToString(", ", "ORDER BY ", "") {
            "${SEARCH_FIELD_AND_DB_FIELD_NAME_MAPPING[it.searchField]} ${it.sortOrder.name}"
        }

        return """
            SELECT
                p.id as p_id,
                p.name as p_name,
                p.coordinates,
                p.creationDate,
                p.price,
                p.partNumber,
                p.manufacturerCost,
                p.unitOfMeasure,
                p.ownerPassportID,
                pe.passportID,
                pe.name as pe_name,
                pe.eyeColor,
                pe.hairColor
            FROM product p
            LEFT JOIN person pe ON p.ownerPassportID = pe.passportID
            $wherePart
            $sortPart
            LIMIT ?
            OFFSET ?
        """.trimIndent()
    }

    private fun setParameters(preparedStatement: PreparedStatement, params: ProductSearchParams): Int {
        var idx = 1
        params.filterParams.forEach {
            when (it.searchField) {
                SearchField.COORDINATES -> {
                    val coordinates = it.value as Coordinates
                    preparedStatement.setInt(idx++, coordinates.x)
                    preparedStatement.setInt(idx++, coordinates.y)
                }

                else -> preparedStatement.setObject(idx++, it.value)
            }
        }
        preparedStatement.setInt(idx++, params.limit)
        preparedStatement.setInt(idx, params.offset)
        return idx
    }

    private fun buildCondition(fieldName: String, operator: ComparisonOperator, paramPlaceholder: String): String {
        val dbOperator = when (operator) {
            ComparisonOperator.EQUAL -> "="
            ComparisonOperator.GREATER -> ">"
            ComparisonOperator.GREATER_OR_EQUAL -> ">="
            ComparisonOperator.LESS -> "<"
            ComparisonOperator.LESS_OR_EQUAL -> "<="
            ComparisonOperator.SAME -> "~="
        }

        return "$fieldName $dbOperator $paramPlaceholder"
    }

    companion object {
        private val SEARCH_FIELD_AND_DB_FIELD_NAME_MAPPING = mapOf(
            SearchField.ID to "p.id",
            SearchField.NAME to "p.name",
            SearchField.COORDINATES to "p.coordinates",
            SearchField.CREATION_DATE to "p.creationDate",
            SearchField.PRICE to "p.price",
            SearchField.PART_NUMBER to "p.partNumber",
            SearchField.MANUFACTURER_COST to "p.manufacturerCost",
            SearchField.UNIT_OF_MEASURE to "p.unitOfMeasure",
            SearchField.OWNER_PASSPORT_ID to "p.ownerPassportId"
        )
    }
}
