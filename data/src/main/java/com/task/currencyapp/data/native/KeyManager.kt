package com.task.currencyapp.data.native

internal object KeyManager {
    external fun apiKey(): String
    init {
        System.loadLibrary("native-lib")
    }
}