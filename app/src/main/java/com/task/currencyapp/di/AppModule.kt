package com.task.currencyapp.di

import com.task.currencyapp.common.di.CommonModule
import com.task.currencyapp.presentation.currencyConversion.CurrencyConverterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val AppModule = module {

    includes(CommonModule)

    viewModel {
        CurrencyConverterViewModel(get(), get(), get(), get())
    }
}
