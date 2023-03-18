package com.task.currencyapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.currencyapp.data.local.DBConstants.COLUMN_CURRENCY_NAME
import com.task.currencyapp.data.local.DBConstants.COLUMN_CURRENCY_SYMBOL
import com.task.currencyapp.data.local.DBConstants.TABLE_CURRENCY_INFO

@Entity(tableName = TABLE_CURRENCY_INFO)
internal data class CurrencyInfoEntity (

    @PrimaryKey
    @ColumnInfo(name = COLUMN_CURRENCY_SYMBOL)
    val currencyCode: String,

    @ColumnInfo(name = COLUMN_CURRENCY_NAME)
    val currencyName : String
    )