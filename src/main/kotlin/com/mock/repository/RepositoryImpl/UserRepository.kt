package com.mock.repository.RepositoryImpl

import com.mock.repository.UserRepository
import com.mock.repository.data.User
import com.mock.repository.data.source.UserDataSource

class UserRepository(val localDataSource: UserDataSource) : UserRepository {
    override suspend fun insert(user: User): User? {
        return localDataSource.insert(user)
    }

    override suspend fun findByName(userName: String): User? {
        return localDataSource.findByName(userName)
    }

    override suspend fun findById(id: Long): User? {
        return localDataSource.findById(id)
    }
}