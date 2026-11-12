package com.example.calculatorapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculatorapp.ui.theme.CustomCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomCalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainCalculatorView()
                }
            }
        }
    }
}

@Composable
fun MainCalculatorView() {
    var currentDisplayValue by rememberSaveable { mutableStateOf("") }

    val screenConfig = LocalConfiguration.current
    val isLandscapeView = screenConfig.orientation == Configuration.ORIENTATION_LANDSCAPE

    val scientificKeys = listOf(
        listOf("sin", "cos", "tan", "π"),
        listOf("√", "x^y", "ln", "e")
    )

    val standardKeys = listOf(
        listOf("C", "⌫", "(", ")"),
        listOf("7", "8", "9", "÷"),
        listOf("4", "5", "6", "×"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".", "+", "=")
    )

    val handleKeyPress: (String) -> Unit = { key ->
        when (key) {
            "C" -> currentDisplayValue = ""
            "⌫" -> {
                if (currentDisplayValue.isNotEmpty() && currentDisplayValue != "Błąd") {
                    currentDisplayValue = currentDisplayValue.substring(0, currentDisplayValue.length - 1)
                } else if (currentDisplayValue == "Błąd") {
                    currentDisplayValue = ""
                }
            }
            "=" -> {
                if (currentDisplayValue.isNotEmpty()) {
                    currentDisplayValue = evaluateMathExpression(currentDisplayValue)
                }
            }
            "sin" -> {
                if (currentDisplayValue == "Błąd") currentDisplayValue = ""
                currentDisplayValue += "sin("
            }
            "cos" -> {
                if (currentDisplayValue == "Błąd") currentDisplayValue = ""
                currentDisplayValue += "cos("
            }
            "tan" -> {
                if (currentDisplayValue == "Błąd") currentDisplayValue = ""
                currentDisplayValue += "tan("
            }
            "√" -> {
                if (currentDisplayValue == "Błąd") currentDisplayValue = ""
                currentDisplayValue += "√("
            }
            "x^y" -> {
                if (currentDisplayValue == "Błąd") currentDisplayValue = ""
                currentDisplayValue += "^"
            }
            "ln" -> {
                if (currentDisplayValue == "Błąd") currentDisplayValue = ""
                currentDisplayValue += "ln("
            }
            else -> {
                if (currentDisplayValue == "Błąd") currentDisplayValue = ""
                currentDisplayValue += key
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isLandscapeView) 0.25f else 0.3f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = currentDisplayValue.ifEmpty { "0" },
                style = MaterialTheme.typography.headlineLarge,
                fontSize = if (isLandscapeView) 28.sp else 36.sp,
                textAlign = TextAlign.End,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isLandscapeView) 0.75f else 0.7f)
        ) {
            if (isLandscapeView) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KeypadGrid(
                        keyLayout = scientificKeys,
                        onKeyTap = handleKeyPress,
                        modifier = Modifier.weight(1f)
                    )
                    KeypadGrid(
                        keyLayout = standardKeys,
                        onKeyTap = handleKeyPress,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                KeypadGrid(
                    keyLayout = standardKeys,
                    onKeyTap = handleKeyPress,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun KeypadGrid(
    keyLayout: List<List<String>>,
    onKeyTap: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keyLayout.forEach { rowKeys ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowKeys.forEach { symbol ->
                    Button(
                        onClick = { onKeyTap(symbol) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (symbol) {
                                "=" -> MaterialTheme.colorScheme.tertiary
                                "+", "×", "-", "÷" -> MaterialTheme.colorScheme.primary
                                "C", "⌫", "(", ")" -> MaterialTheme.colorScheme.secondaryContainer
                                "sin", "cos", "tan", "π", "√", "x^y", "ln", "e" -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            contentColor = when (symbol) {
                                "=" -> MaterialTheme.colorScheme.onTertiary
                                "+", "×", "-", "÷" -> MaterialTheme.colorScheme.onPrimary
                                "C", "⌫", "(", ")" -> MaterialTheme.colorScheme.onSecondaryContainer
                                "sin", "cos", "tan", "π", "√", "x^y", "ln", "e" -> MaterialTheme.colorScheme.onTertiaryContainer
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    ) {
                        Text(
                            text = symbol,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

fun evaluateMathExpression(expression: String): String {
    return try {
        val sanitized = expression
            .replace("×", "*")
            .replace("÷", "/")
            .replace("π", Math.PI.toString())
            .replace("e", Math.E.toString())
            .replace("√", "sqrt")

        val parser = SimpleExpressionParser(sanitized)
        val result = parser.parse()

        if (result.isNaN() || result.isInfinite()) {
            "Błąd"
        } else {
            if (result == result.toLong().toDouble()) {
                result.toLong().toString()
            } else {
                String.format(java.util.Locale.US, "%.6f", result).trimEnd('0').trimEnd('.')
            }
        }
    } catch (e: Exception) {
        "Błąd"
    }
}

class SimpleExpressionParser(private val input: String) {
    private var pos = -1
    private var ch = 0

    private fun nextChar() {
        ch = if (++pos < input.length) input[pos].toInt() else -1
    }

    private fun eat(charToEat: Int): Boolean {
        while (ch == ' '.toInt()) nextChar()
        if (ch == charToEat) {
            nextChar()
            return true
        }
        return false
    }

    fun parse(): Double {
        nextChar()
        val x = parseExpression()
        if (pos < input.length) throw RuntimeException("Błąd składni")
        return x
    }

    private fun parseExpression(): Double {
        var x = parseTerm()
        while (true) {
            when {
                eat('+'.toInt()) -> x += parseTerm()
                eat('-'.toInt()) -> x -= parseTerm()
                else -> return x
            }
        }
    }

    private fun parseTerm(): Double {
        var x = parseFactor()
        while (true) {
            when {
                eat('*'.toInt()) -> x *= parseFactor()
                eat('/'.toInt()) -> {
                    val divisor = parseFactor()
                    if (divisor == 0.0) throw ArithmeticException("Dzielenie przez zero")
                    x /= divisor
                }
                else -> return x
            }
        }
    }

    private fun parseFactor(): Double {
        var x = parseBase()
        if (eat('^'.toInt())) {
            x = Math.pow(x, parseFactor())
        }
        return x
    }

    private fun parseBase(): Double {
        if (eat('+'.toInt())) return parseBase()
        if (eat('-'.toInt())) return -parseBase()

        var x: Double
        val startPos = pos
        if (eat('('.toInt())) {
            x = parseExpression()
            eat(')'.toInt())
        } else if ((ch >= '0'.toInt() && ch <= '9'.toInt()) || ch == '.'.toInt()) {
            while ((ch >= '0'.toInt() && ch <= '9'.toInt()) || ch == '.'.toInt()) nextChar()
            x = input.substring(startPos, pos).toDouble()
        } else if ((ch >= 'a'.toInt() && ch <= 'z'.toInt()) || (ch >= 'A'.toInt() && ch <= 'Z'.toInt())) {
            while ((ch >= 'a'.toInt() && ch <= 'z'.toInt()) || (ch >= 'A'.toInt() && ch <= 'Z'.toInt())) nextChar()
            val func = input.substring(startPos, pos)
            x = parseBase()
            x = when (func) {
                "sqrt" -> {
                    if (x < 0) throw IllegalArgumentException("Pierwiastek z liczby ujemnej")
                    Math.sqrt(x)
                }
                "sin" -> Math.sin(Math.toRadians(x))
                "cos" -> Math.cos(Math.toRadians(x))
                "tan" -> Math.tan(Math.toRadians(x))
                "ln" -> {
                    if (x <= 0) throw IllegalArgumentException("Logarytm z liczby niedodatniej")
                    Math.log(x)
                }
                else -> throw RuntimeException("Nieznana funkcja")
            }
        } else {
            throw RuntimeException("Nieoczekiwany znak")
        }
        return x
    }
}