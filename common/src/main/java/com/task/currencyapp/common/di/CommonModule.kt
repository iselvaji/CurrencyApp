package com.task.currencyapp.common.di

import com.task.currencyapp.common.*
import com.task.currencyapp.common.DefaultDispatcherProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val CommonModule = module {

    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }

    single<ConnectivityObserver> {
        ConnectivityNetworkMonitor(androidApplication())
    }
}
