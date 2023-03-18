package com.task.currencyapp.data.repository.currencyInfo

import android.util.Log
import com.task.currencyapp.common.ConnectivityObserver
import com.task.currencyapp.data.local.dao.CurrencyInfoDao
import com.task.currencyapp.data.remote.ApiService
import com.task.currencyapp.data.toCurrencyInfoEntityList
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.first
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class CurrencyInfoRepositoryImpl(private val apiService: ApiService,
                                          private val currenciesDao: CurrencyInfoDao,
                                          private val connectivityObserver: ConnectivityObserver
) : CurrencyInfoRepository
{
    override suspend fun getCurrenciesMap(): Map<String, String> {
        var currencies : Map<String, String> = emptyMap()
        try {
            currencies = getCurrenciesFromDatabase()
            val isNetworkConnected = connectivityObserver.isOnline.first()
            if (isNetworkConnected && currencies.isEmpty()) {
                currencies = getCurrenciesFromApiAndSave()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return currencies
    }

    private suspend fun getCurrenciesFromDatabase(): Map<String, String> {
        val localData = currenciesDao.getCurrenciesMap()
        Log.d("Currencies size (db) ", localData.size.toString())
        return localData
    }

    private suspend fun getCurrenciesFromApiAndSave(): Map<String, String> {
        Log.d("Currencies db is empty" , "Fetch it from remote api..")

        val remoteData = apiService.getCurrencies()
        val currenciesMap = Json.decodeFromString<Map<String, String>>(remoteData.readText())

        // save it to database
        currenciesDao.insertCurrencyInfo(currenciesMap.toCurrencyInfoEntityList())
        Log.d("Currencies size [api]", currenciesMap.size.toString())

        return currenciesMap
    }

}

