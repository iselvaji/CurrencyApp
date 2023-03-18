package com.task.currencyapp.domain.usecase

import app.cash.turbine.test
import com.task.currencyapp.model.CurrencyInfo
import com.task.currencyapp.common.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CurrencyInfoUseCaseTest {

    @MockK
    lateinit var useCase: CurrencyInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = mockk()
    }

    @Test
    fun `get currencies list and validate`() = runBlocking {

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
            useCase.getAllCurrencies()
        } returns flow {
            emit(Resource.Loading())
            delay(100L)
            emit( Resource.Success(currencies))
        }

        // when
        val response = useCase.getAllCurrencies()

        // then
        response.test {
            val progress = awaitItem()
            assert(progress is Resource.Loading)
            delay(100L)

            val status = awaitItem()
            assert(status is Resource.Success)

            val currencies = status.data
            assert(!currencies.isNullOrEmpty())
            awaitComplete()
        }
    }
}