package com.task.currencyapp.domain.usecase

import java.lang.Double.parseDouble

/**
 * validate the amount to be converted provided by the user and return the validation result
 */
class CurrencyInputValidationUseCase {
    fun validate(amountEntered: String?) : ValidationResult {
        if(amountEntered.isNullOrEmpty()) {
            return ValidationResult(
                successful = false,
                error = ValidationError.RequiredField
            )
        }
        try {
            val amount = parseDouble(amountEntered)
            if(amount <= 0)
                return ValidationResult(successful = false, error = ValidationError.InvalidValue)

        } catch (e: NumberFormatException) {
            return ValidationResult(successful = false, error = ValidationError.InvalidValue)
        }

        return ValidationResult(successful = true, error = null)
    }
}

data class ValidationResult(val successful: Boolean, val error: ValidationError? = null)

sealed class ValidationError {
    object RequiredField : ValidationError()
    object InvalidValue : ValidationError()
}