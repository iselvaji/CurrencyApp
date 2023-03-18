package com.task.currencyapp.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RatesDto(
    @SerialName("timestamp")
    val timeStamp : Long,
    @SerialName("base")
    val base : String,
    @SerialName("rates")
    var rates: MutableMap<String, Double>
)