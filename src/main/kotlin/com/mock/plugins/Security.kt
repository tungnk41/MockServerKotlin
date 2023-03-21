package com.mock.plugins

import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import com.mock.auth.JwtService
import com.mock.auth.Session
import com.mock.repository.RepositoryImpl.UserRepository
import com.mock.repository.data.source.local.UserLocalDataSource
import io.ktor.application.*

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<Session>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    authentication {

        val repository = UserRepository(UserLocalDataSource())
        val jwtService = JwtService()
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "MockServer"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asLong()
                val user = repository.findById(claimString)
                user
            }
        }
    }
}
