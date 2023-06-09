package com.mock.server.application.data.model.response.note

import com.mock.server.application.data.model.base.DataResponse
import com.mock.server.application.data.model.base.WrapDataResponse
import com.mock.server.application.data.model.base.WrapListDataResponse
import kotlinx.serialization.Serializable

@Serializable
class NoteResponse(override var data: Data? = null): WrapDataResponse<NoteResponse.Data>() {
    @Serializable
    data class Data(val id: Int? = null, val title: String, val content: String) : DataResponse()

}

@Serializable
class ListNoteResponse(override var data: List<NoteResponse.Data>? = listOf()): WrapListDataResponse<NoteResponse.Data>() {
    @Serializable
    data class Data(val id: Int? = null, val title: String, val content: String) : DataResponse()
}