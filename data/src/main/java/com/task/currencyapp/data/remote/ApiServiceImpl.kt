package com.task.currencyapp.data.remote

import com.task.currencyapp.data.remote.model.RatesDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

/**
 * Api service implementation for remote api calls with request parameters via provided http client
 *
 */

internal class ApiServiceImpl(private val client: HttpClient) : ApiService {

    override suspend fun getLatestRates() : RatesDto {
        return client.get {
            url(ApiService.EndPoints.GetLatestRates.url)
        }
    }

    override suspend fun getCurrencies(): HttpResponse {
        return client.get {
            url(ApiService.EndPoints.GetCurrencyInfo.url)
        }
    }
}