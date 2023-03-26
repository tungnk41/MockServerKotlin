package com.mock.client.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val data : UserInfoData?
)

@Serializable
data class UserInfoData(
    val id: Int?,
    val username: String?
)