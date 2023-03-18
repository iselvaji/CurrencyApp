package com.task.currencyapp.data.di

import com.task.currencyapp.data.repository.currencyInfo.CurrencyInfoRepository
import com.task.currencyapp.data.repository.currencyInfo.CurrencyInfoRepositoryImpl
import com.task.currencyapp.data.repository.currencyRate.CurrencyRateRepository
import com.task.currencyapp.data.repository.currencyRate.CurrencyRateRepositoryImpl
import org.koin.dsl.module
import com.task.currencyapp.common.di.CommonModule

val RepositoryModule = module {

    includes(
        LocalModule, RemoteModule, CommonModule
    )

    single<CurrencyInfoRepository> {
        CurrencyInfoRepositoryImpl(get(), get(), get())
    }

    single<CurrencyRateRepository> {
        CurrencyRateRepositoryImpl(get(), get(), get())
    }
}
