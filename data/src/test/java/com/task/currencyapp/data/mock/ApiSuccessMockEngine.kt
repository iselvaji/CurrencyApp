package com.task.currencyapp.data.mock

import com.task.currencyapp.data.util.ResourceHelper
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*

object ApiSuccessMockEngine : MockEngine {

    private val responseHeaders = headersOf("Content-Type" to
            listOf(ContentType.Application.Json.toString()))

    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                var mockResponse = ""
                val statusCode = HttpStatusCode.OK

                val urlPath = request.url.encodedPath
                println("urlPath $urlPath")

                mockResponse = if(urlPath.contains("latest.json")) {
                    ResourceHelper.getData("mocks/latest.json").toString()
                } else {
                    ResourceHelper.getData("mocks/currencies.json").toString()
                }

                respond(mockResponse, statusCode, responseHeaders)
            }
        }
    }

    override fun getEngine(): HttpClientEngine {
        return client.engine
    }
}



