package com.task.currencyapp.data.di

import androidx.room.Room
import com.task.currencyapp.data.local.DBConstants.DATABASE_NAME
import com.task.currencyapp.data.local.RatesDatabase
import com.task.currencyapp.data.local.dao.CurrencyInfoDao
import com.task.currencyapp.data.local.dao.RatesDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

internal val LocalModule = module {

    single {
         Room.databaseBuilder(androidApplication(), RatesDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideRatesDao(dataBase: RatesDatabase): RatesDao {
        return dataBase.ratesDao()
    }

    fun provideCurrenciesDao(dataBase: RatesDatabase): CurrencyInfoDao {
        return dataBase.currencyInfoDao()
    }

    single { provideRatesDao(get()) }

    single { provideCurrenciesDao(get()) }

}
