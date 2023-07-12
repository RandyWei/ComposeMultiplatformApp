package service

import entity.PageEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import network.Network

interface ImageService {
    suspend fun getImage(): PageEntity

    companion object {
        val instance = ImageServiceImpl(Network.client)
    }
}

class ImageServiceImpl(private val client: HttpClient) : ImageService {
    override suspend fun getImage(): PageEntity = client.get {
        url {
            path("/api/getImages")
            parameters.append("type", "person")
            parameters.append("page", "0")
            parameters.append("size", "1")
        }
    }.body()
}