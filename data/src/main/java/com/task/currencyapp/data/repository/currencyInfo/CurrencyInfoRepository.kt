package com.task.currencyapp.data.repository.currencyInfo

interface CurrencyInfoRepository {
    suspend fun getCurrenciesMap() : Map<String, String>
}