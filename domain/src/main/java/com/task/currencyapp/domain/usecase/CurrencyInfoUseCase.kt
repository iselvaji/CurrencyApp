package com.task.currencyapp.domain.usecase

import com.task.currencyapp.model.CurrencyInfo
import com.task.currencyapp.common.Resource
import com.task.currencyapp.data.repository.currencyInfo.CurrencyInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * get the all supported currencies (currency code and description) from data source
 */
class CurrencyInfoUseCase(private val repository: CurrencyInfoRepository) {

    suspend fun getAllCurrencies(): Flow<Resource<List<CurrencyInfo>>> = flow {
        try {
            emit(Resource.Loading())
            val currencies = repository.getCurrenciesMap().map { CurrencyInfo(it.key, it.value)}
            emit(Resource.Success(currencies))
        } catch (ex: Exception) {
            emit(Resource.Error(ex.localizedMessage))
        }
    }
}