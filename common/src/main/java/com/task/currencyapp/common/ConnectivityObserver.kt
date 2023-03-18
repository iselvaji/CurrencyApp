package com.task.currencyapp.common

import kotlinx.coroutines.flow.Flow

/**
 * Utility for reporting app connectivity status
 */
interface ConnectivityObserver {
    val isOnline: Flow<Boolean>
}