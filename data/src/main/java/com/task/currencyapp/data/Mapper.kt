package com.task.currencyapp.data

import com.task.currencyapp.data.local.entities.CurrencyInfoEntity
import com.task.currencyapp.data.local.entities.RatesEntity
import com.task.currencyapp.data.remote.model.RatesDto
import com.task.currencyapp.model.RateInfo


internal fun RatesDto.toRateEntityList(timestamp: Long) : List<RatesEntity> {
    return this.rates.map { entry ->
        RatesEntity(entry.key, entry.value, timestamp, this.base)
    }
}

internal fun RatesEntity.toRateInfo() : RateInfo {
    return RateInfo(
        this.currencyCode,
        this.exchangeRate,
        ""
    )
}

internal fun Map<String, String>.toCurrencyInfoEntityList() : List<CurrencyInfoEntity> {
    return this.map { entry ->
        CurrencyInfoEntity(entry.key, entry.value)
    }
}

