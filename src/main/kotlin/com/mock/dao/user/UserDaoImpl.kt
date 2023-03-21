package com.mock.dao.user

import com.mock.dao.DatabaseFactory.query
import com.mock.models.User
import com.mock.models.UserEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserDaoImpl : UserDao {
    private fun resultRowMapping(row: ResultRow) = User(
        id = row[UserEntity.id],
        username = row[UserEntity.username],
        password = row[UserEntity.password],
    )

    override suspend fun createUser(user: User): User? = query{
        val insertStatement = UserEntity.insert {
            it[UserEntity.username] = user.username
            it[UserEntity.password] = user.password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowMapping)
    }

    override suspend fun findByUsername(username: String): User? = query {
        UserEntity
            .select { UserEntity.username eq username }
            .map(::resultRowMapping).singleOrNull()
    }
}

val userDAO = UserDaoImpl()