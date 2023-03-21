package com.mock.repository.data.source.local

import com.mock.repository.data.User
import com.mock.repository.data.source.UserDataSource
import com.mock.repository.data.source.local.database.DatabaseFactory.query
import com.mock.repository.data.source.local.database.entities.UserEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserLocalDataSource : UserDataSource {

    override suspend fun insert(user: User): User? {
        var statement: InsertStatement<Number>? = null
        query {
            statement = UserEntity.insert {
                it[username] = user.username
                it[password] = user.password
            }
        }

        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun update(user: User): Int {
        val result = query {
            UserEntity.update(where = { UserEntity.id.eq(user.id ?: -1) } ) {
                it[username] = user.username
                it[password] = user.password
            }
        }

        return result
    }

    override suspend fun findById(id: Long): User? {
        val result = query {
            UserEntity
                .select { UserEntity.id.eq(id) }
                .map { rowToUser(it) }
                .singleOrNull()
        }

        return result
    }

    override suspend fun findByName(username: String): User? {
        val result = query {
            UserEntity
                .select { UserEntity.username.eq(username) }
                .map { rowToUser(it) }.
                singleOrNull()
        }
        return result
    }

    override suspend fun find(): List<User>? {
        val result = query {
            UserEntity.selectAll().mapNotNull { rowToUser(it) }
        }
        return result
    }

    override suspend fun deleteById(id: Long): Int {
        val result = query {
            UserEntity.deleteWhere {
                UserEntity.id.eq(id)
            }
        }
        return result
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }
        return User(
            id = row[UserEntity.id],
            username = row[UserEntity.username],
            password = row[UserEntity.password]
        )
    }
}