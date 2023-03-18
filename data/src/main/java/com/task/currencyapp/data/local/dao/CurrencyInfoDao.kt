package com.task.currencyapp.data.local.dao

import androidx.room.*
import com.task.currencyapp.data.local.DBConstants.COLUMN_CURRENCY_NAME
import com.task.currencyapp.data.local.DBConstants.COLUMN_CURRENCY_SYMBOL
import com.task.currencyapp.data.local.DBConstants.TABLE_CURRENCY_INFO
import com.task.currencyapp.data.local.entities.CurrencyInfoEntity

@Dao
internal interface CurrencyInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyInfo(currencies: List<CurrencyInfoEntity>)

    @MapInfo(keyColumn = COLUMN_CURRENCY_SYMBOL, valueColumn = COLUMN_CURRENCY_NAME)
    @Query("SELECT * FROM $TABLE_CURRENCY_INFO ORDER BY $COLUMN_CURRENCY_SYMBOL ASC")
    suspend fun getCurrenciesMap(): Map<String, String>
}