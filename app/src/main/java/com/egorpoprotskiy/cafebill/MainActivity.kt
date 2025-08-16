package com.egorpoprotskiy.cafebill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.egorpoprotskiy.cafebill.ui.theme.CafeBillTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeBillTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipCalculatedApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TipCalculatedApp(
    modifier: Modifier = Modifier
) {
    var selectedTipPercent by remember { mutableStateOf(0) }
    var amountInput by remember { mutableStateOf("") }
    var numberOfPeopleInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.app_name_screen),
            style = MaterialTheme.typography.titleLarge
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            value = amountInput,
            onValueChange = {amountInput = it}
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            value = numberOfPeopleInput,
            onValueChange = {numberOfPeopleInput = it}
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedTipPercent == 0,
                    onClick = {selectedTipPercent = 0}
                )
                Text(stringResource(R.string.percent_0))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedTipPercent == 5,
                    onClick = {selectedTipPercent = 5}
                )
                Text(stringResource(R.string.percent_5))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedTipPercent == 10,
                    onClick = {selectedTipPercent = 10}
                )
                Text(stringResource(R.string.percent_10))
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedTipPercent == 15,
                    onClick = {selectedTipPercent = 15}
                )
                Text(stringResource(R.string.percent_15))
            }
        }
        val (tip, total, perPerson) = calculateTotal(
            amount = amountInput,
            tipPercent = selectedTipPercent,
            numberOfPeople = numberOfPeopleInput
        )
        Text(text = "Сумма чаевых: $tip")
        Text(text = "Общая сумма: $total")
        Text(text = "На одного человека: $perPerson")
    }
}

@Composable
private fun calculateTotal(
    amount: String,
    tipPercent: Int,
    numberOfPeople: String
) : Triple<String, String, String> {
    val billAmount = amount.toDoubleOrNull() ?: 0.0
    val peopleCount = numberOfPeople.toIntOrNull() ?: 0
    //сумма чаевых
    val tipAmount = (billAmount * tipPercent) / 100
    // расчет общей суммы
    val totalAmount = billAmount + tipAmount
    // расчет суммы на человека (если людей больше 0)
    val totalPerPerson = if (peopleCount > 0) {
        totalAmount / peopleCount
    } else{
        0.0 // Избегаем деления на ноль
    }

    // 5. Форматирование результатов в виде валюты
    val currencyFormatter = NumberFormat.getCurrencyInstance()
    val formattedTip = currencyFormatter.format(tipAmount)
    val formattedTotal = currencyFormatter.format(totalAmount)
    val formattedPerPerson = currencyFormatter.format(totalPerPerson)

    // 6. Возвращаем все три отформатированных значения
    return Triple(formattedTip, formattedTotal, formattedPerPerson)

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CafeBillTheme {
        TipCalculatedApp()
    }
}