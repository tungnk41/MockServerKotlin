package com.mock.server.application.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.mock.server.Environment
import com.mock.server.config
import com.mock.server.application.data.model.response.auth.TokenResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.*
import java.util.concurrent.TimeUnit

class TokenManager {

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
    private val secret = config[Environment.JWT_SECRET]
    private val tokenExpired = config[Environment.JWT_EXPIRED]?.toLong() ?: TimeUnit.MINUTES.toMillis(5)
    private val refreshTokenExpired: Long = tokenExpired //+ TimeUnit.MINUTES.toMillis(10)
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .build()


    @Throws(JWTVerificationException::class)
    suspend fun isTokenVerified(call: ApplicationCall, token: String): Boolean {
            try {
                verifier.verify(token)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Unauthorized)
                return false
            }
        return true
    }

    @Throws(JWTVerificationException::class)
    fun getPayloadData(token: String): PayloadData? {
        val verifiedToken = verifier.verify(token)
        val userId = verifiedToken.claims["userId"]?.asInt() ?: return null
        val username = verifiedToken.claims["username"]?.asString() ?: return null
        val tokenType = verifiedToken.claims["tokenType"]?.asString() ?: return null
        return PayloadData(userId,username,tokenType)
    }

    /**
     * Produce token and refresh token for this combination of User and Account
     */
    fun generateToken(userId: Int, username: String) = TokenResponse.Data(
        createAccessToken(userId,username, generateTokenExpiration(tokenExpired)),
        createRefreshToken(userId,username, generateTokenExpiration(refreshTokenExpired))
    )
    private fun createAccessToken(userId: Int,username: String, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withClaim("userId", userId)
        .withClaim("username", username)
        .withClaim("tokenType", ACCESS_TOKEN)
        .withExpiresAt(expiration)
        .sign(algorithm)

    private fun createRefreshToken(userId: Int,username: String, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withClaim("userId", userId)
        .withClaim("username", username)
        .withClaim("tokenType", REFRESH_TOKEN)
        .withExpiresAt(expiration)
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun generateTokenExpiration(expiredInMillis: Long = 0) = Date(System.currentTimeMillis() + expiredInMillis)
}

data class PayloadData(val userId: Int, val username: String,val tokenType: String)