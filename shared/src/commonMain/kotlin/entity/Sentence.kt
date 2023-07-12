package entity

import kotlinx.serialization.Serializable

@Serializable
data class Sentence(val name: String, val from: String)
