package com.task.currencyapp.data.mock

import io.ktor.client.engine.*

interface MockEngine {
    fun getEngine() : HttpClientEngine
}