package com.task.currencyapp.data.repository.currencyRate

import com.task.currencyapp.model.RateInfo

interface CurrencyRateRepository {
    suspend fun getRates() : List<RateInfo>
}