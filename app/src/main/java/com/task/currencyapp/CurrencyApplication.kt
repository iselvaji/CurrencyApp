package com.task.currencyapp

import android.app.Application
import com.task.currencyapp.di.AppModule
import com.task.currencyapp.domain.di.UseCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CurrencyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CurrencyApplication)
            koin.loadModules(listOf(AppModule, UseCaseModule))
        }
    }
}