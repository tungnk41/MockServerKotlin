package com.mock.client

class TokenManager {
    var access_token =""
        private set
    var refresh_token = ""
        private set

    fun setAccessToken(accessToken: String) {
        this.access_token = access_token
    }
    fun setRefreshToken(refreshToken: String) {
        this.refresh_token = refresh_token
    }

}