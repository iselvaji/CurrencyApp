package com.task.currencyapp.domain.usecase

import com.task.currencyapp.common.Resource
import com.task.currencyapp.data.repository.currencyInfo.CurrencyInfoRepository
import com.task.currencyapp.data.repository.currencyRate.CurrencyRateRepository
import com.task.currencyapp.model.RateInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * return the converted rates for provided amount and selected currency code
 */
class CurrencyConvertUseCase(private val repository: CurrencyRateRepository,
                             private val repositoryInfo: CurrencyInfoRepository ) {

    suspend fun getConvertedRates(
        selectedCurrency: String?, inputAmount: Double): Flow<Resource<List<RateInfo>>> = flow {

        try {
            emit(Resource.Loading())
            if (selectedCurrency.isNullOrBlank()) {
                emit(Resource.Success(emptyList()))
                return@flow
            }
            emit(Resource.Success(getConvertedRateForSelectedCurrency(inputAmount, selectedCurrency)))
        }catch (ex : Exception) {
            ex.printStackTrace()
            emit(Resource.Error(ex.localizedMessage))
        }
    }

    private suspend fun getConvertedRateForSelectedCurrency(inputAmount: Double, selectedCurrency: String): List<RateInfo> {
        val exchangeRates = repository.getRates()
        val currenciesMap = repositoryInfo.getCurrenciesMap()

        val selectedCurrencyRate = exchangeRates.find { rate -> rate.currencyCode == selectedCurrency }

        selectedCurrencyRate?.let {
            val filteredRates = exchangeRates.filter { rate -> rate.currencyCode != selectedCurrencyRate.currencyCode }
            val adjustedRate = 1 / selectedCurrencyRate.exchangeRate

            return filteredRates.map {
                RateInfo(
                    it.currencyCode,
                    inputAmount * adjustedRate * it.exchangeRate,
                    currenciesMap[it.currencyCode] ?: ""
                )
            }
        }
        return emptyList()
    }


}