package com.mock.application.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class Note(val id: Int = -1, val title: String = "", val content: String = "")