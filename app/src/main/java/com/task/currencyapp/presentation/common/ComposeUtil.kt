package com.task.currencyapp.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Compose util contains the reusable compose functions
 *
 */
internal object ComposeUtil {

    const val TEST_TAG_PROGRESS = "TAG_PROGRESS"
    const val TEST_TAG_ERROR = "TAG_ERROR"

    @Composable
    fun ErrorText(message: String?) {
        Box(modifier = Modifier.fillMaxSize().padding(20.dp).testTag(TEST_TAG_ERROR),
            contentAlignment = Alignment.Center
        ) {
            message?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    fontSize = 16.sp
                )
            }
        }
    }

    @Composable
    fun ProgressIndicator(message: String) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
                .testTag(TEST_TAG_PROGRESS),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(message)
            CircularProgressIndicator()
        }
    }
}