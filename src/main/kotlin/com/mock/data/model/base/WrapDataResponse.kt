package com.mock.data.model.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
abstract class WrapDataResponse<D: DataResponse> {
    open var message: String = ""
    open var code: String = ""
    @SerialName("data")
    abstract var data: D?
}

@Serializable
abstract class WrapListDataResponse<D: DataResponse>{
    open var message: String = ""
    open var code: String = ""
    @SerialName("data")
    abstract var data: List<D>?
}
