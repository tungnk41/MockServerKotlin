package com.mock.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(val id: Int? = null, val title: String, val content: String)