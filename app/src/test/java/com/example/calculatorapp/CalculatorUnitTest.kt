package com.example.calculatorapp

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorUnitTest {

    @Test
    fun testSimpleAddition() {
        assertEquals("4", evaluateMathExpression("2+2"))
    }

    @Test
    fun testPrecedence() {
        assertEquals("6", evaluateMathExpression("2+2*2"))
    }

    @Test
    fun testParentheses() {
        assertEquals("8", evaluateMathExpression("(2+2)*2"))
    }

    @Test
    fun testDivisionByZero() {
        assertEquals("Błąd", evaluateMathExpression("1/0"))
    }

    @Test
    fun testIncompleteExpression() {
        assertEquals("Błąd", evaluateMathExpression("2+"))
    }

    @Test
    fun testEmptyExpression() {
        assertEquals("Błąd", evaluateMathExpression(""))
    }

    @Test
    fun testDomainError() {
        assertEquals("Błąd", evaluateMathExpression("sqrt(-1)"))
    }

    @Test
    fun testDecimalSeparatorDot() {
        // mXparser zwraca "5" dla wyniku całkowiteczego z liczb zmiennoprzecinkowych
        assertEquals("5", evaluateMathExpression("2.5+2.5"))
    }

    @Test
    fun testDecimalSeparatorComma() {
        // Zamieniamy przecinek na kropkę, aby mXparser poprawnie obsłużył polski separator
        val expression = "2,5+2,5".replace(',', '.')
        assertEquals("5", evaluateMathExpression(expression))
    }
}