package com.mock.application.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.mock.Environment
import com.mock.config
import com.mock.data.database.entity.User
import com.mock.data.model.response.auth.TokenResponse
import java.util.*
import java.util.concurrent.TimeUnit

class TokenManager {

    companion object {
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
    private val secret = config[Environment.JWT_SECRET]
    private val tokenExpired = config[Environment.JWT_EXPIRED]?.toLong() ?: TimeUnit.MINUTES.toMillis(5) //15 Minutes
    private val refreshTokenExpired: Long = tokenExpired + TimeUnit.MINUTES.toMillis(10) //30 minutes
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .build()

    fun isVerifiedToken(token: String): Boolean {
        return verifier.verify(token).claims["userId"] != null && verifier.verify(token).claims["username"] != null && verifier.verify(token).claims["tokenType"] != null
    }

    fun isExpiredToken(token: String): Boolean {
        return verifier.verify(token).expiresAt.before(Calendar.getInstance().time)
    }

    fun isRefreshToken(token: String) : Boolean {
        return verifier.verify(token).claims["tokenType"]?.asString() == REFRESH_TOKEN
    }

    fun isAccessToken(token: String) : Boolean {
        return verifier.verify(token).claims["tokenType"]?.asString() == ACCESS_TOKEN
    }

    /**
     * Produce token and refresh token for this combination of User and Account
     */
    fun generateToken(userId: Int, username: String) = TokenResponse.Data(
        createAccessToken(userId,username, generateTokenExpiration(tokenExpired)),
        createRefreshToken(userId,username, generateTokenExpiration(refreshTokenExpired))
    )

    fun refreshToken(token: String) : TokenResponse.Data?{
        val userId = verifier.verify(token).claims["userId"]?.asInt()
        val username = verifier.verify(token).claims["username"]?.asString()
        if (userId == null || username == null) return null
        val accessToken = createAccessToken(userId = userId, username = username, generateTokenExpiration(tokenExpired))
        val refreshToken = createRefreshToken(userId = userId, username = username, generateTokenExpiration(refreshTokenExpired))
        return TokenResponse.Data(accessToken, refreshToken)
    }

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