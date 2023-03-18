package com.task.currencyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.currencyapp.data.local.dao.CurrencyInfoDao
import com.task.currencyapp.data.local.dao.RatesDao
import com.task.currencyapp.data.local.entities.CurrencyInfoEntity
import com.task.currencyapp.data.local.entities.RatesEntity

@Database(entities = [CurrencyInfoEntity::class, RatesEntity::class], version = 1, exportSchema = false)
internal abstract class RatesDatabase : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
    abstract fun currencyInfoDao(): CurrencyInfoDao
}