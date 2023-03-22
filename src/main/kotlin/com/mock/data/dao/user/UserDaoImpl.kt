package com.mock.data.dao.user

import com.mock.data.database.query
import com.mock.application.models.User
import com.mock.data.database.entity.UserEntity
import org.jetbrains.exposed.sql.*

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

    override suspend fun updateById(userId: Int, user: User): Boolean = query {
        UserEntity.update({ UserEntity.id eq userId }) {
            it[username] = username
            it[password] = password
        } > 0
    }

    override suspend fun deleteById(userId: Int): Boolean = query {
        UserEntity.deleteWhere { UserEntity.id eq userId } >0
    }

    override suspend fun findById(userId: Int): User? = query{
        UserEntity
            .select { UserEntity.id eq userId }
            .map(::resultRowMapping).singleOrNull()
    }

    override suspend fun findByUsername(username: String): User? = query {
        UserEntity
            .select { UserEntity.username eq username }
            .map(::resultRowMapping).singleOrNull()
    }
}