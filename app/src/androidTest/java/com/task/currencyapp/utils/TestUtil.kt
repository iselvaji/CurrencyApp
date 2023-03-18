package com.task.currencyapp.utils

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule

object TestUtil {

    fun ComposeContentTestRule.waitUntilDoesNotExist(
        matcher: SemanticsMatcher,
        timeoutMillis: Long = 10_000L
    ) {
        return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
    }

    private fun ComposeContentTestRule.waitUntilNodeCount(
        matcher: SemanticsMatcher,
        count: Int,
        timeoutMillis: Long = 10_000L
    ) {
        this.waitUntil(timeoutMillis) {
            this.onAllNodes(matcher).fetchSemanticsNodes().size == count
        }
    }
}