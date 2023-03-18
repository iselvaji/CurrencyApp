package com.task.currencyapp.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.task.currencyapp.R
import com.task.currencyapp.presentation.common.ComposeUtil.TEST_TAG_ERROR
import com.task.currencyapp.presentation.common.ComposeUtil.TEST_TAG_PROGRESS
import com.task.currencyapp.ui.theme.CurrencyAppTheme
import com.task.currencyapp.presentation.currencyConversion.CurrencyConverterScreen
import com.task.currencyapp.presentation.navigation.Route
import com.task.currencyapp.utils.TestUtil.waitUntilDoesNotExist
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.compose.getViewModel
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class CurrencyConverterScreenTest : KoinTest{

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        composeRule.activity.setContent{
            navController = rememberNavController()

            CurrencyAppTheme {
                NavHost(navController = navController,
                        startDestination = Route.SCREEN_CURRENCY_MAIN) {
                    composable(Route.SCREEN_CURRENCY_MAIN) {
                        CurrencyConverterScreen(getViewModel())
                    }
                }
            }
        }
    }

    @Test
    fun test_CurrenciesListVisible() {
        composeRule.apply {
            waitUntilDoesNotExist(hasTestTag(TEST_TAG_PROGRESS))
            onNodeWithContentDescription(activity.getString(R.string.enter_amount)).assertIsDisplayed()
            onNodeWithContentDescription(activity.getString(R.string.currencies_list)).assertIsDisplayed()
            onNodeWithTag(TEST_TAG_ERROR).apply {
                assertIsDisplayed()
                hasText(activity.getString(R.string.err_empty_data))
            }
        }
    }

    @Test
    fun test_CurrenciesRatesVisible() {
        composeRule.apply {
            waitUntilDoesNotExist(hasTestTag(TEST_TAG_PROGRESS))
            onNodeWithContentDescription(activity.getString(R.string.enter_amount)).apply {
                assertIsDisplayed()
                performTextInput("10")
            }
            Thread.sleep(1000)
            waitUntilDoesNotExist(hasTestTag(TEST_TAG_PROGRESS))
            onAllNodesWithContentDescription(activity.getString(R.string.rates_list)).onFirst().assertExists()
            onNodeWithTag(TEST_TAG_ERROR).assertDoesNotExist()
        }
    }

    @Test
    fun test_CurrencyInputValidationErrorVisible() {
        composeRule.apply {
            waitUntilDoesNotExist(hasTestTag(TEST_TAG_PROGRESS))
            onNodeWithContentDescription(activity.getString(R.string.enter_amount)).apply {
                assertIsDisplayed()
                performTextInput("###")
            }
            onNodeWithText(activity.getString(R.string.err_amount)).assertIsDisplayed()
        }
    }
}
