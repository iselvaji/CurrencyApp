package com.task.currencyapp.data.remote

import com.task.currencyapp.data.remote.model.RatesDto
import io.ktor.client.statement.*

/**
 * Api service interface which contains the remote api function and end points
 */

internal interface ApiService {

    companion object {
        const val BASE_URL = "https://openexchangerates.org/api/"
        const val END_POINT_LATEST_RATES = "latest.json?"
        const val END_POINT_CURRENCY_INFO = "currencies.json?"
    }

    sealed class EndPoints(val url: String) {
        object GetLatestRates : EndPoints(BASE_URL + END_POINT_LATEST_RATES)
        object GetCurrencyInfo : EndPoints(BASE_URL + END_POINT_CURRENCY_INFO)
    }

    suspend fun getLatestRates() : RatesDto
    suspend fun getCurrencies() : HttpResponse
}