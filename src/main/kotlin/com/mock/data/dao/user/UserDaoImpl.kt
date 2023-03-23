package com.mock.data.dao.user

import com.mock.data.database.entity.User
import com.mock.data.database.query
import com.mock.data.database.entity.UserEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl : UserDao {
    private fun resultRowMapping(row: ResultRow) = User(
        id = row[UserEntity.id],
        username = row[UserEntity.username],
        password = row[UserEntity.password],
    )

    override suspend fun createUser(user: User): User? = query{
        val insertStatement = UserEntity.insert {
            it[username] = user.username
            it[password] = user.password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowMapping)
    }

    override suspend fun findAll(): List<User>? = query {
        UserEntity.selectAll().mapNotNull(::resultRowMapping)
    }

    override suspend fun update(user: User): Boolean = query {
        if (user.id == null || user.id < 0) return@query false
        UserEntity.update(where = { UserEntity.id eq user.id }) {
            it[username] = username
            it[password] = password
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