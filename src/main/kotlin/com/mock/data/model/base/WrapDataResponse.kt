package com.mock.data.model.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class WrapDataResponse<D: DataResponse>(@SerialName("data") val data: D)
