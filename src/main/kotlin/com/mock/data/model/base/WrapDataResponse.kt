package com.mock.data.model.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WrapDataResponse<D>(val message: String = "", val code: String = "", @SerialName("data") val data: D)

@Serializable
class WrapListDataResponse<D>(val message: String = "", val code: String = "", @SerialName("data") val data: List<D>)
