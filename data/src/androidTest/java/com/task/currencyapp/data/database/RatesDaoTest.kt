package com.task.currencyapp.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.currencyapp.data.local.RatesDatabase
import com.task.currencyapp.data.local.dao.RatesDao
import com.task.currencyapp.data.local.entities.RatesEntity
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RatesDaoTest {

    private lateinit var database: RatesDatabase
    private lateinit var ratesDao: RatesDao

    @Before
    fun before() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RatesDatabase::class.java
        ).allowMainThreadQueries().build()

        ratesDao = database.ratesDao()
    }

    @After
    fun after() {
        database.close()
    }

    @Test
    fun testInsertAndGetRateInDatabase() = runBlocking {

        val currencyCode = "AED"
        val rate = 3.67238

        val rateEntity = RatesEntity(currencyCode, rate, System.currentTimeMillis(), "USD")

        val rateEntities = mutableListOf<RatesEntity>()
        rateEntities.add(rateEntity)

        // Save entities
        ratesDao.insertRates(rateEntities)

        // Request rate by code
        val savedRate = ratesDao.getRate(currencyCode)

        // compare result
        assert(savedRate == rate)
    }

    @Test
    fun testInsertAllRatesInDatabase() = runBlocking {

        val rateAED = RatesEntity("AED", 3.67, System.currentTimeMillis(), "USD")
        val rateAUD = RatesEntity("AUD", 1.2, System.currentTimeMillis(), "USD")
        val rateJPY = RatesEntity("JPY", 131.4, System.currentTimeMillis(), "USD")

        val rateEntities = mutableListOf<RatesEntity>()
        rateEntities.add(rateAED)
        rateEntities.add(rateAUD)
        rateEntities.add(rateJPY)

        // Save entities
        ratesDao.insertRates(rateEntities)

        // get all rates
        val savedRates = ratesDao.getAllRates()

        // compare result
        assertTrue(savedRates.isNotEmpty())
        assertEquals(savedRates.size, rateEntities.size)
        assert(savedRates.first().exchangeRate == 3.67)
        assert(savedRates.last().currencyCode == "JPY")
    }

    @Test
    fun testGetRateNotPresentInDatabase() = runBlocking {

        val currencyCode = "USD"
        val rate = 1.0
        val currencyCodeNotSaved = "INVALID"

        val rateEntity = RatesEntity(currencyCode, rate, System.currentTimeMillis(), "USD")

        val rateEntities = mutableListOf<RatesEntity>()
        rateEntities.add(rateEntity)

        // Save entities
        ratesDao.insertRates(rateEntities)

        // Request rate for invalid code
        val invalidRate = ratesDao.getRate(currencyCodeNotSaved)

        // compare result
        assertNull(invalidRate)
    }
}