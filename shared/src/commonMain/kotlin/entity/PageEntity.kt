package entity

import kotlinx.serialization.Serializable

@Serializable
data class PageEntity(val list: List<ImageEntity>)

@Serializable
data class ImageEntity(val url: String)
