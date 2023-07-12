package service

import entity.TimeEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import network.Network

interface TimeService {

    suspend fun getTime(): TimeEntity

    companion object {
        val instance = TimeServiceImpl(Network.client)
    }
}

class TimeServiceImpl(private val client: HttpClient) : TimeService {
    override suspend fun getTime(): TimeEntity = client.get("/api/getTime").body()
}