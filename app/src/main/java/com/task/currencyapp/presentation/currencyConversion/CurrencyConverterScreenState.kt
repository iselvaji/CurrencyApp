package com.task.currencyapp.presentation.currencyConversion

import com.task.currencyapp.model.CurrencyInfo
import com.task.currencyapp.model.RateInfo

/**
 * Currency converter screen UI state
 */

data class CurrencyConverterScreenState(
    val isLoading : Boolean = false,
    val currenciesInfoList: List<CurrencyInfo>? = emptyList(),

    val rates : List<RateInfo>? = emptyList(),

    val inputAmount : String? = null,
    val selectedCurrencyCode : String? = null,

    val currenciesInfoError: Boolean = false,
    val ratesError: Boolean = false,
    val currencyInputValidationError : Boolean = false
)
