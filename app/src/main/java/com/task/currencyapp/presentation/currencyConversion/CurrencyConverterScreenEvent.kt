package com.task.currencyapp.presentation.currencyConversion

/**
 * Currency converter screen UI events
 */
sealed class CurrencyConverterScreenEvent {

    object FetchCurrencies : CurrencyConverterScreenEvent()
    data class OnCurrencySelectionChange(val currencyCode: String): CurrencyConverterScreenEvent()
    data class OnCurrencyAmountChange(val amount: String): CurrencyConverterScreenEvent()
    data class OnCurrenciesFetched(val currencyCode: String): CurrencyConverterScreenEvent()
}