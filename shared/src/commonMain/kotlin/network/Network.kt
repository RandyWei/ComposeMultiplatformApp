package network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Network {

    val client = HttpClient {
        //配置默认参数：域名
        defaultRequest {
            url("https://api.apiopen.top")
        }
        //日志插件
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        //json解析插件
        install(ContentNegotiation) {
            json(Json { // application/json  html/text
                ignoreUnknownKeys = true
                useAlternativeNames = true //这个配置会增加性能开销，非必要不要开启
            })
        }

    }

}