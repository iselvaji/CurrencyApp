package com.task.currencyapp.data.di

import com.task.currencyapp.data.native.KeyManager
import com.task.currencyapp.data.remote.ApiClient
import com.task.currencyapp.data.remote.ApiService
import com.task.currencyapp.data.remote.ApiServiceImpl
import io.ktor.client.*
import org.koin.dsl.module

internal val RemoteModule = module {

    single {
        ApiClient.getClient(HttpClient().engine, KeyManager.apiKey())
    }

    single<ApiService> {
        ApiServiceImpl(get())
    }
}