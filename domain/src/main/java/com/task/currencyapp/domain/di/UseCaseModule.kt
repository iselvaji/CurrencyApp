package com.task.currencyapp.domain.di

import com.task.currencyapp.data.di.RepositoryModule
import com.task.currencyapp.domain.usecase.CurrencyConvertUseCase
import com.task.currencyapp.domain.usecase.CurrencyInfoUseCase
import com.task.currencyapp.domain.usecase.CurrencyInputValidationUseCase
import org.koin.dsl.module

val UseCaseModule = module {

    includes(RepositoryModule)

    single {
       CurrencyInfoUseCase(get())
    }

    single {
        CurrencyConvertUseCase(get(), get())
    }

    single {
        CurrencyInputValidationUseCase()
    }
}