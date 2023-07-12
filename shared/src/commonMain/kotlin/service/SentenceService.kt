package service

import entity.Sentence
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import network.Network

interface SentenceService {
    suspend fun sentences(): Sentence


    companion object {
        val instance = SentenceServiceImpl(Network.client)
    }
}

class SentenceServiceImpl(private val client: HttpClient) : SentenceService {
    override suspend fun sentences(): Sentence = client.get("/api/sentences").body()

}
