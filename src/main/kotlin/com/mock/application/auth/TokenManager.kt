package com.mock.application.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.mock.Environment
import com.mock.config
import com.mock.application.model.User
import com.mock.data.model.response.TokenResponse
import java.util.*

class TokenManager {

    private val secret = config[Environment.JWT_SECRET]
    private val validityInMs: Long = 1200000L // 20 Minutes
    private val refreshValidityInMs: Long = 3600000L * 24L * 30L // 30 days
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .build()

    fun isTokenVerified(token: String): Boolean {
        return verifier.verify(token).claims["userId"] != null && verifier.verify(token).claims["username"] != null && verifier.verify(token).claims["tokenType"] != null
    }

    fun isTokenExpired(token: String): Boolean {
        return verifier.verify(token).expiresAt.after(Calendar.getInstance().time)
    }

    /**
     * Produce token and refresh token for this combination of User and Account
     */
    fun generateToken(user: User) = TokenResponse(
        createAccessToken(user, getTokenExpiration()),
        createRefreshToken(user, getTokenExpiration(refreshValidityInMs))
    )

    private fun createAccessToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withClaim("userId", user.id)
        .withClaim("username", user.username)
        .withClaim("tokenType", "accessToken")
        .withExpiresAt(expiration)
        .sign(algorithm)

    private fun createRefreshToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withClaim("userId", user.id)
        .withClaim("username", user.username)
        .withClaim("tokenType", "refreshToken")
        .withExpiresAt(expiration)
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getTokenExpiration(validity: Long = validityInMs) = Date(System.currentTimeMillis() + validity)
}