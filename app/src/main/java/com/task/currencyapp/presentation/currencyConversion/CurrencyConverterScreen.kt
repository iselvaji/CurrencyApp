package com.task.currencyapp.presentation.currencyConversion

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.task.currencyapp.R
import com.task.currencyapp.common.ConnectivityObserver
import com.task.currencyapp.model.CurrencyInfo
import com.task.currencyapp.model.RateInfo
import com.task.currencyapp.presentation.common.ComposeUtil.ErrorText
import com.task.currencyapp.presentation.common.ComposeUtil.ProgressIndicator
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CurrencyConverterScreen(viewModel : CurrencyConverterViewModel,
                            connectivityObserver: ConnectivityObserver = get()) {

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) }
    ) {
        val state = viewModel.state.collectAsState()
        val isConnected by connectivityObserver.isOnline.collectAsState(initial = false)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val currencies = state.value.currenciesInfoList

            if(state.value.currenciesInfoError || currencies.isNullOrEmpty()) {
                if(!isConnected) {
                    ErrorText(stringResource(id = R.string.err_connectivity))
                }
                ErrorText(stringResource(id = R.string.err_unable_to_load))
            } else {
                CurrencyInputField(viewModel, state)
                CurrencyDropdownMenu(
                    onItemClicked = { selectedItem ->
                        viewModel.onEvent(
                            CurrencyConverterScreenEvent.OnCurrencySelectionChange(selectedItem)
                        )},
                    currencies = currencies
                )
                viewModel.onEvent(CurrencyConverterScreenEvent.OnCurrenciesFetched(currencies[0].symbol))
            }

            if (state.value.isLoading) {
                ProgressIndicator(stringResource(id = R.string.loading_progress))
            } else {
                val rates = state.value.rates
                if(state.value.ratesError
                    || state.value.currencyInputValidationError
                    || rates.isNullOrEmpty()
                ) {
                    ErrorText(stringResource(id = R.string.err_empty_data))
                }
                CurrencyExchangeRateList(rates!!)
            }
        }
    }
}

@Composable
fun CurrencyInputField(
    viewModel: CurrencyConverterViewModel,
    state: State<CurrencyConverterScreenState>
) {
    val context = LocalContext.current
    Column {
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            value = state.value.inputAmount ?: "",
            onValueChange = {
                viewModel.onEvent(CurrencyConverterScreenEvent.OnCurrencyAmountChange(it))
            },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .semantics { contentDescription = context.getString(R.string.enter_amount) },
            placeholder = {
                Text(text = LocalContext.current.getString(R.string.enter_amount))
            },
            maxLines = 1,
            singleLine = true,
        )
        if (state.value.currencyInputValidationError) {
            Text(
                text = stringResource(id = R.string.err_amount),
                fontSize = 15.sp,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrencyDropdownMenu(onItemClicked: (String) -> Unit, currencies: List<CurrencyInfo>) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(currencies[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption.codeAndName,
                   // getStringResourceByName(selectedOption.symbol, context),
            onValueChange = { },
            label = { Text(stringResource(R.string.currency)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .semantics { contentDescription = context.getString(R.string.currencies_list) }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.exposedDropdownSize()
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = currency
                        expanded = false
                        onItemClicked(currency.symbol)
                    }
                ) {
                    Text(currency.codeAndName)
                }
            }
        }
    }
}


@Composable
fun CurrencyExchangeRateList(currencyRates: List<RateInfo>) {
    val context = LocalContext.current

    LazyVerticalGrid(
        modifier = Modifier.semantics { contentDescription = context.getString(R.string.rates_list)},
        columns = GridCells.Fixed(2),
        content =  {
            items(currencyRates.size) { i->
                CurrencyRateItem(currencyRates[i])
            }
        })
}

@Composable
fun CurrencyRateItem(currency : RateInfo) {
    val context = LocalContext.current
    Card(
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(15.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(String.format("%.5f", currency.exchangeRate), fontWeight = FontWeight.W700)
            Text(currency.currencyCode)
            Text(currency.currencyName, color = Color.Gray)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


