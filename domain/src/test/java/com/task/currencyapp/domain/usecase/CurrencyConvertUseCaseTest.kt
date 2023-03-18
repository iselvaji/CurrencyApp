package com.task.currencyapp.domain.usecase

import app.cash.turbine.test
import com.task.currencyapp.common.Resource
import com.task.currencyapp.data.repository.currencyInfo.CurrencyInfoRepository
import com.task.currencyapp.data.repository.currencyRate.CurrencyRateRepository
import com.task.currencyapp.model.RateInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CurrencyConvertUseCaseTest {

    private lateinit var useCase: CurrencyConvertUseCase

    @MockK
    private lateinit var repositoryRate: CurrencyRateRepository

    @MockK
    private lateinit var repositoryInfo: CurrencyInfoRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repositoryRate = mockk()
        repositoryInfo = mockk()
        useCase = CurrencyConvertUseCase(repositoryRate, repositoryInfo)
    }

    @Test
    fun `get converted rates for supported currency and validate`() = runBlocking {

        val rateAED = RateInfo("AED", 3.0, "United Arab Emirates Dirham")
        val rateAUD = RateInfo("AUD", 2.0, "Australian Dollar")
        val rateINR = RateInfo("INR", 1.0, "Indian Rupee")

        val rates = mutableListOf<RateInfo>()
        rates.add(rateAED)
        rates.add(rateAUD)
        rates.add(rateINR)

        val currencyMap = HashMap<String, String>()
        currencyMap["AED"] = "United Arab Emirates Dirham"
        currencyMap["AUD"] = "Australian Dollar"
        currencyMap["INR"] = "Indian Rupee"

        // given
        coEvery {
            repositoryRate.getRates()
        } returns rates

        coEvery {
            repositoryInfo.getCurrenciesMap()
        } returns currencyMap

        val selectedCurrency = "INR"
        val amountToConvert = 10.0

        // when
        val response = useCase.getConvertedRates(selectedCurrency, amountToConvert)

        // then
        response.test {
            val progress = awaitItem()
            assert(progress is Resource.Loading)
            delay(100L)

            val status = awaitItem()
            assert(status is Resource.Success)

            val convertedRates = status.data
            assert(!convertedRates.isNullOrEmpty())

            assert(convertedRates!!.size == rates.size - 1)

            val highExchangeRate = convertedRates.find { it.currencyCode ==  "AED"}?.exchangeRate
            val lowExchangeRate = convertedRates.find { it.currencyCode ==  "AUD"}?.exchangeRate

            assert(highExchangeRate!! > lowExchangeRate!!)

            awaitComplete()
        }
    }

    @Test
    fun `get converted rates for invalid currency and validate`() = runBlocking {

        val rateAED = RateInfo("AED", 3.0, "United Arab Emirates Dirham")
        val rateAUD = RateInfo("AUD", 2.0, "Australian Dollar")

        val rates = mutableListOf<RateInfo>()
        rates.add(rateAED)
        rates.add(rateAUD)

        val currencyMap = HashMap<String, String>()
        currencyMap["AED"] = "United Arab Emirates Dirham"
        currencyMap["AUD"] = "Australian Dollar"
        currencyMap["INR"] = "Indian Rupee"

        val selectedCurrency = "INVALID"
        val amountToConvert = 25.0

        // given
        coEvery {
            repositoryRate.getRates()
        } returns rates

        coEvery {
            repositoryInfo.getCurrenciesMap()
        } returns currencyMap

        // when
        val response = useCase.getConvertedRates(selectedCurrency, amountToConvert)

        // then
        response.test {
            val progress = awaitItem()
            assert(progress is Resource.Loading)
            delay(100L)

            val status = awaitItem()
            assert(status is Resource.Success)

            val convertedRates = status.data
            assert(convertedRates.isNullOrEmpty())

            awaitComplete()
        }
    }

}