package com.mock.data.model.response.note

import com.mock.data.model.base.DataResponse
import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse(val id: Int, val title: String, val content: String) : DataResponse()