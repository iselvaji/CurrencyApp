package com.task.currencyapp.presentation

import com.task.currencyapp.domain.usecase.CurrencyInputValidationUseCase
import com.task.currencyapp.common.Resource
import com.task.currencyapp.common.testUtils.CoroutineTestRule
import com.task.currencyapp.common.testUtils.TestDispatcherProvider
import com.task.currencyapp.domain.usecase.CurrencyConvertUseCase
import com.task.currencyapp.domain.usecase.CurrencyInfoUseCase
import com.task.currencyapp.model.CurrencyInfo
import com.task.currencyapp.model.RateInfo
import com.task.currencyapp.presentation.currencyConversion.CurrencyConverterScreenEvent
import com.task.currencyapp.presentation.currencyConversion.CurrencyConverterViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyViewModelTest {

    private var dispatcherProvider = TestDispatcherProvider()

    @get:Rule
    val dispatcherRule = CoroutineTestRule(dispatcherProvider.default)

    @MockK
    private lateinit var currencyInfoUseCase: CurrencyInfoUseCase

    @MockK
    private lateinit var currencyConvertUseCase: CurrencyConvertUseCase

    @MockK
    private lateinit var currencyInputValidationUseCase: CurrencyInputValidationUseCase

    private lateinit var viewModel: CurrencyConverterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        currencyInfoUseCase = mockk()
        currencyConvertUseCase = mockk()
        viewModel = CurrencyConverterViewModel(dispatcherProvider,
            currencyInputValidationUseCase,
            currencyInfoUseCase,
            currencyConvertUseCase)
    }

    @Test
    fun `for success resource for currencies, view model data must be available`() = runTest {

        val currencyCodeJPY = "JPY"
        val currencyNameJPY = "Japanese Yen"

        val currencyCodeUSD = "INR"
        val currencyNameUSD = "Indian Rupee"

        val currencyJPY = CurrencyInfo(currencyCodeJPY, currencyNameJPY)
        val currencyUSD = CurrencyInfo(currencyCodeUSD, currencyNameUSD)

        val currencies = ArrayList<CurrencyInfo>()
        currencies.add(currencyJPY)
        currencies.add(currencyUSD)

        // given
        coEvery {
            currencyInfoUseCase.getAllCurrencies()
        } returns flow {
            emit(Resource.Success(currencies))
        }

        // when
        viewModel.onEvent(CurrencyConverterScreenEvent.FetchCurrencies)

        advanceUntilIdle()

        // then
        viewModel.state.value.apply {
            assert(! isLoading)
            assert(!currencyInputValidationError)
            assert(!currenciesInfoError)
            assert(currenciesInfoList!!.isNotEmpty())
        }
    }

    @Test
    fun `for success resource for rates, view model data must be available`() = runTest {

        val rateAED = RateInfo("AED", 3.0)
        val rateAUD = RateInfo("AUD", 2.0)
        val rateINR = RateInfo("INR", 1.0)

        val rates = ArrayList<RateInfo>()
        rates.add(rateAED)
        rates.add(rateAUD)
        rates.add(rateINR)

        // given
        coEvery {
            currencyConvertUseCase.getConvertedRates(any(), any())
        } returns flow {
            emit(Resource.Success(rates))
        }

        // when
        viewModel.onEvent(CurrencyConverterScreenEvent.OnCurrencyAmountChange("10"))

        advanceUntilIdle()

        // then
        viewModel.state.value.apply {
            assert(! isLoading)
            assert(!currencyInputValidationError)
            assert(! ratesError)
            assert(rates.isNotEmpty())
        }
    }

    @Test
    fun `for loading resource, data should be null && isLoading should be true`() = runTest {

        // given
        coEvery {
            currencyInfoUseCase.getAllCurrencies()
        } returns flow {
            emit(Resource.Loading())
        }

        // when
        viewModel.onEvent(CurrencyConverterScreenEvent.FetchCurrencies)

        // then
        viewModel.state.value.apply {
            assert(isLoading)
            assert(currenciesInfoList.isNullOrEmpty())
            assert(!currenciesInfoError)
        }
    }
}