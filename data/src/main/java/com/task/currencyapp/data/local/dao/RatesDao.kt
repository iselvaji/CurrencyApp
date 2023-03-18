package com.task.currencyapp.data.local.dao

import androidx.room.*
import com.task.currencyapp.data.local.DBConstants.COLUMN_CURRENCY_CODE
import com.task.currencyapp.data.local.DBConstants.COLUMN_EXCHANGE_RATE
import com.task.currencyapp.data.local.DBConstants.TABLE_RATES
import com.task.currencyapp.data.local.entities.RatesEntity

@Dao
internal interface RatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<RatesEntity>)

    @Query("SELECT * FROM $TABLE_RATES ORDER BY $COLUMN_CURRENCY_CODE ASC")
    suspend fun getAllRates(): List<RatesEntity>

    @Query("SELECT $COLUMN_EXCHANGE_RATE FROM $TABLE_RATES WHERE $COLUMN_CURRENCY_CODE = :currencyCode")
    suspend fun getRate(currencyCode: String): Double?
}