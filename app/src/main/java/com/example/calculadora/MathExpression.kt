package com.example.calculadora

class MathExpression(val tokens: MutableList<Token>) {
    val values: List<Token>
        get() = tokens

    val strValue: String
        get() = tokens.joinToString(separator = " ") { it.symbol }

    constructor(token: Token) : this(mutableListOf()) {
        tokens.add(token)
    }

    constructor(number: Int) : this(NumberToken(number))

    fun append(token: Token) {
        when (token) {
            is NumberToken -> addNumberToken(token)
            is OperatorToken -> addOperatorToken(token)
            else -> tokens.add(token)
        }
    }

    private fun addNumberToken(token: NumberToken) {
        if (tokens.isEmpty()) {
            tokens.add(token)
            return
        }

        val last = tokens.last()
        if (last is NumberToken) {
            tokens[tokens.lastIndex] = NumberToken((last.value * 10) + token.value)
        } else {
            tokens.add(token)
        }
    }

    private fun addOperatorToken(token: OperatorToken) {
        if (!tokens.isEmpty()) {
            val last = tokens.last()
            if (last is OperatorToken) {
                tokens[tokens.lastIndex] = token
                return
            }
        }

        tokens.add(token)
    }

    fun clear() = tokens.clear()

    fun calculate() {
        if (tokens.isEmpty()) return

        validateSyntax(tokens)

        var result = tokens.toList()
        while (canBeSimplified(result)) {
            result = solveHighestPriority(result)
        }

        tokens.clear()
        tokens.add(resultToNumberToken(result))
    }

    private fun validateSyntax(tokens: List<Token>) {
        if (tokens.size % 2 == 0) {
            throw IllegalStateException("Invalid syntax - Expression must be [NumberToken, OperatorToken, ...] and end with a NumberToken")
        }

        for (i in 0..<tokens.size) {
            if (i % 2 == 0) {
                if (tokens[i] is OperatorToken) {
                    throw IllegalStateException("Invalid syntax - Expression must be [NumberToken, OperatorToken, ...] and end with a NumberToken")
                }
            } else if (tokens[i] is NumberToken) {
                throw IllegalStateException("Invalid syntax - Expression must be [NumberToken, OperatorToken, ...] and end with a NumberToken")
            }
        }
    }

    private fun canBeSimplified(expression: List<Token>) = expression.size > 1

    private fun solveHighestPriority(expression: List<Token>): List<Token> {
        val mutableExpression = expression.toMutableList()
        var simpleExpressionToSolve: SimpleExpression? = null
        var firstElementIndex: Int? = null

        println(expression)
        var i = 0
        while (i < expression.size - 1) {
            val n1 = expression[i] as NumberToken
            val operator = expression[i + 1] as OperatorToken
            val n2 = expression[i + 2] as NumberToken
            val newSimpleExpression = SimpleExpression(n1, n2, operator)

            if (simpleExpressionToSolve == null || newSimpleExpression.priority > simpleExpressionToSolve.priority) {
                firstElementIndex = i
                simpleExpressionToSolve = newSimpleExpression
            }

            i += 2
        }

        mutableExpression[firstElementIndex!!] = NumberToken(simpleExpressionToSolve!!.calculate())
        mutableExpression.removeAt(firstElementIndex + 1)
        mutableExpression.removeAt(firstElementIndex + 1)

        return mutableExpression.toList()
    }

    private fun resultToNumberToken(result: List<Token>): NumberToken =
        NumberToken((result[0] as NumberToken).value)
}

open class NumberToken(val value: Int) : Token {
    override val symbol: String = value.toString()
}

enum class OperatorToken(override val symbol: String, val priority: Int, val exec: (Int, Int) -> Int) : Token {
    SUM("+", 1, { a, b -> a + b }),
    SUBTRACT("-", 1, { a, b -> a - b }),
    DIVIDE("/", 2, { a, b -> a / b }),
    MULTIPLY("*", 2, { a, b -> a * b }),
    ;
}

class SimpleExpression(val n1: NumberToken, val n2: NumberToken, val operator: OperatorToken) {
    val priority: Int
        get() = operator.priority

    fun calculate() = operator.exec(n1.value, n2.value)
}

interface Token {
    val symbol: String
}
