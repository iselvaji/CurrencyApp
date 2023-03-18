package com.task.currencyapp.data.repository.currencyRate

import android.util.Log
import com.task.currencyapp.common.ConnectivityObserver
import com.task.currencyapp.data.local.dao.RatesDao
import com.task.currencyapp.data.local.entities.RatesEntity
import com.task.currencyapp.data.remote.ApiService
import com.task.currencyapp.data.toRateEntityList
import com.task.currencyapp.data.toRateInfo
import com.task.currencyapp.model.RateInfo
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

/**
 * Return the currency exchange rates from local data source
 * if data refresh interval expired then fetch the currency rates
 * from remote data source and save it to database
 */
internal class CurrencyRateRepositoryImpl(
    private val apiService: ApiService,
    private val ratesDao: RatesDao,
    private val connectivityObserver: ConnectivityObserver
    ) : CurrencyRateRepository
{
    companion object {
        const val DATA_REFRESH_INTERVAL : Long = 30 // Minutes
    }

    override suspend fun getRates(): List<RateInfo> {
        var result = emptyList<RateInfo>()
        try {
            val locallyCachedData = getCurrencyRatesFromDatabase()
            val isNetworkConnected = connectivityObserver.isOnline.first()
            Log.d("isNetworkConnected ", isNetworkConnected.toString())

            val isDataRefreshRequired = locallyCachedData.isEmpty() ||
                    isRefreshIntervalExpired(locallyCachedData.first().timeStamp)

            Log.d("isDataRefreshRequired ", isDataRefreshRequired.toString())

            result = if(isDataRefreshRequired && isNetworkConnected) {
                    getCurrencyRatesFromApiAndSave()
                } else
                    locallyCachedData.map { it.toRateInfo() }

        } catch (ex: Exception) {
           ex.printStackTrace()
        }
        return result
    }


    private suspend fun getCurrencyRatesFromDatabase(): List<RatesEntity> {
        val localData = ratesDao.getAllRates()
        Log.d("Rates Size [db] ", localData.size.toString())
        return localData
    }

    private suspend fun getCurrencyRatesFromApiAndSave(): List<RateInfo> {
        Log.d("fetch rates ", "getCurrencyRatesFromApi..")
        /*
            Considered current time as exchange rate time stamp and its saved in database.
             which is used for rates refresh [api call] interval expiry check
        */
        val timeStamp = System.currentTimeMillis()

        // If we need to consider exchange rates expiry based on published timestamp then we can use below logic
        // val timeStamp = remoteData.timeStamp

        val remoteData = apiService.getLatestRates().toRateEntityList(timeStamp)

        Log.d("Rates Size [api] ", remoteData.size.toString())

        if(remoteData.isNotEmpty()) {
            // save to local database
            ratesDao.insertRates(remoteData)
        }
        return remoteData.map { it.toRateInfo() }
    }

    private fun isRefreshIntervalExpired(lastUpdatedTime : Long) : Boolean {
        val diffInMs = System.currentTimeMillis() - lastUpdatedTime
        val minutesExpired : Long = TimeUnit.MILLISECONDS.toMinutes(diffInMs)
        Log.d("Minutes Expired", minutesExpired.toString())
        return minutesExpired >= DATA_REFRESH_INTERVAL
    }

}