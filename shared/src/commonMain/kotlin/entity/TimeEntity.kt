package entity

import kotlinx.serialization.Serializable

@Serializable
data class TimeEntity(val date: String, val weekday: String)
