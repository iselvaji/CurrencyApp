package com.task.currencyapp.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.currencyapp.data.local.*
import com.task.currencyapp.data.local.dao.CurrencyInfoDao
import com.task.currencyapp.data.local.entities.CurrencyInfoEntity
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyInfoDaoTest {

    private lateinit var database: RatesDatabase
    private lateinit var currencyInfoDao : CurrencyInfoDao

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RatesDatabase::class.java
        ).allowMainThreadQueries().build()

        currencyInfoDao = database.currencyInfoDao()
    }

    @After
    fun after() {
        database.close()
    }

    @Test
    fun testInsertAndGetCurrenciesInDatabase() = runBlocking {

        val currencyCodeJPY = "JPY"
        val currencyNameJPY = "Japanese Yen"

        val currencyCodeUSD = "USD"
        val currencyNameUSD = "United States of America"

        val currencyJPY = CurrencyInfoEntity(currencyCodeJPY, currencyNameJPY)
        val currencyUSD = CurrencyInfoEntity(currencyCodeUSD, currencyNameUSD)

        val currencies = mutableListOf<CurrencyInfoEntity>()
        currencies.add(currencyJPY)
        currencies.add(currencyUSD)

        // Save entities
        currencyInfoDao.insertCurrencyInfo(currencies)

        val savedData = currencyInfoDao.getCurrenciesMap()

        // compare result
        assert(savedData.isNotEmpty())
        assertEquals(savedData.size, currencies.size)
        assertEquals(savedData[currencyCodeJPY], currencyNameJPY)
        assertEquals(savedData[currencyCodeUSD], currencyNameUSD)
    }

}