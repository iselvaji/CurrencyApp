package com.task.currencyapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.currencyapp.ui.theme.CurrencyAppTheme
import com.task.currencyapp.presentation.currencyConversion.CurrencyConverterScreen
import com.task.currencyapp.presentation.navigation.Route
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyAppTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.SCREEN_CURRENCY_MAIN
                    ) {
                        composable(Route.SCREEN_CURRENCY_MAIN) {
                            CurrencyConverterScreen(getViewModel())
                        }
                    }
                }
            }
        }
    }
}