package com.mock.server.application.data.database.dao.user

import com.mock.server.application.data.database.entity.User
import com.mock.server.application.data.database.query
import com.mock.server.application.data.database.entity.UserEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl : UserDao {
    private fun resultRowMapping(row: ResultRow) = User(
        id = row[UserEntity.id],
        username = row[UserEntity.username],
        password = row[UserEntity.password],
    )

    override suspend fun createUser(username: String,password: String): User? = query{
        val insertStatement = UserEntity.insert {
            it[UserEntity.username] = username
            it[UserEntity.password] = password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowMapping)
    }

    override suspend fun findAll(): List<User>? = query {
        UserEntity.selectAll().mapNotNull(::resultRowMapping)
    }

    override suspend fun update(userId: Int, username: String,password: String): Boolean = query {
        UserEntity.update(where = { UserEntity.id eq userId }) {
            it[UserEntity.username] = username
            it[UserEntity.password] = password
        } > 0
    }

    override suspend fun deleteById(userId: Int): Boolean = query {
        UserEntity.deleteWhere { id eq userId } >0
    }

    override suspend fun findById(userId: Int): User? = query{
        UserEntity
            .select { UserEntity.id eq userId }
            .mapNotNull(::resultRowMapping).singleOrNull()
    }

    override suspend fun findByUsername(username: String): User? = query {
        val a = UserEntity
            .select { UserEntity.username.eq(username) }
            .mapNotNull(::resultRowMapping).singleOrNull()
         a
    }
}