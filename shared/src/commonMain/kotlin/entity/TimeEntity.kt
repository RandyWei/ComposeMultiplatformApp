package entity

import kotlinx.serialization.Serializable

@Serializable
abstract class BaseResp {
    abstract val code: Int
    abstract val message: String
}

@Serializable
data class TimeEntityResp(
    val result: TimeEntity,
    override val code: Int,
    override val message: String
) : BaseResp()

@Serializable
data class TimeEntity constructor(val date: String, val weekday: String)
