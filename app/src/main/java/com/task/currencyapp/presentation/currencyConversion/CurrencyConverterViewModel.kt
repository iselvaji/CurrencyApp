package com.task.currencyapp.presentation.currencyConversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.currencyapp.domain.usecase.CurrencyInputValidationUseCase
import com.task.currencyapp.common.DispatcherProvider
import com.task.currencyapp.common.Resource
import com.task.currencyapp.domain.usecase.CurrencyConvertUseCase
import com.task.currencyapp.domain.usecase.CurrencyInfoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Currency converter view model which fetch the required details from domain (use cases)
 */
class CurrencyConverterViewModel (
    private val dispatcherProvider: DispatcherProvider,
    private val useCaseInputValidation : CurrencyInputValidationUseCase,
    private val useCaseCurrencies: CurrencyInfoUseCase,
    private val useCaseRate: CurrencyConvertUseCase,
) : ViewModel(){

    private val _state = MutableStateFlow(CurrencyConverterScreenState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        onEvent(CurrencyConverterScreenEvent.FetchCurrencies)
    }

    fun onEvent(event: CurrencyConverterScreenEvent) {
        when(event) {
            is CurrencyConverterScreenEvent.FetchCurrencies -> {
                getCurrenciesInfo()
            }
            is CurrencyConverterScreenEvent.OnCurrencyAmountChange -> {
                _state.value = _state.value.copy(inputAmount = event.amount)
                validateInputsAndGetRates()
            }
            is CurrencyConverterScreenEvent.OnCurrencySelectionChange -> {
                _state.value = _state.value.copy(selectedCurrencyCode = event.currencyCode)
                validateInputsAndGetRates()
            }
            is CurrencyConverterScreenEvent.OnCurrenciesFetched -> {
                if(state.value.selectedCurrencyCode == null)
                    _state.value = _state.value.copy(selectedCurrencyCode = event.currencyCode)
            }
        }
    }

    private fun validateInputsAndGetRates() {
        viewModelScope.launch(dispatcherProvider.io) {
            val validationResult = useCaseInputValidation.validate(state.value.inputAmount)
            if(validationResult.successful) {
                _state.value = _state.value.copy(currencyInputValidationError = false)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    getRates()
                }
            } else {
                _state.value = _state.value.copy(currencyInputValidationError = true)
            }
        }
    }

    private fun getCurrenciesInfo() {
        viewModelScope.launch(dispatcherProvider.io) {
            useCaseCurrencies.getAllCurrencies().collectLatest {
                _state.value = when(it) {
                    is Resource.Success -> {
                        _state.value.copy(isLoading = false, currenciesInfoList = it.data, currenciesInfoError = false)
                    }
                    is Resource.Error -> {
                        _state.value.copy(isLoading = false, currenciesInfoError = true)
                    }
                    is Resource.Loading -> {
                        _state.value.copy(isLoading = true, currenciesInfoError = false)
                    }
                }
            }
        }
    }

    private fun getRates() {
        viewModelScope.launch(dispatcherProvider.io) {
            useCaseRate.getConvertedRates(_state.value.selectedCurrencyCode, _state.value.inputAmount!!.toDouble()).collectLatest {
                _state.value = when(it) {
                    is Resource.Success -> {
                        _state.value.copy(isLoading = false, rates = it.data, ratesError = false)
                    }
                    is Resource.Error -> {
                        _state.value.copy(isLoading = false, ratesError = true)
                    }
                    is Resource.Loading -> {
                        _state.value.copy(isLoading = true, ratesError = false)
                    }
                }
            }
        }
    }

}