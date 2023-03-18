package com.task.currencyapp.data.local

internal object DBConstants {

    const val DATABASE_NAME = "currency.db"

    // Currency exchange rates table
    const val TABLE_RATES = "rates"
    const val COLUMN_CURRENCY_CODE = "currency_code"
    const val COLUMN_EXCHANGE_RATE = "exchange_rate"
    const val COLUMN_TIME_STAMP = "time_stamp"
    const val COLUMN_BASE_CURRENCY = "base_currency"

    // Currency info table
    const val TABLE_CURRENCY_INFO = "currency_info"
    const val COLUMN_CURRENCY_SYMBOL = "currency_symbol"
    const val COLUMN_CURRENCY_NAME = "currency_name"
}