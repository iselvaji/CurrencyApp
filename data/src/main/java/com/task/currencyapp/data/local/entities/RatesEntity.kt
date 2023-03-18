package com.task.currencyapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.currencyapp.data.local.DBConstants.COLUMN_BASE_CURRENCY
import com.task.currencyapp.data.local.DBConstants.COLUMN_CURRENCY_CODE
import com.task.currencyapp.data.local.DBConstants.COLUMN_EXCHANGE_RATE
import com.task.currencyapp.data.local.DBConstants.COLUMN_TIME_STAMP
import com.task.currencyapp.data.local.DBConstants.TABLE_RATES

@Entity(tableName = TABLE_RATES)
internal data class RatesEntity (

    @PrimaryKey
    @ColumnInfo(name = COLUMN_CURRENCY_CODE)
    val currencyCode: String,

    @ColumnInfo(name = COLUMN_EXCHANGE_RATE)
    val exchangeRate: Double,

    @ColumnInfo(name = COLUMN_TIME_STAMP)
    val timeStamp: Long,

    @ColumnInfo(name = COLUMN_BASE_CURRENCY)
    val baseCurrency: String
    )