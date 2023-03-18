package com.task.currencyapp.common.testUtils

import com.task.currencyapp.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider : DispatcherProvider {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val main: CoroutineDispatcher
        get() = testDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    override val io: CoroutineDispatcher
        get() = testDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    override val default: CoroutineDispatcher
        get() = testDispatcher

}