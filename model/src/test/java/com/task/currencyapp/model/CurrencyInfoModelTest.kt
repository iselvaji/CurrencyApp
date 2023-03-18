package com.task.currencyapp.model

import org.junit.Test

class CurrencyInfoModelTest {

    @Test
    fun `get currency code and name from CurrencyInfoModel and validate`() {

        val currencyInfo = CurrencyInfo("ABC", "Test")

        val currencyInfoCodeAndName = currencyInfo.codeAndName
        val expectedData = "ABC - Test"

        assert(expectedData == currencyInfoCodeAndName)
    }
}