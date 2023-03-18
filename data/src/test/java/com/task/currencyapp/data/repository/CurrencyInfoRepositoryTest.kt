package com.task.currencyapp.data.repository

import android.util.Log
import com.task.currencyapp.common.ConnectivityObserver
import com.task.currencyapp.data.local.dao.CurrencyInfoDao
import com.task.currencyapp.data.mock.ApiSuccessMockEngine
import com.task.currencyapp.data.remote.ApiClient
import com.task.currencyapp.data.remote.ApiServiceImpl
import com.task.currencyapp.data.repository.currencyInfo.CurrencyInfoRepository
import com.task.currencyapp.data.repository.currencyInfo.CurrencyInfoRepositoryImpl
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class CurrencyInfoRepositoryTest {

    @MockK
    private lateinit var repository: CurrencyInfoRepository

    private val apiMockClient = ApiClient.getClient(ApiSuccessMockEngine.getEngine(), "")
    private val apiService = ApiServiceImpl(apiMockClient)

    @MockK
    private lateinit var currencyInfoDao : CurrencyInfoDao

    @MockK
    private lateinit var connectionObserver : ConnectivityObserver


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every {
            Log.d(any(), any())
        } returns 0

        repository = mockk()
        currencyInfoDao = mockk()

        repository = CurrencyInfoRepositoryImpl(apiService,currencyInfoDao, connectionObserver)
    }

    @Test
    fun `get currencies from repository when device offline which returns data from local database and validate`() = runBlocking {

        val currencyCodeJPY = "JPY"
        val currencyNameJPY = "Japanese Yen"

        val currencyCodeUSD = "INR"
        val currencyNameUSD = "Indian Rupee"

        val currencies = HashMap<String, String>()
        currencies[currencyCodeJPY] = currencyNameJPY
        currencies[currencyCodeUSD] = currencyNameUSD

        coEvery {
            connectionObserver.isOnline
        } returns flow {
            emit(false)
        }

        coEvery {
            currencyInfoDao.getCurrenciesMap()
        } returns currencies

        val data = repository.getCurrenciesMap()
        assert(data.size == currencies.size)
        assert(data[currencyCodeJPY] == currencyNameJPY)
    }

    @Test
    fun `get currencies from repository when device online and local data not present which returns data from remote data source and validate`() = runBlocking {

       coEvery {
            connectionObserver.isOnline
        } returns flow {
           emit(true)
        }

       val locallySavedData = HashMap<String, String>()
       coEvery {
           currencyInfoDao.getCurrenciesMap()
       } returns locallySavedData

        coJustRun {
            currencyInfoDao.insertCurrencyInfo(any())
        }

        val data = repository.getCurrenciesMap()
        assert(data.isNotEmpty())
    }

    @Test
    fun `get currencies from repository when device offline and local data also not present which returns empty data and validate`() = runBlocking {

        coEvery {
            connectionObserver.isOnline
        } returns flow {
            emit(false)
        }

        val locallySavedData = HashMap<String, String>()
        coEvery {
            currencyInfoDao.getCurrenciesMap()
        } returns locallySavedData

        coJustRun {
            currencyInfoDao.insertCurrencyInfo(any())
        }

        val data = repository.getCurrenciesMap()

        assert(data.isEmpty())
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }
}

