package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.joinToString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Home()
        }
    }
}

@Composable
fun Home() {
    val expressionList = remember { mutableStateListOf<Token>() }
    val expression = MathExpression(expressionList)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(30.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Text(fontSize = 50.sp, text = expression.strValue)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CalculatorButton(7, expression)
            CalculatorButton(8, expression)
            CalculatorButton(9, expression)
            CalculatorButton(OperatorToken.DIVIDE, expression)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CalculatorButton(4, expression)
            CalculatorButton(5, expression)
            CalculatorButton(6, expression)
            CalculatorButton(OperatorToken.MULTIPLY, expression)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CalculatorButton(1, expression)
            CalculatorButton(2, expression)
            CalculatorButton(3, expression)
            CalculatorButton(OperatorToken.SUBTRACT, expression)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CalculatorButton(0, expression)
            CalculatorButton("C", { expression.clear() }, expression)
            CalculatorButton("=", { expression.calculate() }, expression)
            CalculatorButton(OperatorToken.SUM, expression)
        }
    }
}

@Composable
fun CalculatorButton(value: Int, expression: MathExpression) {
    CalculatorButton(NumberToken(value), expression)
}

@Composable
fun CalculatorButton(token: Token, expression: MathExpression) {
    CalculatorButton(token.symbol, { it.append(token) }, expression)
}

@Composable
fun CalculatorButton(text: String, action: (MathExpression) -> Unit, expression: MathExpression) {
    Button(
        modifier = Modifier
            .padding(5.dp),
        onClick = { action(expression) }
    ) {
        Text(fontSize = 30.sp, fontWeight = FontWeight.Light, text = text)
    }
}

@Preview
@Composable
fun HomePreview() {
    Home()
}