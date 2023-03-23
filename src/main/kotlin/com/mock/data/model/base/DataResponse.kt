package com.mock.data.model.base

abstract class DataResponse {
    fun wrap() = WrapDataResponse(this)
}
