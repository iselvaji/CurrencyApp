package com.task.currencyapp.model

data class CurrencyInfo(
    val symbol: String,
    val name: String,
) {
    val codeAndName : String get() = "$symbol - $name"
}