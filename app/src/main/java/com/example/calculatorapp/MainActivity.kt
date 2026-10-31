package com.example.calculatorapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var currentDisplayValue by remember { mutableStateOf("") }

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
            "⌫" -> if (currentDisplayValue.isNotEmpty()) currentDisplayValue = currentDisplayValue.substring(0, currentDisplayValue.length - 1)
            "sin" -> currentDisplayValue += "sin("
            "cos" -> currentDisplayValue += "cos("
            "tan" -> currentDisplayValue += "tan("
            "√" -> currentDisplayValue += "√("
            "x^y" -> currentDisplayValue += "^"
            "ln" -> currentDisplayValue += "ln("
            else -> currentDisplayValue += key
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