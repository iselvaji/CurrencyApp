package com.task.currencyapp.model

data class RateInfo(
    val currencyCode: String,
    val exchangeRate: Double,
    val currencyName: String = ""
)
