package com.task.currencyapp.data.mock

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*

object ApiFailureMockEngine : MockEngine {

    private val responseHeaders = headersOf("Content-Type" to
            listOf(ContentType.Application.Json.toString()))

    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                val mockResponse = "Not Found"
                val statusCode = HttpStatusCode.NotFound
                respond(mockResponse, statusCode, responseHeaders)
            }
        }
    }

    override fun getEngine(): HttpClientEngine {
        return  client.engine
    }
}




