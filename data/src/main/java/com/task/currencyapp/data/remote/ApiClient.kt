package com.task.currencyapp.data.remote

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*

/**
 * Api client provides the Ktor Http Client
 *
 */
internal object ApiClient {

    fun getClient(httpClientEngine: HttpClientEngine, apiKey: String) = HttpClient(httpClientEngine) {

        install(Logging) {
            level = LogLevel.ALL
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(
                json = kotlinx.serialization.json.Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }

        install(DefaultRequest) {
            parameter("app_id", apiKey)
        }
    }
}