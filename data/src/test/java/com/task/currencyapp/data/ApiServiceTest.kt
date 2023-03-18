package com.task.currencyapp.data

import com.task.currencyapp.data.mock.ApiFailureMockEngine
import com.task.currencyapp.data.mock.ApiSuccessMockEngine
import com.task.currencyapp.data.remote.ApiClient
import com.task.currencyapp.data.remote.ApiServiceImpl
import io.ktor.client.statement.*
import io.ktor.http.*
import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class ApiServiceTest {

    private val apiMockClient = ApiClient.getClient(ApiSuccessMockEngine.getEngine(), "")
    private val apiFailureMockClient = ApiClient.getClient(ApiFailureMockEngine.getEngine(), "")

    private val apiService = ApiServiceImpl(apiMockClient)
    private val apiServiceFailure = ApiServiceImpl(apiFailureMockClient)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test rates api success response`() = runBlocking {
        val response = apiService.getLatestRates()
        assert(response.rates.isNotEmpty())
        assert(response.base == "USD")
    }

    @Test
    fun `test rates api failure (exception) response`() = runBlocking {
        val exception = kotlin.runCatching {
            apiServiceFailure.getLatestRates()
        }.exceptionOrNull()
        assertNotNull(exception)
    }

    @Test
    fun `test currencies api success response`() = runBlocking {
        val apiResponse = apiService.getCurrencies()
        val response = Json.decodeFromString<Map<String, String>>(apiResponse.readText())
        assert(apiResponse.status == HttpStatusCode.OK)
        assert(response.isNotEmpty())
    }

    @Test
    fun `test currencies api failure (exception) response`() = runBlocking {
        val exception = kotlin.runCatching {
            apiServiceFailure.getCurrencies()
        }.exceptionOrNull()
        assertNotNull(exception)
    }

}