package com.task.currencyapp.data.repository

import android.util.Log
import com.task.currencyapp.common.ConnectivityObserver
import com.task.currencyapp.data.local.dao.RatesDao
import com.task.currencyapp.data.local.entities.RatesEntity
import com.task.currencyapp.data.mock.ApiSuccessMockEngine
import com.task.currencyapp.data.remote.ApiClient
import com.task.currencyapp.data.remote.ApiServiceImpl
import com.task.currencyapp.data.repository.currencyRate.CurrencyRateRepository
import com.task.currencyapp.data.repository.currencyRate.CurrencyRateRepositoryImpl
import com.task.currencyapp.data.repository.currencyRate.CurrencyRateRepositoryImpl.Companion.DATA_REFRESH_INTERVAL
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class CurrencyRateRepositoryTest {

    @MockK
    private lateinit var repository: CurrencyRateRepository

    private val apiMockClient = ApiClient.getClient(ApiSuccessMockEngine.getEngine(), "")
    private val apiService = ApiServiceImpl(apiMockClient)

    @MockK
    private lateinit var currencyRatesDao: RatesDao

    @MockK
    private lateinit var connectionObserver: ConnectivityObserver


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every {
            Log.d(any(), any())
        } returns 0

        repository = mockk()
        currencyRatesDao = mockk()

        repository = CurrencyRateRepositoryImpl(apiService, currencyRatesDao, connectionObserver)
    }

    @Test
    fun `get currency exchange rates from repository when device offline which returns data from local database and validate`() =
        runBlocking {

            val rateAED = RatesEntity("AED", 3.67, System.currentTimeMillis(), "USD")
            val rateAUD = RatesEntity("AUD", 1.2, System.currentTimeMillis(), "USD")
            val rateJPY = RatesEntity("JPY", 131.4, System.currentTimeMillis(), "USD")

            val rateEntities = mutableListOf<RatesEntity>()
            rateEntities.add(rateAED)
            rateEntities.add(rateAUD)
            rateEntities.add(rateJPY)

            coEvery {
                connectionObserver.isOnline
            } returns flow {
                emit(false)
            }

            coEvery {
                currencyRatesDao.getAllRates()
            } returns rateEntities


            val rates = repository.getRates()

            assert(rates.size == rateEntities.size)
            assert(rates.first().exchangeRate == 3.67)

            val jpyRate = rates.find { it.currencyCode == "JPY" }

            assert(jpyRate != null)
            assert(jpyRate!!.exchangeRate.equals(131.4))

        }

    @Test
    fun `get currency exchange rates from repository when device online and Refresh Interval Expired which fetch data from remote datasource`() =
        runBlocking {

            val expiredInterval = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(DATA_REFRESH_INTERVAL + 10)

            val rateAED = RatesEntity("AED", 3.67, expiredInterval, "USD")
            val rateAUD = RatesEntity("AUD", 1.2, expiredInterval, "USD")

            val rateEntities = mutableListOf<RatesEntity>()
            rateEntities.add(rateAED)
            rateEntities.add(rateAUD)

            coEvery {
                connectionObserver.isOnline
            } returns flow {
                emit(true)
            }

            coEvery {
                currencyRatesDao.getAllRates()
            } returns rateEntities

            coJustRun {
                currencyRatesDao.insertRates(any())
            }

            val rates = repository.getRates()

            assert(rates.isNotEmpty())
        }


    @Test
    fun `get currency exchange rates from repository when device offline and Refresh Interval Expired which returns data from local database`() =
        runBlocking {

            val expiredInterval = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(DATA_REFRESH_INTERVAL + 10)

            val rateAED = RatesEntity("AED", 3.67, expiredInterval, "USD")
            val rateAUD = RatesEntity("AUD", 1.2, expiredInterval, "USD")

            val rateEntities = mutableListOf<RatesEntity>()
            rateEntities.add(rateAED)
            rateEntities.add(rateAUD)

            coEvery {
                connectionObserver.isOnline
            } returns flow {
                emit(false)
            }

            coEvery {
                currencyRatesDao.getAllRates()
            } returns rateEntities

            val rates = repository.getRates()

            assert(rates.size == rateEntities.size)
            assert(rates.first().exchangeRate == 3.67)
        }


    @Test
    fun `get currency exchange rates from repository when device online and local database data empty which fetch data from remote database`() =
        runBlocking {

            coEvery {
                connectionObserver.isOnline
            } returns flow {
                emit(true)
            }

            coEvery {
                currencyRatesDao.getAllRates()
            } returns emptyList()

            coJustRun {
                currencyRatesDao.insertRates(any())
            }

            val rates = repository.getRates()

            assert(rates.isNotEmpty())
        }


    @Test
    fun `get currency exchange rates from repository when device offline and local database data empty which return empty data`() =
        runBlocking {

            coEvery {
                connectionObserver.isOnline
            } returns flow {
                emit(false)
            }

            coEvery {
                currencyRatesDao.getAllRates()
            } returns emptyList()

            val rates = repository.getRates()
            assert(rates.isEmpty())
        }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }
}

