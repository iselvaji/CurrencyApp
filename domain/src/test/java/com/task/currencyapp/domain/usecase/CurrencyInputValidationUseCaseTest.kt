package com.task.currencyapp.domain.usecase

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CurrencyInputValidationUseCaseTest {

    private lateinit var useCase: CurrencyInputValidationUseCase

    @Before
    fun setUp() {
        useCase = CurrencyInputValidationUseCase()
    }

    @Test
    fun `check currency input validation result with valid amount`() = runBlocking {
        val expected = ValidationResult(successful = true, error = null)
        val result = useCase.validate("100.00")
        assertEquals(expected, result)
    }

    @Test
    fun `check currency input validation result with invalid data`() = runBlocking {
        val expected = ValidationResult(successful = false, error = ValidationError.InvalidValue)
        val result = useCase.validate("XCSF")
        assertEquals(expected, result)
    }

    @Test
    fun `check currency input validation result with empty data`() = runBlocking {
        val expected = ValidationResult(successful = false, error = ValidationError.RequiredField)
        val result = useCase.validate("")
        assertEquals(expected, result)
    }
}